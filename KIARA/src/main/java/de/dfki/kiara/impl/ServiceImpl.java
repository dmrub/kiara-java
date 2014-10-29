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

import com.google.common.reflect.TypeToken;
import de.dfki.kiara.*;
import de.dfki.kiara.idl.IDLWriter;
import de.dfki.kiara.idl.KiaraKTDConstructor;
import de.dfki.kiara.idl.KiaraLexer;
import de.dfki.kiara.idl.KiaraParser;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.ktd.Module;
import de.dfki.kiara.ktd.World;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author shahzad
 */
public class ServiceImpl implements Service {

    private final ServiceMethodExecutorImpl methodExecutor;
    private final World world;
    private final Module module;

    public ServiceImpl(Context context) {
        this.methodExecutor = new ServiceMethodExecutorImpl();
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
        try {
            IDLWriter idlWriter = new IDLWriter(module);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            idlWriter.write(ps);
            ps.close();
            return baos.toString("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // TODO add log message
            return "";
        }
    }

    /**
     * @param idlFunctionName
     * @param serviceMethodName
     * @param serviceImpl
     * @throws MethodAlreadyBoundException
     * @throws java.lang.NoSuchMethodException
     */
    @Override
    public void registerServiceFunction(String idlFunctionName, Object serviceImpl,
            String serviceMethodName) throws MethodAlreadyBoundException,
            NoSuchMethodException, SecurityException {
        if (methodExecutor.getServiceMethodBinder(idlFunctionName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        methodExecutor.bindServiceMethod(idlFunctionName, serviceImpl, serviceMethodName);
    }

    /**
     * @param idlFunctionName
     * @param serviceMethodName
     * @param serviceImpl
     * @param parameterTypes
     * @throws MethodAlreadyBoundException
     * @throws java.lang.NoSuchMethodException
     */
    @Override
    public void registerServiceFunction(String idlFunctionName, Object serviceImpl,
            String serviceMethodName, Class<?>... parameterTypes)
            throws MethodAlreadyBoundException, NoSuchMethodException, SecurityException {
        if (methodExecutor.getServiceMethodBinder(idlFunctionName) != null) {
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        methodExecutor.bindServiceMethod(idlFunctionName, serviceImpl, serviceMethodName, parameterTypes);
    }

    @Override
    public void unregisterServiceFunction(String idlFunctionName) throws NoSuchIDLFunctionException {
        methodExecutor.unbindServiceMethod(idlFunctionName);
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
     * @param messageString
     */
    @Override
    public Object dbgSimulateCall(ByteBuffer payload) throws Exception {
        JsonRpcProtocol protocol = new JsonRpcProtocol();

        Message rpcMessage = protocol.createMessageFromData(payload);
        String methodName = rpcMessage.getMethodName();

        ServiceMethodBinder serviceMethod = methodExecutor.getServiceMethodBinder(methodName);

        try {
            java.lang.reflect.Type[] genericParamTypes = serviceMethod.getBoundMethod().getGenericParameterTypes();
            TypeToken<?>[] paramTypes = new TypeToken<?>[genericParamTypes.length];
            for (int i = 0; i < genericParamTypes.length; ++i) {
                paramTypes[i] = TypeToken.of(genericParamTypes[i]);
            }
            return serviceMethod.getBoundMethod().invoke(
                serviceMethod.getImplementedClass(),
                rpcMessage.getRequestObject(paramTypes).args.toArray());
        } catch (InvocationTargetException ex) {
            throw (Exception)ex.getTargetException();
        }
    }

    public ServiceMethodExecutorImpl getMethodExecutor() {
        return methodExecutor;
    }
}
