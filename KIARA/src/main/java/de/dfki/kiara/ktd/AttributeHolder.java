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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class AttributeHolder {

    private final Map<String, Object> attributeDict;

    public AttributeHolder() {
        attributeDict = new HashMap<>();
    }

    public final boolean hasAttributes() {
        return !attributeDict.isEmpty();
    }

    public final void setAttributes(Map<String, Object> attrs) {
        attributeDict.clear();
        attributeDict.putAll(attrs);
    }

    public final Object findAttribute(String key) {
        return attributeDict.get(key);
    }

    public final boolean hasAttribute(String key) {
        return attributeDict.containsKey(key);
    }

    public final void removeAttribute(String key) {
        attributeDict.remove(key);
    }

    public final void setAttribute(String key, Object value) {
        attributeDict.put(key, value);
    }

    public final Object getAttribute(String key) {
        return attributeDict.get(key);
    }

    public final <T> boolean hasAttributeValue(Class<T> cls) {
        return cls.isInstance(attributeDict.get(cls.getName()));
    }

    public final <T> T getAttributeValue(Class<T> cls) {
        Object obj = attributeDict.get(cls.getName());
        if (cls.isInstance(obj))
            return cls.cast(obj);
        return null;
    }

    public final <T> void setAttributeValue(T value) {
        if (value == null)
            throw new NullPointerException();
        attributeDict.put(value.getClass().getName(), value);
    }
}
