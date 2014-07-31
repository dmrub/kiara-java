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

package de.dfki.kiara;

import java.lang.reflect.Method;

/**
 *
 * @author Shahzad, Dmitri Rubinstein
 */
public class ServiceMethodBinder {
    private final Object implementedClass;
    private final Method boundMethod;
    private final MethodEntry methodEntry;

    public ServiceMethodBinder(Object implementedClass, Method boundMethod) {
        this.implementedClass = implementedClass;
        this.boundMethod = boundMethod;
        this.methodEntry = new MethodEntry(boundMethod);
    }

    public Object getImplementedClass(){
        return implementedClass;
    }

    public Method getBoundMethod(){
        return boundMethod;
    }

    public MethodEntry getMethodEntry() {
        return methodEntry;
    }
}
