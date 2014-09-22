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

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class SymbolType extends CompositeType {

    public static SymbolType get(World world, String symbol) {
        return (SymbolType)world.getOrCreate(new SymbolType(world, symbol));
    }

    public final String getSymbol() {
        return (String)getElementAs(PrimValueType.class, 1).getValue();
    }

    private SymbolType(World world, String symbol) {
        super(world, "symbol", NodeKind.NODE_SYMBOLTYPE, 1, false);
        setElements(PrimValueType.get(world, symbol));
    }
}
