package com.cubershop.service;

import com.cubershop.entity.Cube;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CubeService {

	Cube save(Cube cube);

	Iterable<Cube> saveAll(Iterable<Cube> cubes);

	Optional<Cube> findById(UUID id);

	List<Cube> findAllByName(String name);

	Iterable<Cube> findAll();

	void deleteById(UUID id);

	void deleteAll();

	long count();
}
