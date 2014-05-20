/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
