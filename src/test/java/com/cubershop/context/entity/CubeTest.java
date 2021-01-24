package com.cubershop.context.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CubeTest {

    @Test
    public void dois_cubes_devem_ser_iguais() {
        Cube cube1 = new Cube(), cube2 = new Cube();
        UUID uuid = UUID.randomUUID();

        cube1.setId(uuid);
        cube1.setImageUUID(new UUID[]{uuid});
        cube1.setName("2X2X2 QIDI COLOR");

        cube2.setId(uuid);
        cube2.setImageUUID(new UUID[]{uuid});
        cube2.setName("2X2X2 QIDI COLOR");

        assertEquals(cube1, cube2);
    }

    @Test
    public void dois_cubes_devem_ser_diferentes() {
        Cube cube1 = new Cube(), cube2 = new Cube();

        cube1.setId(UUID.randomUUID());
        cube1.setName("4x4x4 QIDI COLOR");

        cube2.setId(UUID.randomUUID());
        cube2.setName("2X2X2 QIDI COLOR");

        assertNotEquals(cube1, cube2);
    }

//    @Test
//    public void os_cubes_devem_ser_ordenaveis() {
//        Cube[] cubes = new Cube[5];
//        for(int i = 0; i < cubes.length; i++)
//            cubes[i] = new Cube();
//
//        cubes[0].setId(12);
//        cubes[0].setName("4x4x4 QIDI COLOR");
//
//        cubes[1].setId(15);
//        cubes[1].setName("2X2X2 QIDI COLOR");
//
//        cubes[3].setId(22);
//        cubes[3].setName("3x3x3 QIDI COLOR");
//
//        cubes[2].setId(36);
//        cubes[2].setName("big QIDI COLOR");
//
//        cubes[4].setId(5);
//        cubes[4].setName("5x5x5 QIDI COLOR");
//
//        assertArrayEquals(
//            new Cube[]{cubes[4], cubes[4], cubes[1], cubes[3], cubes[2]},
//            Stream.<Cube>of(cubes).sorted().toArray(Cube[]::new)
//        );
//    }
}
