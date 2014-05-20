/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
