package com.cubershop.service;

import com.cubershop.entity.ColorPattern;
import com.cubershop.entity.Cube;
import com.cubershop.entity.Type;
import com.cubershop.exception.CubeWithoutImageException;
import com.cubershop.helper.CubeHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CubeServiceIntegrationTest {

	@Autowired
	private CubeService cubeService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private ColorPatternService colorPatternService;

	private UUID cubeIdReference;
	private String cubeNameReference;

	private final int CUBE_COUNT = 3;
	private final int IMAGE_COUNT = 3;
	private final int IMAGE_COUNT_TOTAL = IMAGE_COUNT * IMAGE_COUNT;

	@BeforeAll
	@Commit
	void initBeforeAll() {
		List<Cube> initCubes = CubeHelper.builder().withCubes(CUBE_COUNT).withImages(IMAGE_COUNT).get();
		cubeService.saveAll(initCubes);
		cubeIdReference = initCubes.get(0).getId();
		cubeNameReference = initCubes.get(0).getName();
	}

	@AfterAll
	@Commit
	@Transactional
	void finishData() {
		this.cubeService.deleteAll();
		this.colorPatternService.deleteAll();
		this.typeService.deleteAll();
	}

	@Test
	void givenACubeWithOneImage_whenSave_thenSavedWithSuccess() {
		// given
		final int imageCount = 2;
		Cube cubeToSave = CubeHelper.getOneCube(imageCount);

		// when
		cubeService.save(cubeToSave);

		// then
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT + 1);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL + 2);
	}

	@Test
	void givenANullCube_whenSave_thenThrowsException() {
		// given
		final Cube nullCube = null;

		// when
		// then
		assertThatThrownBy(() -> this.cubeService.save(nullCube))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void givenACubeWithoutImage_whenSave_ThenThrowsException() {
		// given
		final int imageCount = 0;
		Cube cubeToSave = CubeHelper.getOneCube(imageCount);

		// when cubeService.save(cubeToSave)

		// then
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThatThrownBy(() -> cubeService.save(cubeToSave)).isInstanceOf(CubeWithoutImageException.class);
		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL);
	}

	@Test
	void givenACubeWithManyImages_whenSave_ThenSavedWithSuccess() {
		// given
		final int imageCount = 3;
		Cube cubeToSave = CubeHelper.getOneCube(imageCount);

		// when
		cubeService.save(cubeToSave);

		// then
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT + 1);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL + 3);
	}

	@Test
	void givenACubeWithExistingTypeAndExistingColorPattern_whenSave_thenSavedWithSuccess() {
		// given
		final Cube cubeToSave = CubeHelper.getOneCube(2);

		// when
		cubeService.save(cubeToSave);

		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT + 1);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL + 2);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenACubeWithNonExistingTypeAndNonExistingColorPattern_whenSave_ThenSavedWithSuccess() {
		// given
		final Type nonExistingType = new Type(Type.Name._2x2x2);
		final ColorPattern nonExistingColorPattern = new ColorPattern("Monochrome");
		final Cube cubeToSave = CubeHelper.getOneCube(2, nonExistingType, nonExistingColorPattern);

		// when
		cubeService.save(cubeToSave);

		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT + 1);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL + 2);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(2);
		assertThat(actualTypeCount).as("Type count").isEqualTo(2);
	}

	@Test
	void givenACubeWithNullTypeAndNullColorPattern_whenSave_ThenThrowsException() {
		// given
		final Type nullType = null;
		final ColorPattern nullColorPattern = null;
		final Cube cubeToSave = CubeHelper.getOneCube(2, nullType, nullColorPattern);

		// when
		assertThatThrownBy(() -> cubeService.save(cubeToSave));

		//then
		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenValidListOfCubes_whenSaveAll_thenAllSavedWithSuccess() {
		// given
		final int cubeCountToSave = 5;
		final List<Cube> cubeListToSave = CubeHelper
	  		.builder()
			.withCubes(cubeCountToSave)
			.withImages(2).get();

		// when
		this.cubeService.saveAll(cubeListToSave);

		// then
		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT + 5);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL + 10);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenEmptyListOfCubes_whenSaveAll_thenNothingSaved() {
		// given
		List<Cube> emptyListOfCubes = Collections.emptyList();

		// when
		this.cubeService.saveAll(emptyListOfCubes);

		// then
		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenNullListOfCubes_whenSaveAll_thenThrowsException() {
		// given
		List<Cube> nullListOfCubes = null;

		// when
		// then
		assertThatThrownBy(() -> this.cubeService.saveAll(nullListOfCubes))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void givenListOfCubesWithANullCube_whenSaveAll_thenThrowsException() {
		// given
		final List<Cube> listOfCubesWithANullCube = CubeHelper
			.builder()
			.withCubes(4)
			.withImages(2).get();

		listOfCubesWithANullCube.set(1, null);

		// when
		// then
		assertThatThrownBy(() -> this.cubeService.saveAll(listOfCubesWithANullCube))
			.isInstanceOf(IllegalArgumentException.class);

		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenAExistingId_whenDeleteById_thenDeletedWithSuccess() {
		// given
		final UUID existingId = cubeIdReference;

		// when
		this.cubeService.deleteById(existingId);

		// then
		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT - 1);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL - 3);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenANonExistingId_whenDeleteById_thenNothingDeleted() {
		// given
		final UUID existingId = UUID.randomUUID();

		// when
		// then

		assertThatThrownBy(() -> this.cubeService.deleteById(existingId))
				.isInstanceOf(EmptyResultDataAccessException.class);

		final long actualTypeCount = typeService.count();
		final long actualColorPatternCount = colorPatternService.count();
		final long actualCubeCount = cubeService.count();
		final long actualImageCount = imageService.count();

		assertThat(actualCubeCount).as("Cube count").isEqualTo(CUBE_COUNT);
		assertThat(actualImageCount).as("Image count").isEqualTo(IMAGE_COUNT_TOTAL);
		assertThat(actualColorPatternCount).as("ColorPattern count").isEqualTo(1);
		assertThat(actualTypeCount).as("Type count").isEqualTo(1);
	}

	@Test
	void givenNullId_whenDeleteById_thenThrowsException() {
		// given
		final UUID nullId = null;

		// when
		// then
		assertThatThrownBy(() -> this.cubeService.deleteById(nullId))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	void givenExistingName_whenFindAllByName_thenReturnedWithSuccess() {
		//given
		final String existingName = cubeNameReference;

		// when
		final List<Cube> foundCubes = cubeService.findAllByName(existingName);

		// then
		assertThat(foundCubes).isNotNull();
		assertThat(foundCubes).hasSize(1);
		assertThat(foundCubes).allSatisfy(cube -> {
			assertThat(cube).isNotNull();
			assertThat(cube.getImages()).as("Images").isNotEmpty().as("Images count").hasSize(IMAGE_COUNT);
		});
		assertThat(foundCubes.get(0).getImages()).as("Images count").hasSize(IMAGE_COUNT);
		assertThat(foundCubes).extracting("type").isNotNull();
		assertThat(foundCubes).extracting("colorPattern").isNotNull();
	}

	@Test
	void givenNonExistingName_whenFindByName_thenNothingReturned() {
		//given
		final String nonExistingName = "abc";

		// when
		final List<Cube> foundCubes = cubeService.findAllByName(nonExistingName);

		// then
		assertThat(foundCubes).isEmpty();
	}

	@Test
	void givenNullName_whenFindAllByName_thenExceptionThrown() {
		//given
		final String nullName = null;

		// when
		// then
		assertThatThrownBy(() -> cubeService.findAllByName(nullName));
	}

	@Test
	void givenExistingId_whenFindById_thenReturnsACube() {
		//given
		final UUID existingId = cubeIdReference;

		// when
		final Cube cubeFetched = cubeService.findById(existingId).orElse(null);

		// then
		assertThat(cubeFetched).isNotNull();
		assertThat(cubeFetched.getImages()).isNotNull();
		assertThat(cubeFetched.getImages()).as("Count of Images").hasSize(3);
		assertThat(cubeFetched.getType()).isNotNull();
		assertThat(cubeFetched.getColorPattern()).isNotNull();
	}

	@Test
	void givenNonExistingId_whenFindById_thenReturnsNothing() {
		// given
		final UUID nonExistingId = UUID.randomUUID();

		// when
		final Cube cubeFetched = cubeService.findById(nonExistingId).orElse(null);

		// then
		assertThat(cubeFetched).isNull();
	}

	@Test
	void givenNullId_whenFindById_thenThrowsException() {
		// given
		final UUID nullId = null;

		// when
		final Cube cubeFetched = cubeService.findById(nullId).orElse(null);

		// then
		assertThat(cubeFetched).isNull();
	}

	@Test
	void whenFindAll_thenReturnsAllTheCubes() {
		// given
		// when
		final Iterable<Cube> cubesFetched = this.cubeService.findAll();

		// then
		assertThat(cubesFetched)
			.isNotNull()
			.isNotEmpty()
			.as("Size of list of cubes").hasSize(3)
			.doesNotContainNull();
	}

}
