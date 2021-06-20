package com.cubershop.service;

import com.cubershop.entity.ColorPattern;
import com.cubershop.entity.Cube;
import com.cubershop.helper.CubeHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ColorPatternServiceIntegrationTest {

	@Autowired
	private ColorPatternService colorPatternService;

	@Autowired
	private CubeService cubeService;

	@BeforeAll
	@Commit
	void initBeforeAll() {
		ColorPattern monochrome = new ColorPattern("monochrome");
		ColorPattern traditional = new ColorPattern("traditional");

		List<Cube> cubesWithMonochrome = CubeHelper
			.builder()
			.withCubes(3)
			.withImages(2).withColorPattern(monochrome).get();

		Cube cubeWithTraditional = CubeHelper.getOneCube(2);
		cubeWithTraditional.setColorPattern(traditional);

		this.cubeService.saveAll(cubesWithMonochrome);
		this.cubeService.save(cubeWithTraditional);
	}

	@AfterAll
	@Commit
	@Transactional
	void finishData() {
		this.cubeService.deleteAll();
		this.colorPatternService.deleteAll();
	}

	@Test
	void whenFindAll_thenReturnsAllWithSuccess() {
		// given
		// when
		List<ColorPattern> patternsFetched = (List<ColorPattern>) this.colorPatternService.findAll();

		// then
		assertThat(patternsFetched).isNotNull().isNotEmpty()
			.as("Cubes count").hasSize(2);
	}

	@Test
	void givenExistingName_whenFindByName_thenReturnedWithSuccess() {
		// given
		final String existingName = "traditional";

		// when
		final ColorPattern colorPatternFetched =
			this.colorPatternService.findByName(existingName).orElse(null);

		// then
		assertThat(colorPatternFetched).isNotNull();
	}

	@Test
	void givenNonExistingName_whenFindByName_thenNothingReturned() {
		// given
		final String nonExistingName = "red";

		// when
		final ColorPattern colorPatternFetched =
			this.colorPatternService.findByName(nonExistingName).orElse(null);

		// then
		assertThat(colorPatternFetched).isNull();
	}

	@Test
	void givenNullName_whenFindByName_thenThrowsException() {
		// given
		final String nullName = null;

		// when
		// then
		assertThatThrownBy(() -> this.colorPatternService.findByName(nullName));
	}

	@Test
	void givenExistingName_whenDeleteByName_thenDeletedWithSuccess() {
		// given
		final String existingName = "monochrome";

		// when
		this.colorPatternService.deleteByName(existingName);

		// then
		final long actualColorPatternCount = this.colorPatternService.count();
		final long actualCubeCount = this.cubeService.count();

		assertThat(actualColorPatternCount).as("Color pattern count").isEqualTo(1);
		assertThat(actualCubeCount).as("Cube count").isEqualTo(1);
	}

	@Test
	void givenNonExistingName_whenDeleteByName_thenNothingDeleted() {
		// given
		final String nonExistingName = "red";

		// when
		this.colorPatternService.deleteByName(nonExistingName);

		// then
		final long actualColorPatternCount = this.colorPatternService.count();
		final long actualCubeCount = this.cubeService.count();

		assertThat(actualColorPatternCount).as("Color pattern count").isEqualTo(2);
		assertThat(actualCubeCount).as("Cube count").isEqualTo(4);
	}

	@Test
	void givenNullName_whenDeleteByName_thenThrowsException() {
		// given
		final String nullName = null;

		// when
		// then
		assertThatThrownBy(() -> this.colorPatternService.deleteByName(nullName));

		final long actualColorPatternCount = this.colorPatternService.count();
		final long actualCubeCount = this.cubeService.count();

		assertThat(actualColorPatternCount).as("Color pattern count").isEqualTo(2);
		assertThat(actualCubeCount).as("Cube count").isEqualTo(4);
	}
}
