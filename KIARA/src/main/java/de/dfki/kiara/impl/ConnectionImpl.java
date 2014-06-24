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

import de.dfki.kiara.Connection;
import de.dfki.kiara.ConnectionException;
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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ConnectionImpl implements Connection {

    private static final Map<String, Class<? extends Protocol>> protocolRegistry = new HashMap<>();
    private final Protocol protocol;
    private final World world;
    private final Module module;

    static {
        // initialize protocols
        protocolRegistry.put("jsonrpc", JsonRpcProtocol.class);
        protocolRegistry.put("javaobjectstream", JosProtocol.class);
    }

    ConnectionImpl(String configUri) throws IOException {
        world = new World();
        module = new Module(world, "kiara");

        // 1. load server configuration

        String configText;
        try {
            byte[] serverConfigData = URILoader.load(configUri);
            configText = new String(serverConfigData, "UTF-8");
        } catch (URISyntaxException ex) {
            throw new ConnectionException("Invalid configuration URI", ex);
        } catch (IOException ex) {
            throw new ConnectionException("Could not load server configuration", ex);
        }

        ServerConfiguration serverConfig;
        try {
            serverConfig = ServerConfiguration.fromJSON(configText);
        } catch (IOException ex) {
            throw new ConnectionException("Could not parse server configuration", ex);
        }

        try {
            System.err.println(serverConfig.toJSON());
        } catch (IOException ex) {
            throw new ConnectionException("Could not convert to JSON", ex);
        }

        // load IDL

        if (serverConfig.idlContents != null && !serverConfig.idlContents.isEmpty()) {
            loadIDL(new ByteArrayInputStream(serverConfig.idlContents.getBytes("UTF-8")), configUri);
        }

        //???DEBUG
        IDLWriter idlWriter = new IDLWriter(module);
        idlWriter.write(System.err);

        // 2. perform negotation

        // 3. select implementation
        String protocolName = "jsonrpc";
        //String protocolName = "javaobjectstream";
        Class<? extends Protocol> protocolClass = protocolRegistry.get(protocolName);
        if (protocolClass == null)
            throw new ConnectionException("Unsupported protocol '"+protocolName+"'");

        try {
            protocol = protocolClass.newInstance();
        } catch (InstantiationException ex) {
            throw new ConnectionException("Could not instantiate protocol", ex);
        } catch (IllegalAccessException ex) {
            throw new ConnectionException("Could not instantiate protocol", ex);
        }

        protocol.initConnection(this);
    }

    public void loadIDL(InputStream stream, String fileName) throws IOException {
        // create a CharStream that reads from standard input
        ANTLRInputStream input = new ANTLRInputStream(stream);
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
