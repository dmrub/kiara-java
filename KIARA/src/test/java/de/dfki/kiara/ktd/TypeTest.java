/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.dfki.kiara.ktd;

import java.util.Arrays;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.common.collect.ImmutableList;

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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void testWorld() {
        World world = new World();

        PrimType i8 = PrimType.getBooleanType(world);
        logger.info(i8.getTypeName());

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
    }
}
