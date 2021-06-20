package com.cubershop.repository;

import com.cubershop.entity.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends CrudRepository<Type, Integer> {

	Optional<Type> findByName(Type.Name name);

	void deleteByName(Type.Name name);
}
