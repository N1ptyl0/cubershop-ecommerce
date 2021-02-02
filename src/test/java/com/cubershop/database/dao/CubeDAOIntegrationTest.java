package com.cubershop.database.dao;

import com.cubershop.CubershopApplication;
import com.cubershop.context.entity.Cube;
import static org.junit.jupiter.api.Assertions.*;

import com.cubershop.database.base.CubeDAOBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest(classes = CubershopApplication.class)
@Transactional
@Rollback
@ActiveProfiles(profiles = "dev")
public class CubeDAOIntegrationTest {

    @Autowired
    private CubeDAOBase cubeDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void findHomeCubes_test() {
        List<Cube> cubes = this.cubeDAO.findHomeCubes();

        assertEquals(12, cubes.size(), "Sizes of cubes list");
        assertEquals(3, cubes.stream().filter(e -> e.getType().equals("2x2x2")).count(),
            "Presence of 3 cubes of type '2x2x2'"
        );
        assertEquals(3, cubes.stream().filter(e -> e.getType().equals("3x3x3")).count(),
                "Presence of 3 cubes of type '3x3x3'"
        );
        assertEquals(3, cubes.stream().filter(e -> e.getType().equals("4x4x4")).count(),
                "Presence of 3 cubes of type '4x4x4'"
        );
        assertEquals(3, cubes.stream().filter(e -> e.getType().equals("5x5x5")).count(),
                "Presence of 3 cubes of type '5x5x5'"
        );
    }

    @Test
    public void saveCube_without_images() {
        assertEquals(12, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "cube"),
            "Quantity of cubes in the cube table"
        );
        assertEquals(12, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "image"),
            "Quantity of images in the image table"
        );

        Cube cube = new Cube();

        this.cubeDAO.saveCube(cube);

        assertEquals(13, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "cube"),
            "Quantity of cubes in the cube table after insertion"
        );
        assertEquals(12, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "image"),
            "Quantity of images in the image table after insertion"
        );
    }

    @Test
    public void saveCube_with_images() {
        assertEquals(12, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "cube"),
            "Quantity of cubes in the cube table"
        );
        assertEquals(12, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "image"),
            "Quantity of images in the image table"
        );

        Cube cube = new Cube();
        cube.setImageFile(
            IntStream.range(0, 5).boxed().map(e -> new MockMultipartFile("noname", new byte[]{}))
            .collect(Collectors.toList())
        );

        this.cubeDAO.saveCube(cube);

        assertEquals(13, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "cube"),
            "Quantity of cubes in the cube table after insertion"
        );
        assertEquals(17, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "image"),
            "Quantity of images in the image table after insertion"
        );
    }

    @Test
    public void findCubesByTypeAndOrder_order_with_alpha_desc() {
        String type = "3x3x3", order = "alpha_desc";

        List<Cube> cubes = this.cubeDAO.findCubesByTypeAndOrder(type, order);

        assertTrue(cubes.stream().allMatch(o -> o.getType().equals(type)), "Type of the cubes");
        assertArrayEquals(cubes.toArray(Cube[]::new),
            cubes.stream()
                .sorted((a, b) -> b.getName().compareTo(a.getName()))
                .toArray(Cube[]::new),
            "Desc order by name of the cubes"
        );

    }

    @Test
    public void findCubesByTypeAndOrder_order_with_alpha_asc() {
        String type = "3x3x3", order = "alpha_asc";

        List<Cube> cubes = this.cubeDAO.findCubesByTypeAndOrder(type, order);

        assertTrue(cubes.stream().allMatch(o -> o.getType().equals(type)), "Type of the cubes");
        assertArrayEquals(cubes.toArray(Cube[]::new),
            cubes.stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .toArray(Cube[]::new),
            "Asc order by name of the cubes"
        );

    }

    @Test
    public void findCubesByTypeAndOrder_order_with_price_desc() {
        String type = "3x3x3", order = "price_desc";

        List<Cube> cubes = this.cubeDAO.findCubesByTypeAndOrder(type, order);

        assertTrue(cubes.stream().allMatch(o -> o.getType().equals(type)), "Type of the cubes");
        assertArrayEquals(cubes.toArray(Cube[]::new),
            cubes.stream()
                .sorted((a, b) -> b.getPrice().compareTo(a.getPrice()))
                .toArray(Cube[]::new),
        "Desc order by price of the cubes"
        );
    }

    @Test
    public void findCubesByTypeAndOrder_order_with_price_asc() {
        String type = "3x3x3", order = "price_asc";

        List<Cube> cubes = this.cubeDAO.findCubesByTypeAndOrder(type, order);

        assertTrue(cubes.stream().allMatch(o -> o.getType().equals(type)), "Type of the cubes");
        assertArrayEquals(cubes.toArray(Cube[]::new),
        cubes.stream()
            .sorted((a, b) -> a.getPrice().compareTo(b.getPrice()))
            .toArray(Cube[]::new),
    "Desc order by price of the cubes"
        );
    }

    @Test
    public void findCubeById_must_not_be_null() {
        Cube cube = this.cubeDAO.findCubeById(UUID.fromString("4cecfde1-f483-417e-8b53-9f67d6698b8b"));

        Assertions.assertThat(cube).isNotNull();
    }

    @Test
    public void findImageById_must_not_be_null() {
        byte[] bytes = this.cubeDAO.findImageById(UUID.fromString("84437a46-f737-4e48-ab2f-7bb52a16270e"));

        Assertions.assertThat(bytes).isNotNull();
    }

    @Test
    public void findNamesByExpression_must_not_be_null() {
        List<String> result = this.cubeDAO.findNamesByExpression("warrior");

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void findCubesByExpressionAndOrder_must_not_be_null() {
        List<Cube> result = this.cubeDAO.findCubesByExpressionAndOrder("warrior", "price_desc");

        Assertions.assertThat(result).isNotNull();
    }
}
