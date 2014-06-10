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

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.gson.Gson;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class SerializationInvocationHandler extends AbstractInvocationHandler {

    private final Gson gson = new Gson();
    private final Map<String, Method> boundMethods;

    public SerializationInvocationHandler(Map<String, Method> boundMethods) {
        this.boundMethods = boundMethods;
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        String out = gson.toJson(os[0]);
        Object obj = gson.fromJson(out, method.getReturnType());
        System.out.println("invoke: object: "+o+" Method: "+method+" Object[] "+os);
        System.out.println("String: "+out);
        System.out.println("Object: " + obj);
        return null;
    }

}
