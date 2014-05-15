/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Type extends KTDObject {
    private String name;
    private int kind;
    private Namespace namespace;
    private Type canonicalType;
    protected ArrayList<Object> elements;

    @SuppressWarnings("LeakingThisInConstructor")
    protected Type(World world, String name, int kind, int numElems) {
        this(world, name, kind, new ArrayList<>(numElems), null);
        this.canonicalType = this;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected Type(World world, String name, int kind, ArrayList<Object> elems) {
        this(world, name, kind, elems, null);
        this.canonicalType = this;
    }

    protected Type(World world, String name, int kind, int numElems,
            Type canonicalType) {
        this(world, name, kind, new ArrayList<Object>(numElems), canonicalType);
    }

    protected Type(World world, String name, int kind, ArrayList<Object> elems,
            Type canonicalType) {
        super(world);
        this.elements = elems;
        this.kind = kind;
        this.namespace = null;
        this.canonicalType = canonicalType;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public String getTypeName() {
        return name;
    }

    public String getFullTypeName() {
        if (namespace != null)
            return namespace.getFullName() + "." + getTypeName();
        else
            return getTypeName();
    }

    public final int getKind() {
        return kind;
    }

    public final ArrayList<Object> getElements() {
        return this.elements;
    }

    public final int getNumElements() {
        return this.elements.size();
    }

    public final <T> T getAs(Class<T> cls) {
        if (cls.isInstance(this))
            return cls.cast(this);
        Type ty = getCanonicalType();
        if (cls.isInstance(ty))
            return cls.cast(ty);
        return null;
    }

    public final <T> T getElementAs(Class<T> cls, int index) {
        return cls.cast(this.elements.get(index));
    }

    public final <T> T getSafeElementAs(Class<T> cls, int index) {
        Object element = this.elements.get(index);
        if (cls.isInstance(element))
            return cls.cast(element);
        return null;
    }

    public final Type getCanonicalType() {
        return canonicalType;
    }

    public final boolean isCanonicalType() {
        return this == canonicalType;
    }

    protected final void resizeElements(int newSize) {
        this.elements.ensureCapacity(newSize);
        // trim
        while (this.elements.size() > newSize) {
            this.elements.remove(this.elements.size()-1);
        }
        // fill
        while (this.elements.size() < newSize) {
            this.elements.add(null);
        }
    }

    protected void setCanonicalTypeUnsafe(Type type) {
        this.canonicalType = type;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Type))
            return false;
        Type otherTy = (Type)other;
        if (getKind() != otherTy.getKind())
            return false;
        if (elements.size() != otherTy.elements.size())
            return false;
        for (int i = 0; i < elements.size(); ++i) {
            if (elements.get(i) == otherTy.elements.get(i))
                continue;
            if (elements.get(i) != null && elements.equals(otherTy.elements.get(i)))
                continue;
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), elements);
    }
}
