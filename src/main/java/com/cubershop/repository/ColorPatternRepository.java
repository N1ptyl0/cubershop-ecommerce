package com.cubershop.repository;

import com.cubershop.entity.ColorPattern;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorPatternRepository extends CrudRepository<ColorPattern, Integer> {

	Optional<ColorPattern> findByName(String name);

	void deleteByName(String name);
}
