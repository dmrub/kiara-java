/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */

package de.dfki.kiara.ktd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Annotation extends KTDObject {
    private StructType annotationType;
    private final List<Object> values;

    public Annotation(World world) {
        super(world);
        this.annotationType = null;
        this.values = new ArrayList<>();
    }

    public Annotation(StructType annotationType) {
        super(annotationType.getWorld());
        this.annotationType = annotationType;
        this.values = new ArrayList<>();
    }

    public final StructType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(StructType annotationType) {
        this.annotationType = annotationType;
    }

    public final List<Object> getValues() {
        return values;
    }

    public final void setValues(List<Object> values) {
        if (values != this.values) {
            this.values.clear();
            this.values.addAll(values);
        }
    }

    public static Annotation getFirstAnnotationOfType(
            Collection<Annotation> annotationList, StructType type) {
        for (Annotation a : annotationList) {
            if (a.getAnnotationType() == type)
                return a;
        }
        return null;
    }

}
