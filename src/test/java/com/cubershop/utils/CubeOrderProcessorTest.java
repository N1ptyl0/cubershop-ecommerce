package com.cubershop.utils;

import com.cubershop.entity.Cube;
import com.cubershop.exception.NotAcceptableOrderException;
import com.cubershop.helpers.CubeHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

public final class CubeOrderProcessorTest {

	@Test
	void whenProcessReceiveAcceptableOrderThenReturnsCorrectly() throws NotAcceptableOrderException {
		// given
		String acceptableOrder = "alpha_asc";
		List<Cube> cubes = CubeHelper.getFiveCubes();

		// when
		List<Cube> actualCubes = CubeOrderProcessor.process(cubes, acceptableOrder);

		// then
		assertThat(actualCubes).as("Null check").isNotNull()
			.as("Empty check").isNotEmpty()
			.as("Size of list").hasSize(5)
			.extracting("name", String.class)
			.containsExactlyInAnyOrderElementsOf(
				cubes.stream().map(Cube::getName).collect(Collectors.toList())
			);
	}

	@Test
	void whenProcessReceiveNotAcceptableOrderThenReturnsThrowsException() {
		// given
		String acceptableOrder = "beta_asc";
		List<Cube> cubes = CubeHelper.getFiveCubes();

		// when then
		assertThatExceptionOfType(NotAcceptableOrderException.class)
			.isThrownBy(() -> CubeOrderProcessor.process(cubes, acceptableOrder));
	}
}
