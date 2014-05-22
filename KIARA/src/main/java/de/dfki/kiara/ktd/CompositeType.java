/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.ktd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class CompositeType extends CompoundType {

    public static final int npos = -1;
    private final boolean unique;
    private final List<ElementData> elementDataList;

    public final boolean isUnique() { return unique; }

    public final boolean isOpaque() { return unique && getNumElements() == 0; }

    public final void makeOpaque() {
        resizeElements(0);
    }

    private void resizeElementDataList(int newSize) {
        // trim
        while (elementDataList.size() > newSize) {
            elementDataList.remove(elementDataList.size()-1);
        }
        // fill
        while (elementDataList.size() < newSize) {
            elementDataList.add(new ElementData());
        }
    }

    @Override public void resizeElements(int newSize) {
        if (!isUnique())
            throw new IllegalStateException("Structure is not unique");
        Lists.resizeList(this.elements, newSize);
        resizeElementDataList(newSize);
    }

    public final void setElements(Type ... elements) {
        if (elements.length != this.elements.size())
            throw new IllegalArgumentException("Invalid size of the elements list");
        for (int i = 0; i < elements.length; ++i) {
            this.elements.set(i, elements[i]);
        }
    }

    public final void setElements(List<Type> elements) {
        if (elements.size() != this.elements.size())
            throw new IllegalArgumentException("Invalid size of the elements list");
        Collections.copy(this.elements, elements);
    }

    /** returns npos if no element with specified name found.
     * @param name
     * @return
     */
    public final int getElementIndexByName(String name) {
        for (int i = 0; i < getNumElements(); ++i) {
            if (getElementNameAt(i).equals(name))
                return i;
        }
        return npos;
    }

    public final Type getElementAt(int index) {
        return getElementAs(Type.class, index);
    }

    /** Throws exception when structure is not unique
     * @param index
     * @param element
     */
    public final void setElementAt(int index, Type element) {
        if (!isUnique())
            throw new IllegalStateException("Structure is not unique");
        elements.set(index, element);
    }

    public final ElementData getElementDataAt(int index) {
        return elementDataList.get(index);
    }

    public final void setElementNames(String ... names) {
        Lists.resizeList(elementDataList, names.length);
        for (int i = 0; i < names.length; ++i) {
            elementDataList.get(i).setName(names[i]);
        }
    }

    public final void setElementNames(List<String> names) {
        Lists.resizeList(elementDataList, names.size());
        for (int i = 0; i < names.size(); ++i) {
            elementDataList.get(i).setName(names.get(i));
        }
    }

    public final List<String> getElementNames() {
        List<String> names = new ArrayList<>();
        for (ElementData e : elementDataList) {
            names.add(e.getName());
        }
        return names;
    }

    public final String getElementNameAt(int index) {
        return elementDataList.get(index).getName();
    }

    public final void setElementNameAt(int index, String name) {
        elementDataList.get(index).setName(name);
    }

    @Override public int hashCode() {
        if (unique)
            return System.identityHashCode(this);
        else
            return super.hashCode();
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other) {
        if (unique)
            return this == other;
        else
            return super.equals(other);
    }

    protected CompositeType(World world, String name, int kind, int num, boolean unique) {
        super(world, name, kind, num);
        this.unique = unique;
        this.elementDataList = new ArrayList<>(num);
        resizeElementDataList(num);
    }

    protected CompositeType(World world, String name, int kind,
            List<? extends KTDObject> elems, boolean unique) {
        this(world, name, kind, elems, null, unique);
    }

    protected CompositeType(World world, String name, int kind,
            List<? extends KTDObject> elems, List<String> names, boolean unique) {
        super(world, name, kind, elems);
        this.unique = unique;
        this.elementDataList = new ArrayList<>(elems.size());
        resizeElementDataList(elems.size());
        if (names != null)
            setElementNames(names);
    }

    protected final void setElementAtUnsafe(int index, Type element) {
        elements.set(index, element);
    }
}
