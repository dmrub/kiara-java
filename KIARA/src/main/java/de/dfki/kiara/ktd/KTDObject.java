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
public class KTDObject {
    private final World world;

    public KTDObject(World world) {
        this.world = world;
    }

    public final World getWorld() {
        return world;
    }

}
