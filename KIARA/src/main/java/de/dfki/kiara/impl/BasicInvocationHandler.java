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
import de.dfki.kiara.Connection;
import de.dfki.kiara.InterfaceMapping;
import de.dfki.kiara.MessageSerializer;
import java.lang.reflect.Method;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class BasicInvocationHandler extends AbstractInvocationHandler  {
    private final InterfaceMapping<?> interfaceMapping;
    private Class<? extends MessageSerializer> messageSerializerClass;

    public BasicInvocationHandler(InterfaceMapping<?> interfaceMapping) {
        this.interfaceMapping = interfaceMapping;
        this.messageSerializerClass = null;
    }

    public InterfaceMapping<?> getInterfaceMapping() {
        return interfaceMapping;
    }

    public Class<? extends MessageSerializer> getMessageSerializerClass() {
        return messageSerializerClass;
    }

    public void setMessageSerializerClass(Class<? extends MessageSerializer> cls) {
        messageSerializerClass = cls;
    }

    public Connection getConnection() {
        return null;
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] os) throws Throwable {
        if (method.equals(SpecialMethods.riGetConnection)) {
            return getConnection();
        }

        throw new UnsupportedOperationException("Unknown method: "+method);
    }

}
