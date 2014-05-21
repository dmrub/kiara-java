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
public class AnyType extends Type {
    AnyType(World world) {
        super(world, "any", NodeKind.NODE_ANYTYPE, 0);
    }

    public static final AnyType get(World world) {
        return world.type_any();
    }
}
