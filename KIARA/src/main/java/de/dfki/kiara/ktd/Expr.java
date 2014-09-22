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
public class Expr extends KTDObject {
    private Type exprType;

    protected Expr(Type exprType) {
        this(exprType, exprType.getWorld());
    }

    protected Expr(Type exprType, World world) {
        super(world);
        this.exprType = exprType;
    }

    /** replaceExpr returns true if replacement was successfully performed
     * @param oldExpr
     * @param newExpr
     * @return
     */
    public boolean replaceExpr(Object oldExpr, Object newExpr) {
        return false;
    }

    public Type getExprType() {
        return exprType;
    }

    protected final void setExprType(Type type) {
        exprType = type;
    }
}
