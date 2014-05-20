/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class CompoundType extends Type {

    protected CompoundType(World world, String name, int kind, int num) {
        super(world, name, kind, num);
    }

    protected CompoundType(World world, String name, int kind,
            List<? extends KTDObject> elems) {
        super(world, name, kind, elems);
    }
}
