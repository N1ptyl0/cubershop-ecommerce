package com.cubershop.service;

import com.cubershop.entity.Type;
import com.cubershop.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TypeServiceConcrete implements TypeService {

	private final TypeRepository typeRepository;

	@Autowired
	public TypeServiceConcrete (TypeRepository typeRepository) {
		this.typeRepository = typeRepository;
	}

	@Override
	public Iterable<Type> findAll() {
		return typeRepository.findAll();
	}

	@Override
	public Optional<Type> findByName(Type.Name name) {
		if (Objects.isNull(name))
			throw new IllegalArgumentException("Name can not be null!");

		return typeRepository.findByName(name);
	}

	@Override
	public void deleteByName(Type.Name name) {
		if (Objects.isNull(name))
			throw new IllegalArgumentException("Name can not be null");

		this.typeRepository.deleteByName(name);
	}

	@Override
	public void deleteAll() {
		this.typeRepository.deleteAll();
	}

	@Override
	public long count () {
		return this.typeRepository.count();
	}
}
