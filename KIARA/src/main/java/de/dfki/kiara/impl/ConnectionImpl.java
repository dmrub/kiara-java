/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.impl;

import de.dfki.kiara.ConnectException;
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceCodeGen;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.MethodBinder;
import de.dfki.kiara.Protocol;
import de.dfki.kiara.config.ServerConfiguration;
import de.dfki.kiara.idl.IDLWriter;
import de.dfki.kiara.idl.KiaraKTDConstructor;
import de.dfki.kiara.idl.KiaraLexer;
import de.dfki.kiara.idl.KiaraParser;
import de.dfki.kiara.jos.JosProtocol;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.World;
import de.dfki.kiara.util.URILoader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ConnectionImpl implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);

    private static final Map<String, Class<? extends Protocol>> protocolRegistry = new HashMap<>();
    private final Protocol protocol;
    private final World world;
    private final Module module;

    static {
        // initialize protocols
        protocolRegistry.put("jsonrpc", JsonRpcProtocol.class);
        protocolRegistry.put("javaobjectstream", JosProtocol.class);
    }

    ConnectionImpl(String configUriStr) throws IOException {
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
                System.err.println(serverConfig.toJson());
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
        }

        //???DEBUG
        if (logger.isDebugEnabled()) {
            IDLWriter idlWriter = new IDLWriter(module);
            idlWriter.write(System.err);
        }

        // 2. perform negotation
        // 3. select implementation
        String protocolName = "jsonrpc";
        //String protocolName = "javaobjectstream";
        Class<? extends Protocol> protocolClass = protocolRegistry.get(protocolName);
        if (protocolClass == null) {
            throw new ConnectException("Unsupported protocol '" + protocolName + "'");
        }

        try {
            protocol = protocolClass.newInstance();
        } catch (InstantiationException ex) {
            throw new ConnectException("Could not instantiate protocol", ex);
        } catch (IllegalAccessException ex) {
            throw new ConnectException("Could not instantiate protocol", ex);
        }

        protocol.initConnection(this);
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
    }

    @Override
    public <T> T generateClientFunctions(MethodBinder<T> methodBinder) {
        InterfaceMapping<T> mapping = new InterfaceMapping<>(methodBinder);
        Class<T> interfaceClass = mapping.getInterfaceClass();

        InterfaceCodeGen codegen = protocol.getInterfaceCodeGen();
        return codegen.generateInterfaceImpl(interfaceClass, mapping);
    }

}
