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

package de.dfki.kiara.test;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.InterfaceGenerator;
import de.dfki.kiara.MethodBinder;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MessageSerializer;
import de.dfki.kiara.RemoteInterface;
import java.lang.reflect.Method;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class CalcTest {

    public static interface Calc {
        public int add(int a, int b);
        public float addFloat(float a, float b);
        public int stringToInt32(String s);
        public String int32ToString(int i);
    }

    public static void main(String[] args) throws Exception {
        try (Context context = Kiara.createContext();
             Connection connection = context.openConnection("http://localhost:8080/service")) {

            MethodBinder<Calc> b = new MethodBinder<>(Calc.class).bind("calc.add", "add");

            Calc calc = connection.generateClientFunctions(b);
            RemoteInterface ri = (RemoteInterface)calc;
            Class<? extends MessageSerializer> cls = ri.getMessageSerializerClass();

            calc.add(10, 12);
        }
    }
}
