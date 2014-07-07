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
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Lists {

    public static <T> ArrayList<T> elementList(T element) {
        ArrayList<T> l = new ArrayList<>();
        l.add(element);
        return l;
    }

    public static final <T> void resizeList(ArrayList<T> list, int newSize) {
        list.ensureCapacity(newSize);
        // trim
        while (list.size() > newSize) {
            list.remove(list.size()-1);
        }
        // fill
        while (list.size() < newSize) {
            list.add(null);
        }
    }

    public static final <T> void resizeList(List<T> list, int newSize) {
        // trim
        while (list.size() > newSize) {
            list.remove(list.size()-1);
        }
        // fill
        while (list.size() < newSize) {
            list.add(null);
        }
    }

}
