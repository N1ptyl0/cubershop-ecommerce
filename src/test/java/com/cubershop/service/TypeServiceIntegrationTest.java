package com.cubershop.service;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Type;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TypeServiceIntegrationTest {

	@Autowired
	private TypeService typeService;

	@Autowired
	private CubeService cubeService;

	@BeforeAll
	@Commit
	void initData() {
		Type type1 = new Type(Type.Name._3x3x3);
		Type type2 = new Type(Type.Name._2x2x2);

		List<Cube> cubesWithType1 = CubeHelper
				.builder().withCubes(3).withImages(2).withType(type1).get();

		Cube cubeWithType2 = CubeHelper.getOneCube(2, type2);

		this.cubeService.saveAll(cubesWithType1);
		this.cubeService.save(cubeWithType2);
	}

	@AfterAll
	@Commit
	@Transactional
	void finishData() {
		this.cubeService.deleteAll();
		this.typeService.deleteAll();
	}

	@Test
	void whenFindAll_thenFetchesAll() {
		// given
		// when
		Iterable<Type> typesFetched = this.typeService.findAll();

		// then
		assertThat(typesFetched).isNotNull().isNotEmpty().hasSize(2);
	}

	@Test
	void givenExistingName_whenFindByName_thenReturnedWithSuccess() {
		// given
		final Type.Name existingName = Type.Name._3x3x3;

		// when
		final Type typeFetched = this.typeService.findByName(existingName).orElse(null);

		// then
		assertThat(typeFetched).isNotNull();
	}

	@Test
	void givenNonExistingName_whenFindByName_thenNothingReturned() {
		// given
		final Type.Name nonExistingName = Type.Name.BIG;

		// when
		final Type typeFetched = this.typeService.findByName(nonExistingName).orElse(null);

		// then
		assertThat(typeFetched).isNull();
	}

	@Test
	void givenNullName_whenFindByName_thenThrowsException() {
		// given
		final Type.Name nullName = null;

		// when
		assertThatThrownBy(() -> this.typeService.findByName(nullName));
	}

	@Test
	void givenExistingName_whenDeleteByName_thenDeletedWithSuccess() {
		// given
		final Type.Name existingName = Type.Name._3x3x3;

		// when
		this.typeService.deleteByName(existingName);

		// then
		final long typeCount = this.typeService.count();

		assertThat(typeCount).isEqualTo(1);
	}

	@Test
	void givenNonExistingName_whenDeleteByName_thenNothingDeleted() {
		// given
		final Type.Name nonExistingName = Type.Name.BIG;

		// when
		this.typeService.deleteByName(nonExistingName);

		// then
		final long typeCount = this.typeService.count();

		assertThat(typeCount).isEqualTo(2);
	}

	@Test
	void givenNullName_whenDeleteByName_thenThrowsException() {
		// given
		final Type.Name nullName = null;

		// when
		// then
		assertThatThrownBy(() -> this.typeService.deleteByName(nullName));
	}
}
