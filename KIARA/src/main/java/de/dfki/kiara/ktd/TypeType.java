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
public class TypeType extends Type {
    TypeType(World world) {
        super(world, "type", NodeKind.NODE_TYPETYPE, 0);
    }

    public static final TypeType get(World world) {
        return world.type_type();
    }
}
