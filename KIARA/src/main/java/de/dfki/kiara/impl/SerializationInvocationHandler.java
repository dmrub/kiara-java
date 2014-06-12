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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.Util;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class SerializationInvocationHandler extends BasicInvocationHandler {

    private final Gson gson = new Gson();

    public SerializationInvocationHandler(InterfaceMapping<?> interfaceMapping) {
        super(interfaceMapping);
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        InterfaceMapping<?> mapping = getInterfaceMapping();

        final String idlMethodName = mapping.getIDLMethodName(method);
        if (idlMethodName != null) {
            String out = gson.toJson(os[0]);

            if (Util.isSerializer(method)) {
                JsonRpcHeader header = new JsonRpcHeader(idlMethodName, os, 1);

                ObjectMapper mapper = new ObjectMapper();
                ByteBuffer buf = ByteBuffer.wrap(mapper.writeValueAsBytes(header));

                return new DummyMessage(idlMethodName, buf);
            }

            Object obj = gson.fromJson(out, method.getReturnType());
            System.out.println("invoke: object: "+o+" Method: "+method+" Object[] "+os);
            System.out.println("String: "+out);
            System.out.println("Object: " + obj);

            if (method.getReturnType().equals(int.class)) {
                return 0;
            }

            return null;
        }

        return super.handleInvocation(o, method, os);
    }

}
