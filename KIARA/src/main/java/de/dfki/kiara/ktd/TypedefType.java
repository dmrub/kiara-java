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
public class TypedefType extends Type {

    private TypedefType(String name, Type typeDeclType) {
        super(
                typeDeclType.getWorld(),
                name, NodeKind.NODE_TYPEDEFTYPE,
                Lists.elementList(typeDeclType),
                typeDeclType.getCanonicalType());
    }

    public static final TypedefType create(String name, Type typeDeclType) {
        return (TypedefType) typeDeclType.getWorld().getOrCreate(
                new TypedefType(name, typeDeclType));
    }

    public final Type getDeclType() {
        return getElementAs(Type.class, 0);
    }

}
