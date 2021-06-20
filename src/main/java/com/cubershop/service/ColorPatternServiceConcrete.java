package com.cubershop.service;

import com.cubershop.entity.ColorPattern;
import com.cubershop.repository.ColorPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;

@Service
public class ColorPatternServiceConcrete implements ColorPatternService {

	private final ColorPatternRepository colorPatternRepository;

	@Autowired
	public ColorPatternServiceConcrete(final ColorPatternRepository colorPatternRepository) {
		this.colorPatternRepository = colorPatternRepository;
	}

	@Override
	@Transactional
	public long count() {
		return colorPatternRepository.count();
	}

	@Override
	@Transactional
	public Iterable<ColorPattern> findAll() {
		return new Vector<>() {
			{
				colorPatternRepository.findAll().forEach(this::add);
			}
		};
	}

	@Override
	@Transactional
	public Optional<ColorPattern> findByName(String name) {
		if (Objects.isNull(name))
			throw new IllegalArgumentException("Name can not be null");

		return this.colorPatternRepository.findByName(name);
	}

	@Override
	public void deleteByName(String name) {
		if (Objects.isNull(name))
			throw new IllegalArgumentException("Name can not be null");

		this.colorPatternRepository.deleteByName(name);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.colorPatternRepository.deleteAll();
	}
}
