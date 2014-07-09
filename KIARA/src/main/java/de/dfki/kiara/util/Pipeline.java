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

    public static interface ExceptionHandler {

        void exceptionCaught(Throwable t);
    }

    private final List<Handler> handlers = new ArrayList<>();
    private final List<ExceptionHandler> exceptionHandlers = new ArrayList<>();

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

    public void addExceptionHandler(ExceptionHandler handler) {
        synchronized (exceptionHandlers) {
            exceptionHandlers.add(handler);
        }
    }

    public void removeExceptionHandler(ExceptionHandler handler) {
        synchronized (exceptionHandlers) {
            exceptionHandlers.remove(handler);
        }
    }

    public void processException(Throwable t) {
        synchronized (exceptionHandlers) {
            for (ExceptionHandler eh : exceptionHandlers) {
                eh.exceptionCaught(t);
            }
        }
    }

    public Object process(Object input) {
        try {
            synchronized (handlers) {
                for (Handler handler : handlers) {
                    input = handler.process(input);
                    if (input == null) {
                        break;
                    }
                }
                return input;
            }
        } catch (Exception e) {
            processException(e);
        }
        return null;
    }
}
