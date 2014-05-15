/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Namespace extends KTDObject {
    private final String name;
    private Namespace parent;
    private final Map<String, Type> typeMap;

    private Namespace(World world, String name) {
        super(world);
        this.name = name;
        this.parent = null;
        this.typeMap = new HashMap<>();
    }

    public static final Namespace create(World world, String name) {
        return new Namespace(world, name);
    }

    public final String getName() {
        return name;
    }

    public final String getFullName() {
        StringBuilder fullName = new StringBuilder();
        Namespace ns = parent;
        List<String> names = new ArrayList<>();
        while (ns != null) {
            names.add(ns.getName());
            ns = ns.getParent();
        }
        for (String n : names) {
            fullName.append(n);
            fullName.append('.');
        }
        fullName.append(getName());
        return fullName.toString();
    }

    public final Namespace getParent() {
        return parent;
    }

    public final void setParent(Namespace parent) {
        this.parent = parent;
    }

    public final void bindType(String name, Type type) {
        bindType(name, type, true);
    }

    public final void bindType(String name, Type type, boolean takeOwnership) {
        if (type == null)
            throw new NullPointerException("type cannot be null");
        if (type.getWorld() != this.getWorld())
            throw new IllegalArgumentException("type's world is invalid");

        if (!typeMap.containsKey(name))
            throw new IllegalStateException("Type '"+name+"' already defined.");
        typeMap.put(name, type);
        if (takeOwnership) {
            type.setNamespace(this);
        }
    }

    public final Type lookupType(String name) {
        return typeMap.get(name);
    }

    public final String getTypeName(Type type) {
        for (Map.Entry<String, Type> e : typeMap.entrySet()) {
            if (e.getValue() == type)
                return e.getKey();
        }
        return "";
    }
}
