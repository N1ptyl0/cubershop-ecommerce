package com.cubershop.service;

import com.cubershop.entity.Cube;
import com.cubershop.exception.CubeWithoutImageException;
import com.cubershop.repository.ColorPatternRepository;
import com.cubershop.repository.CubeRepository;
import com.cubershop.repository.ImageRepository;
import com.cubershop.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class CubeServiceConcrete implements CubeService {

	private final CubeRepository cubeRepository;
	private final TypeRepository typeRepository;
	private final ColorPatternRepository colorPatternRepository;
	private final ImageRepository imageRepository;

	@Autowired
	public CubeServiceConcrete(
		CubeRepository cubeRepository,
		TypeRepository typeRepository,
		ColorPatternRepository colorPatternRepository,
		ImageRepository imageRepository
	) {
		this.cubeRepository = cubeRepository;
		this.typeRepository = typeRepository;
		this.colorPatternRepository = colorPatternRepository;
		this.imageRepository = imageRepository;
	}

	@Override
	@Transactional
	public Iterable<Cube> saveAll(Iterable<Cube> cubes) {
		if (Objects.isNull(cubes))
			throw new IllegalArgumentException("List of cubes can not be null");

		this.check(cubes);
		return cubeRepository.saveAll(cubes);
	}

	@Override
	@Transactional
	public Cube save(final Cube cube) {
		if (Objects.isNull(cube))
			throw new IllegalArgumentException("Cube can not be null");

		this.check(List.of(cube));
		return cubeRepository.save(cube);
	}

	private void check(final Iterable<Cube> cubes) {
		cubes.forEach(cube -> {
			if (Objects.isNull(cube))
				throw new IllegalArgumentException("Cube can not be null");

			if (cube.getImages() == null || cube.getImages().size() <= 1)
				throw new CubeWithoutImageException("A cube must have at least two images");

			if (cube.getColorPattern() == null)
				throw new IllegalArgumentException("Color Pattern can not be null");

			if (cube.getType() == null)
				throw new IllegalArgumentException("Type can not be null");

			colorPatternRepository.findByName(cube.getColorPattern().getName())
				.ifPresent(cube::setColorPattern);
			typeRepository.findByName(cube.getType().getName())
				.ifPresent(cube::setType);
		});
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		this.cubeRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Optional<Cube> findById(UUID uuid) {
		return cubeRepository.findById(uuid);
	}

	@Override
	@Transactional
	public Iterable<Cube> findAll() {
		return new Vector<>() {
			{
				cubeRepository.findAll().forEach(this::add);
			}
		};
	}

	@Override
	public List<Cube> findAllByName(String name) {
		return this.cubeRepository.findAllByName(name);
	}

	@Override
	@Transactional
	public void deleteAll () {
		this.cubeRepository.deleteAll();
	}

	@Override
	@Transactional
	public long count () {
		return cubeRepository.count();
	}
}
