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

import com.google.common.reflect.TypeToken;
import java.util.Objects;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class ObjectType extends Type {
    private final TypeToken typeToken;

    public static ObjectType get(World world, Class<?> clazz) {
        return get(world, TypeToken.of(clazz));
    }

    public static ObjectType get(World world, TypeToken typeToken) {
        return (ObjectType)world.getOrCreate(new ObjectType(world, typeToken));
    }

    public final TypeToken getTypeToken() {
        return typeToken;
    }

    private ObjectType(World world, TypeToken typeToken) {
        super(world, typeToken.toString(), NodeKind.NODE_OBJECTTYPE, 0);
        this.typeToken = typeToken;
    }

    @Override
    public int hashCode() {
        return this.typeToken.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectType other = (ObjectType) obj;
        return Objects.equals(this.typeToken, other.typeToken);
    }

}
