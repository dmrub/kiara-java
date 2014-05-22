/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.kiara.ktd;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Type extends KTDObject implements ParameterInfo {

    private String name;
    private int kind;
    private Namespace namespace;
    private Type canonicalType;
    protected ArrayList<KTDObject> elements;

    @SuppressWarnings("LeakingThisInConstructor")
    protected Type(World world, String name, int kind, int numElems) {
        this(world, name, kind, numElems, null);
        this.canonicalType = this;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected Type(World world, String name, int kind,
            List<? extends KTDObject> elems) {
        this(world, name, kind, elems, null);
        this.canonicalType = this;
    }

    protected Type(World world, String name, int kind, int numElems,
            Type canonicalType) {
        super(world);
        this.name = name;
        this.kind = kind;
        this.namespace = null;
        this.canonicalType = canonicalType;
        this.elements = new ArrayList<>(numElems);
        Lists.resizeList(this.elements, numElems);
    }

    protected Type(World world, String name, int kind,
            List<? extends KTDObject> elems, Type canonicalType) {
        super(world);
        this.name = name;
        this.kind = kind;
        this.namespace = null;
        this.canonicalType = canonicalType;
        this.elements = new ArrayList<>(elems);
    }

    @Override public String getParameterName() {
        return null;
    }

    @Override public Type getParameterType() {
        return this;
    }

    public final Namespace getNamespace() {
        return namespace;
    }

    public final void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public final String getTypeName() {
        return name;
    }

    public String getFullTypeName() {
        if (namespace != null) {
            return namespace.getFullName() + "." + getTypeName();
        } else {
            return getTypeName();
        }
    }

    public final int getKind() {
        return kind;
    }

    public final ArrayList<KTDObject> getElements() {
        return this.elements;
    }

    public final int getNumElements() {
        return this.elements.size();
    }

    public final <T> T getAs(Class<T> cls) {
        if (cls.isInstance(this)) {
            return cls.cast(this);
        }
        Type ty = getCanonicalType();
        if (cls.isInstance(ty)) {
            return cls.cast(ty);
        }
        return null;
    }

    public final <T> T getElementAs(Class<T> cls, int index) {
        return cls.cast(this.elements.get(index));
    }

    public final <T> T getSafeElementAs(Class<T> cls, int index) {
        Object element = this.elements.get(index);
        if (cls.isInstance(element)) {
            return cls.cast(element);
        }
        return null;
    }

    public final Type getCanonicalType() {
        return canonicalType;
    }

    public final boolean isCanonicalType() {
        return this == canonicalType;
    }

    protected void resizeElements(int newSize) {
        Lists.resizeList(this.elements, newSize);
    }

    protected void setCanonicalTypeUnsafe(Type type) {
        this.canonicalType = type;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Type)) {
            return false;
        }
        Type otherTy = (Type) other;
        if (getKind() != otherTy.getKind()) {
            return false;
        }
        if (elements.size() != otherTy.elements.size()) {
            return false;
        }
        for (int i = 0; i < elements.size(); ++i) {
            KTDObject thisEl = elements.get(i);
            KTDObject thatEl = otherTy.elements.get(i);

            if (thisEl == thatEl) {
                continue;
            }

            if (thisEl != null && thisEl.equals(thatEl)) {
                continue;
            }

            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), elements);
    }
}
