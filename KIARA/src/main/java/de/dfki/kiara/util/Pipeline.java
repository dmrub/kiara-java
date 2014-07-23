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
package de.dfki.kiara.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Pipeline {

    public static interface Handler {

        Object process(Object input) throws Exception;
    }

    private final List<Handler> handlers = new ArrayList<>();

    public void addHandler(Handler handler) {
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    public void removeHandler(Handler handler) {
        synchronized (handlers) {
            handlers.remove(handler);
        }
    }

    public Object process(Object input) throws Exception {
        synchronized (handlers) {
            for (Handler handler : handlers) {
                input = handler.process(input);
                if (input == null) {
                    break;
                }
            }
            return input;
        }
    }
}
