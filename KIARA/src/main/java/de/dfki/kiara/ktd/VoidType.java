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
public class VoidType extends Type {
    private VoidType(World world) {
        super(world, "void", NodeKind.NODE_VOIDTYPE, 0);
    }

    public static final VoidType get(World world) {
        return world.type_void();
    }
}
