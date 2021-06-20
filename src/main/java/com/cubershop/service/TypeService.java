package com.cubershop.service;

import com.cubershop.entity.Type;

import java.util.Optional;

public interface TypeService {

	Iterable<Type> findAll();

	Optional<Type> findByName(Type.Name name);

	void deleteByName(Type.Name name);

	void deleteAll();

	long count();
}
