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
public class ServiceType extends CompositeType {

    public static ServiceType create(World world, String name, int numElements) {
            return (ServiceType)world.getOrCreate(
                    new ServiceType(world, name, numElements));
    }

    private ServiceType(World world, String name, int num) {
        super(world, name, NodeKind.NODE_SERVICETYPE, num, true);
    }
}
