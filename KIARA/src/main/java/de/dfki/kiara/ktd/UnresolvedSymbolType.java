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
public class UnresolvedSymbolType extends Type {
    private UnresolvedSymbolType(World world) {
        super(world, "unresolved_symbol", NodeKind.NODE_UNRESOLVEDSYMBOLTYPE, 0);
    }

    public static final UnresolvedSymbolType get(World world) {
        return world.type_unresolved_symbol();
    }
}
