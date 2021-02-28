package com.cubershop.database.dao;

import com.cubershop.CubershopApplication;
import com.cubershop.entity.Cube;

import static org.assertj.core.api.Assertions.*;

import com.cubershop.database.template.CubeDAOTemplate;
import com.cubershop.helpers.CubeHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static java.util.stream.Collectors.*;
import java.util.stream.IntStream;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest(classes = CubershopApplication.class)
@Transactional()
@Rollback
@ActiveProfiles(profiles = "dev")
public class CubeDAOIntegrationTest {

    @Autowired
    private CubeDAOTemplate cubeDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int countCube() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cube;", Integer.class);
    }

    private int countImage() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM image;", Integer.class);
    }

    @Test
    void whenFindHomeCubesIsCalledWhereThereAreAllCubesThenReturnsAllExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);

        List<Cube> expectedCubes = List.of(
            CubeHelper.builder().name("Blue cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Yellow cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Green cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Pink cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Brown cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Purple cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Black cube").withImage().type("4x4x4").build(),
            CubeHelper.builder().name("White cube").withImage().type("4x4x4").build(),
            CubeHelper.builder().name("Red cube").withImage().type("4x4x4").build(),
            CubeHelper.builder().name("Orange cube").withImage().type("big").build()
        );
        expectedCubes.forEach(cubeDAO::saveCube);

        // when
        List<Cube> actualCubes = cubeDAO.findHomeCubes().get();

        // then
        actualCubes.forEach(System.out::println);
        assertThat(actualCubes).as("Empty check").isNotEmpty()
            .as("Null check").isNotNull()
            .as("Quantity of cubes").hasSize(9);
        assertThat(actualCubes).as("Validation of cube type")
            .filteredOn(e -> e.getType().equals("2x2x2")).hasSize(3);
        assertThat(actualCubes).as("Validation of cube type")
            .filteredOn(e -> e.getType().equals("3x3x3")).hasSize(3);
        assertThat(actualCubes).as("Validation of cube type")
            .filteredOn(e -> e.getType().equals("4x4x4")).hasSize(3);
    }

    @Test
    void whenFindHomeCubesIsCalledWhereThereIsNotAnyCubeThenReturnsAnEmptyList() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);

        // when
        List<Cube> actualCubes = cubeDAO.findHomeCubes().get();

        // then
        assertThat(actualCubes).as("Null check").isNotNull()
           .as("Empty check").isEmpty();
    }

    @Test
    void whenFindHomeCubesIsCalledWhereThereAreCubesOfOnlyOneTypeThenReturnsExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);

        List<Cube> expectedCubes = List.of(
            CubeHelper.builder().name("Blue cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Yellow cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Green cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Pink cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Red cube").withImage().type("3x3x3").build()
        );
        expectedCubes.forEach(cubeDAO::saveCube);

        // when
        List<Cube> actualCubes = cubeDAO.findHomeCubes().get();

        // then
        assertThat(actualCubes).as("Empty check").isNotEmpty()
           .as("Null check").isNotNull()
           .as("Quantity of cubes").hasSize(3);
        assertThat(actualCubes).as("Validation of cube type")
           .extracting("type", String.class).hasSize(3);
    }

    @Test
    void whenFindHomeCubesIsCalledWhereThereAreCubesOfOnlyTwoTypeReturnsExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);

        List<Cube> expectedCubes = List.of(
            CubeHelper.builder().name("Blue cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Yellow cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Green cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Pink cube").withImage().type("2x2x2").build(),
            CubeHelper.builder().name("Brown cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Purple cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("Black cube").withImage().type("3x3x3").build(),
            CubeHelper.builder().name("White cube").withImage().type("3x3x3").build()
        );
        expectedCubes.forEach(cubeDAO::saveCube);

        // when
        List<Cube> actualCubes = cubeDAO.findHomeCubes().get();

        // then
        assertThat(actualCubes).as("Empty check").isNotEmpty()
           .as("Null check").isNotNull()
           .as("Quantity of cubes").hasSize(6);
        assertThat(actualCubes).as("Validation of cube type")
           .filteredOn(e -> e.getType().equals("2x2x2")).hasSize(3);
        assertThat(actualCubes).as("Validation of cube type")
           .filteredOn(e -> e.getType().equals("3x3x3")).hasSize(3);
    }

    @Test
    void whenFindHomeCubesIsCalledWhereThereIsOnlyOneCubeThenReturnsExpectedCube() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);

        cubeDAO.saveCube(CubeHelper.builder()
           .name("Blue cube").withImage().type("3x3x3").build());

        // when
        List<Cube> actualCubes = cubeDAO.findHomeCubes().get();

        // then
        assertThat(actualCubes).as("Empty check").isNotEmpty()
           .as("Null check").isNotNull()
           .as("Quantity of cubes").hasSize(1)
           .as("Validation of cubes")
           .extracting("name", "type")
           .containsExactly(tuple("Blue cube", "3x3x3"));
    }
    @Test
    void whenSaveCubeIsCalledWithoutImageThenProceedCorrectly() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube cube = CubeHelper.builder().noImage().build();

        // when
        this.cubeDAO.saveCube(cube);

        // then
        assertThat(countCube()).as("Check cube count").isEqualTo(1);
        assertThat(countImage()).as("Check image count").isEqualTo(0);
    }

    @Test
    void whenSaveCubeIsCalledWithImageThenProceedCorrectly() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube cube = CubeHelper.builder().withImages(5).build();

        // when
        this.cubeDAO.saveCube(cube);

        // then
        assertThat(countCube()).as("Check cube count").isEqualTo(1);
        assertThat(countImage()).as("Check image count").isEqualTo(5);
    }

    @Test
    void whenFindCubesByTypeIsCalledWithNotAcceptableTypeThenReturnsAnEmptyList() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        CubeHelper.getFiveCubesWithType("3x3x3").forEach(cubeDAO::saveCube);

        // when
        String typeNotAcceptable = "9x9x9";
        List<Cube> actualCubes = cubeDAO.findCubesByType(typeNotAcceptable).get();

        // then
        assertThat(actualCubes).as("Null check").isNotNull()
           .as("Empty check").isEmpty();
    }

    @Test
    void whenFindCubesByTypeIsCalledWithAcceptableTypeThenReturnsExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        CubeHelper.getFiveCubesWithType("3x3x3").forEach(cubeDAO::saveCube);

        // when
        String typeExits = "3x3x3";
        List<Cube> actualCubes = cubeDAO.findCubesByType(typeExits).get();

        // then
        assertThat(actualCubes).as("Empty check").isNotEmpty()
           .as("Null check").isNotNull()
           .as("Quantity of cubes").hasSize(5)
           .extracting("name", String.class)
           .containsExactlyInAnyOrder("Blue cube", "Yellow cube", "Green cube", "Pink cube", "Red cube");
    }

    @Test
    void whenFindCubesByIdListIsCalledWithInvalidUUIDsThenReturnsEmptyList() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        CubeHelper.getFiveCubesWithType("3x3x3").forEach(cubeDAO::saveCube);
        UUID[] invalidUUIDs = IntStream.range(0, 5)
           .boxed().map((i) -> UUID.randomUUID()).toArray(UUID[]::new);

        // when
        List<Cube> actualCubes = this.cubeDAO.findCubesByIdList(invalidUUIDs).get();

        // then
        assertThat(actualCubes).as("Null check").isNotNull()
           .as("Empty check").isEmpty();
    }

    @Test
    void whenFindCubesByIdListIsCalledWithValidUUIDsThenReturnsExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        List<Cube> cubesToSave = CubeHelper.getFiveCubesWithType("3x3x3");
        UUID[] validUUIDs = cubesToSave.stream().map(cubeDAO::saveCube).toArray(UUID[]::new);

        // when
        List<Cube> actualCubes = this.cubeDAO.findCubesByIdList(validUUIDs).get();

        // then
        assertThat(actualCubes).as("Null check").isNotNull()
            .as("Empty check").isNotEmpty()
            .as("Size of list").hasSize(5)
            .extracting("name", String.class)
            .containsExactlyInAnyOrderElementsOf(
                cubesToSave.stream().map(Cube::getName).collect(toList())
            );
    }

    @Test
    void whenFindCubesByIdListIsCalledWithSomeInvalidUUIDsThenShouldIgnoresThemAndReturnsExpectedList() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        List<Cube> cubesToSave = CubeHelper.getFiveCubesWithType("3x3x3");
        UUID[] uuids = cubesToSave.stream().map(cubeDAO::saveCube).toArray(UUID[]::new);
        uuids[1] = UUID.randomUUID();
        uuids[3] = UUID.randomUUID();

        // when
        List<Cube> actualCubes = this.cubeDAO.findCubesByIdList(uuids).get();

        // then
        assertThat(actualCubes).as("Null check").isNotNull()
            .as("Empty check").isNotEmpty()
            .as("Size of actual list").hasSize(3)
            .as("Validation of actual list")
            .extracting("name", String.class)
            .containsExactlyInAnyOrder("Blue cube", "Green cube", "Red cube")
            .doesNotContain("Yellow cube", "Pink cube");
    }

    @Test
    void whenFindCubeByIdIsCalledWithValidIdThenReturnsExpectedCube() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube expectedCube = CubeHelper.builder().withImage().name("Blue cube").build();
        UUID validUUID = cubeDAO.saveCube(expectedCube);

        // when
        Optional<Cube> actualCube = cubeDAO.findCubeById(validUUID);

        // then
        assertThat(actualCube.get()).as("Null check").isNotNull()
            .as("Validation of the cube").extracting("id").isEqualTo(validUUID);
    }

    @Test
    void whenFindCubeByIdIsCalledWithInvalidIdThenReturnsEmptyOptional() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube expectedCube = CubeHelper.builder().withImage().name("Blue cube").build();
        UUID invalidUUID = UUID.randomUUID();
        cubeDAO.saveCube(expectedCube);

        // when
        Optional<Cube> actualCube = cubeDAO.findCubeById(invalidUUID);

        // then
        assertThat(actualCube).as("Empty check").isEmpty();
    }

    @Test
    void whenFindImageByIdIsCalledWithValidIdThenReturnsExpectedImage() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube cubeToSave = CubeHelper.builder().withImage().name("Blue cube").build();
        UUID cubeUUID = cubeDAO.saveCube(cubeToSave);
        Cube cubeReturned = cubeDAO.findCubeById(cubeUUID).get();
        UUID validImageUUID = cubeReturned.getImageUUID()[0];

        // when
        Optional<byte[]> actualImageBytes = cubeDAO.findImageById(validImageUUID);

        // then
        assertThat(actualImageBytes).as("Presence check").isPresent();
    }

    @Test
    void whenFindImageByIdIsCalledWithInvalidIdThenReturnsEmptyOptional() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        Cube cubeToSave = CubeHelper.builder().withImage().name("Blue cube").build();
        cubeDAO.saveCube(cubeToSave);

        // when
        Optional<byte[]> actualImageBytes = cubeDAO.findImageById(UUID.randomUUID());

        // then
        assertThat(actualImageBytes).as("Presence check").isNotPresent();
    }

    @Test
    void whenFindCubeByExpressionIsCalledWithAcceptableNameTheReturnsExpectedCubes() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        CubeHelper.getFiveCubes().forEach(cubeDAO::saveCube);

        // when
        Optional<List<Cube>> actualCubes = cubeDAO.findCubesByName("cube");

        // then
        assertThat(actualCubes.get()).as("Empty check").isNotEmpty()
            .as("Quantity of cubes").hasSize(5)
            .extracting("name", String.class)
            .containsExactlyInAnyOrder("Blue cube", "Yellow cube", "Green cube", "Pink cube", "Red cube");
    }

    @Test
    void whenFindCubeByExpressionIsCalledWithNotAcceptableNameTheReturnsEmptyList() {
        // given
        assertThat(countCube()).as("Initial cube count").isEqualTo(0);
        assertThat(countImage()).as("Initial image count").isEqualTo(0);

        CubeHelper.getFiveCubes().forEach(cubeDAO::saveCube);

        // when
        Optional<List<Cube>> actualCubes = cubeDAO.findCubesByName("triangle");

        // then
        assertThat(actualCubes.get()).isEmpty();
    }
}
