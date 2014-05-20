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
