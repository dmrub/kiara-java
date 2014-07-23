/*
 * Copyright (C) 2014 shahzad
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

import com.fasterxml.jackson.databind.JsonNode;
import de.dfki.kiara.*;
import de.dfki.kiara.idl.KiaraKTDConstructor;
import de.dfki.kiara.idl.KiaraLexer;
import de.dfki.kiara.idl.KiaraParser;
import de.dfki.kiara.jsonrpc.JsonRpcMessage;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.World;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author shahzad
 */
public class ServiceImpl implements Service {

    private final Binder binder;
    private final World world;
    private final Module module;

    public ServiceImpl(Context context) {
        this.binder = new BinderImpl();
        world = new World();
        module = new Module(world, "kiara");
    }

    @Override
    public void loadIDL(String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadServiceIDLFromString(String idlLanguage, String idlContents) throws IDLParseException {
        try {
            loadIDL(idlContents, null);
        } catch (IOException ex) {
            throw new IDLParseException(ex);
        }
    }

    @Override
    public String getIDLContents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param idlMethodName
     * @param serviceMethodName
     * @param serviceImpl
     * @throws MethodAlreadyBoundException
     * @throws java.lang.NoSuchMethodException
     */
    @Override
    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName) throws MethodAlreadyBoundException,
            NoSuchMethodException, SecurityException {
        if (binder.getServiceMethod(idlMethodName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        binder.bindServiceMethod(idlMethodName, serviceImpl, serviceMethodName);
    }

    /**
     *
     * @param idlMethodName
     * @param serviceMethodName
     * @param serviceImpl
     * @param parameterTypes
     * @throws MethodAlreadyBoundException
     * @throws java.lang.NoSuchMethodException
     */
    @Override
    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName, Class<?>... parameterTypes)
            throws MethodAlreadyBoundException, NoSuchMethodException, SecurityException {
        if (binder.getServiceMethod(idlMethodName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        binder.bindServiceMethod(idlMethodName, serviceImpl, serviceMethodName, parameterTypes);
    }

    @Override
    public void unregisterServiceFunction(String idlMethodName) throws NoSuchMethodException {
        binder.unbindServiceMethod(idlMethodName);
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

    /**
     *
     * @param messageString
     */
    @Override
    public void DbgSimulateCall(String messageString) {
        JsonRpcProtocol protocol = new JsonRpcProtocol();

        try {
            Message rpcMessage = protocol.createRequestMessageFromData(ByteBuffer.wrap(messageString.getBytes("UTF-8")));
            String methodName = rpcMessage.getMethodName();

            ServiceMethodBinder serviceMethod = binder.getServiceMethod(methodName);

            System.out.println(serviceMethod.getBoundedMethod().invoke(
                    serviceMethod.getImplementedClass(), rpcMessage.getRequestObject(serviceMethod.getBoundedMethod().getParameterTypes()).args.toArray()));

        } catch (IOException | InvocationTargetException | IllegalAccessException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}
