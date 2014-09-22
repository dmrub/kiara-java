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

package de.dfki.kiara.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.AbstractInvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TestInvocationHandler extends AbstractInvocationHandler {

    public TestInvocationHandler() {
    }

//    @Override
//    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
//        System.out.println("invoke: object: "+o+" Method: "+method+" Object[] "+os);
//        return null;
//    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String out = mapper.writeValueAsString(os[0]);
        Object obj = mapper.readValue(out, method.getReturnType());
        System.out.println("invoke: object: "+o+" Method: "+method+" Object[] "+Arrays.toString(os));
        System.out.println("String: "+out);
        System.out.println("Object: " + obj);
        return null;
    }

}
