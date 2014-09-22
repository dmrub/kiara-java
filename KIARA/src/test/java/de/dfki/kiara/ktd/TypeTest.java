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

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TypeTest {

    static final Logger logger = Logger.getLogger(TypeTest.class.getName());

    public TypeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        logger.info("Setup TypeTest");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWorld() {
        World world = new World();

        assertEquals(world.type_i8().getTypeName(), "i8");
        assertEquals(world.type_u8().getTypeName(), "u8");
        assertEquals(PrimType.getBooleanType(world).getTypeName(), "boolean");

        StructType t1 = StructType.get(
                world, "T1",
                ImmutableList.<Type>of(world.type_i8()),
                ImmutableList.<String>of("m"));

        StructType t2 = StructType.get(
                world, "T1",
                ImmutableList.<Type>of(world.type_i8()),
                ImmutableList.<String>of("m"));

        StructType t3 = StructType.get(
                world, "T3",
                ImmutableList.<Type>of(world.type_i16()),
                ImmutableList.<String>of("s"));

        assertEquals(t1, t2);
        assertSame(t1, t2);
        assertNotSame(t1, t3);
        assertNotSame(t2, t3);

        assertSame(t1, t1.getCanonicalType());
        assertEquals(t1, t1.getCanonicalType());

        ObjectType ot1 = ObjectType.get(world, String.class);
        ObjectType ot2 = ObjectType.get(world, String.class);

        ObjectType ot3 = ObjectType.get(world,
                new TypeToken<java.util.List<String>>() {});

        assertEquals(ot1, ot2);
        assertSame(ot1, ot2);

        TypedefType tt1 = TypedefType.create("TT1", ot1);
        TypedefType tt2 = TypedefType.create("TT2", ot2);

        assertFalse(tt1.equals(tt2));
        assertNotSame(tt1, tt2);
        assertEquals(tt1.getCanonicalType(), tt2.getCanonicalType());
        assertSame(tt1.getCanonicalType(), tt2.getCanonicalType());

        logger.log(Level.INFO, "Name of String class: {0}", ot1.getTypeName());
        logger.log(Level.INFO, "Name of List<String> class: {0}", ot3.getTypeName());

        world.dump();
    }
}
