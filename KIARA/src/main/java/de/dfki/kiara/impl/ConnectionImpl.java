/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.impl;

import de.dfki.kiara.ConnectException;
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceCodeGen;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.MessageConnection;
import de.dfki.kiara.MethodAlreadyBoundException;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.NoSuchIDLFunctionException;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.ProtocolRegistry;
import de.dfki.kiara.ServiceMethodExecutor;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportRegistry;
import de.dfki.kiara.config.ServerConfiguration;
import de.dfki.kiara.config.ServerInfo;
import de.dfki.kiara.idl.IDLWriter;
import de.dfki.kiara.idl.KiaraKTDConstructor;
import de.dfki.kiara.idl.KiaraLexer;
import de.dfki.kiara.idl.KiaraParser;
import de.dfki.kiara.ktd.Annotation;
import de.dfki.kiara.ktd.FunctionType;
import de.dfki.kiara.ktd.KTDObject;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.World;
import de.dfki.kiara.util.URILoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ConnectionImpl implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private final Protocol protocol;
    private final Transport transport;
    private final TransportConnection transportConnection;
    private final TransportMessageConnection messageConnection;
    private final World world;
    private final Module module;
    private final ServiceMethodBinding methodBinding;
    private final InterfaceCodeGen codegen;

    @SuppressWarnings("null")
    ConnectionImpl(String configUriStr) throws IOException {
        methodBinding = new ServiceMethodBinding();
        world = new World();
        module = new Module(world, "kiara");

        URI configUri;
        try {
            configUri = new URI(configUriStr);
        } catch (URISyntaxException ex) {
            throw new ConnectException("Invalid configuration URI", ex);
        }

        // 1. load server configuration
        String configText;
        try {
            configText = URILoader.load(configUri, "UTF-8");
        } catch (IOException ex) {
            throw new ConnectException("Could not load server configuration", ex);
        }

        logger.debug("Config text: {}", configText);

        ServerConfiguration serverConfig;
        try {
            serverConfig = ServerConfiguration.fromJson(configText);
        } catch (IOException ex) {
            throw new ConnectException("Could not parse server configuration", ex);
        }

        //???DEBUG BEGIN
        if (logger.isDebugEnabled()) {
            try {
                logger.debug(serverConfig.toJson());
                //System.err.println(serverConfig.toJson());
            } catch (IOException ex) {
                throw new ConnectException("Could not convert to JSON", ex);
            }
        }
        //???DEBUG END

        // load IDL

        if (serverConfig.idlContents != null && !serverConfig.idlContents.isEmpty()) {
            loadIDL(serverConfig.idlContents, configUri.toString());
        } else if (serverConfig.idlURL != null && !serverConfig.idlURL.isEmpty()) {
            URI idlUri = configUri.resolve(serverConfig.idlURL);
            String idlContents = URILoader.load(idlUri, "UTF-8");

            logger.debug("IDL CONTENTS: {}", idlContents); //???DEBUG

            loadIDL(idlContents, idlUri.toString());
        } else {
            throw new ConnectException("No IDL specified in server configuration");
        }

        //???DEBUG
        if (logger.isDebugEnabled()) {
            IDLWriter idlWriter = new IDLWriter(module);
            idlWriter.write(System.err);
        }

        // 2. perform negotation

        // find matching endpoint
        ServerInfo serverInfo = null;
        Transport selectedTransport = null;

        for (ServerInfo si : serverConfig.servers) {
            Transport t = TransportRegistry.getTransportByName(si.transport.name);
            if (t != null) {
                // we change selected endpoint only if priority is higher
                // i.e. when priority value is less than current one
                if (selectedTransport != null && selectedTransport.getPriority() < t.getPriority())
                    continue;

                serverInfo = si;
                selectedTransport = t;
            }
        }

        if (serverInfo == null)
            throw new ConnectException("No matching endpoint found");

        transport = selectedTransport;

        logger.debug("Selected transport: {}", serverInfo.transport.name);
        logger.debug("Selected protocol: {}", serverInfo.protocol.name);

        // FIXME load plugin classes ?

        // load required protocol
        String protocolName = serverInfo.protocol.name;
        //String protocolName = "javaobjectstream";

        try {
            protocol = ProtocolRegistry.newProtocolByName(protocolName);
        } catch (InstantiationException ex) {
            throw new ConnectException("Could not instantiate protocol", ex);
        } catch (IllegalAccessException ex) {
            throw new ConnectException("Could not instantiate protocol", ex);
        }

        if (protocol == null)
            throw new ConnectException("Unsupported protocol '" + protocolName + "'");


        URI transportUri = configUri.resolve(serverInfo.transport.url);

        logger.debug("Open transport connection to: {}", transportUri);

        try {
            transportConnection = transport.openConnection(transportUri.toString(), null).get();
        } catch (InvalidAddressException ex) {
            throw new ConnectException(ex);
        } catch (InterruptedException ex) {
            throw new ConnectException(ex);
        } catch (ExecutionException ex) {
            throw new ConnectException(ex);
        }

        messageConnection = new TransportMessageConnection(transportConnection, protocol);
        codegen = protocol.createInterfaceCodeGen(this);
    }

    private void loadIDL(InputStream stream, String fileName) throws IOException {
        loadIDL(new ANTLRInputStream(stream), fileName);
    }

    private void loadIDL(String idlContents, String fileName) throws IOException {
        loadIDL(new ANTLRInputStream(idlContents), fileName);
    }

    private void loadIDL(ANTLRInputStream input, String fileName) throws IOException {
        // create a lexer that feeds off of input CharStream
        KiaraLexer lexer = new KiaraLexer(input);
        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        KiaraParser parser = new KiaraParser(tokens);
        ParseTree tree = parser.program(); // begin parsing at program rule

        ParseTreeWalker walker = new ParseTreeWalker();

        KiaraKTDConstructor ktdConstructor = new KiaraKTDConstructor(module, fileName);
        walker.walk(ktdConstructor, tree);

        if (!ktdConstructor.getParserErrors().isEmpty()) {
            StringBuilder b = new StringBuilder("IDL parser errors:");
            b.append(System.lineSeparator());
            for (String error : ktdConstructor.getParserErrors()) {
                b.append(error);
                b.append(System.lineSeparator());
            }
            throw new IOException(b.toString());
        }
    }

    @Override
    public void close() throws IOException {
        if (transportConnection != null)
            transportConnection.close();
    }

    @Override
    public <T> T getServiceInterface(MethodBinding<T> methodBinding) {
        InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinding);
        Class<T> interfaceClass = mapping.getInterfaceClass();

        return codegen.generateInterfaceImpl(interfaceClass, mapping);
    }

    public ServiceMethodBinding getServiceMethodBinding() {
        return methodBinding;
    }

    @Override
    public ServiceMethodExecutor getServiceMethodExecutor() {
        return methodBinding;
    }

    @Override
    public TransportConnection getTransportConnection() {
        return transportConnection;
    }

    @Override
    public void registerServiceFunction(String idlFunctionName, Object serviceImpl, String serviceMethodName) throws NoSuchIDLFunctionException, MethodAlreadyBoundException, NoSuchMethodException, SecurityException {
        KTDObject object = module.lookupObject(idlFunctionName);

        if (!(object instanceof FunctionType))
            throw new NoSuchIDLFunctionException("No such IDL function: '"+idlFunctionName+"'");

        FunctionType funcTy = (FunctionType)object;

        Annotation annotation = Annotation.getFirstAnnotationOfType(funcTy, module.getWorld().getCallbackAnnotation());
        if (annotation == null)
            throw new IllegalArgumentException(idlFunctionName+" is not a callback function");

        if (methodBinding.getServiceMethodBinder(idlFunctionName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        methodBinding.bindServiceMethod(idlFunctionName, serviceImpl, serviceMethodName);
    }

    @Override
    public void registerServiceFunction(String idlFunctionName, Object serviceImpl, String serviceMethodName, Class<?>... parameterTypes) throws NoSuchIDLFunctionException, MethodAlreadyBoundException, NoSuchMethodException, SecurityException  {
        KTDObject object = module.lookupObject(idlFunctionName);

        if (!(object instanceof FunctionType))
            throw new NoSuchIDLFunctionException("No such IDL function: '"+idlFunctionName+"'");

        FunctionType funcTy = (FunctionType)object;

        Annotation annotation = Annotation.getFirstAnnotationOfType(funcTy, module.getWorld().getCallbackAnnotation());
        if (annotation == null)
            throw new IllegalArgumentException(idlFunctionName+" is not a callback function");

        if (methodBinding.getServiceMethodBinder(idlFunctionName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        methodBinding.bindServiceMethod(idlFunctionName, serviceImpl, serviceMethodName, parameterTypes);
    }

    @Override
    public void unregisterServiceFunction(String idlFunctionName) throws NoSuchIDLFunctionException {
        methodBinding.unbindServiceMethod(idlFunctionName);
    }

    @Override
    public MessageConnection getMessageConnection() {
        return messageConnection;
    }

}
