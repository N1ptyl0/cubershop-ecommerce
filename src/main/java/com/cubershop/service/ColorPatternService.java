package com.cubershop.service;

import com.cubershop.entity.ColorPattern;

import java.util.Optional;

public interface ColorPatternService {

	Iterable<ColorPattern> findAll();

	Optional<ColorPattern> findByName(String name);

	void deleteByName(String name);

	void deleteAll();

	long count();
}
