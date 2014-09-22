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
package de.dfki.kiara.ktd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class FunctionType extends CompositeType {

    public static class Parameter implements ParameterInfo {
        private final String paramName;
        private final Type paramType;

        public Parameter(String paramName, Type paramType) {
            this.paramName = paramName;
            this.paramType = paramType;
        }

        @Override
        public Type getParameterType() {
            return paramType;
        }

        @Override public String getParameterName() {
            return paramName;
        }
    }

    // Create unique function type
    public static FunctionType create(String name, Type returnType,
            List<ParameterInfo> paramTypes) {
        World world = returnType.getWorld();
        FunctionType ty = new FunctionType(world, name, returnType, paramTypes, true);
        ty.init();
        return (FunctionType)world.getOrCreate(ty);
    }

    // Get unnamed function type (non-unique)
    public static FunctionType get(Type returnType,
            List<ParameterInfo> paramTypes) {
        World world = returnType.getWorld();
        FunctionType ty = new FunctionType(world, "", returnType, paramTypes, false);
        ty.init();
        return (FunctionType)world.getOrCreate(ty);
    }

    public final Type getReturnType() {
        return getElementAs(Type.class, 0);
    }

    public final ElementData getReturnElementData() {
        return getElementDataAt(0);
    }

    public final int getNumParams() { return getNumElements()-1; }

    public final Type getParamType(int index) {
        return getElementAt(index+1);
    }

    public final String getParamName(int index) {
        return getElementDataAt(index+1).getName();
    }

    public final ElementData getParamElementDataAt(int index) {
        return getElementDataAt(index+1);
    }

    private FunctionType(
            World world, String name,
            Type returnType, List<ParameterInfo> paramTypes,
            boolean unique) {
        super(world, name, NodeKind.NODE_FUNCTYPE, paramTypes.size()+1, unique);
        setElementAtUnsafe(0, returnType);

        for (int i = 0; i < paramTypes.size(); ++i)
        {
            setElementAtUnsafe(i+1, paramTypes.get(i).getParameterType());
            String paramName = paramTypes.get(i).getParameterName();
            if (paramName != null)
                setElementNameAt(i+1, paramName);
        }
    }

    void init() {
        //if (!getElementType()->isCanonicalType())
        //    setCanonicalTypeUnsafe(PtrType::get(getElementType()->getCanonicalType()));
        boolean isCanonical = true;
        int numElements = getNumElements();
        for (int i = 0; i < numElements; ++i) {
            if (!getElementAt(i).isCanonicalType()) {
                isCanonical = false;
                break;
            }
        }

        if (isCanonical)
            return;

        int numParams = getNumParams();
        List<ParameterInfo> paramTypes = new ArrayList<>(numParams);
        for (int i = 0; i < numParams; ++i) {
            paramTypes.set(i, new Parameter(getParamName(i), getParamType(i).getCanonicalType()));
        }

        FunctionType cty = (isUnique() ?
            FunctionType.create(getTypeName()+"_CANONICAL", getReturnType().getCanonicalType(), paramTypes) :
            FunctionType.get(getReturnType().getCanonicalType(), paramTypes));

        setCanonicalTypeUnsafe(cty);
    }

}
