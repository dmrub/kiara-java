/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        ObjectType ot1 = ObjectType.get(world, String.class);
        ObjectType ot2 = ObjectType.get(world, String.class);

        ObjectType ot3 = ObjectType.get(world,
                new TypeToken<java.util.List<String>>() {});

        assertEquals(ot1, ot2);
        assertSame(ot1, ot2);

        logger.log(Level.INFO, "Name of String class: {0}", ot1.getTypeName());
        logger.log(Level.INFO, "Name of List<String> class: {0}", ot3.getTypeName());

        world.dump();
    }
}
