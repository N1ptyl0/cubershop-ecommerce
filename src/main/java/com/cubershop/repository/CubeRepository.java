package com.cubershop.repository;

import com.cubershop.entity.Cube;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CubeRepository extends PagingAndSortingRepository<Cube, UUID> {

	@Query(value =
  	"""
		SELECT c FROM Cube c JOIN FETCH c.colorPattern \
		JOIN FETCH c.type JOIN FETCH c.images WHERE c.id = :id
	""")
	@Override
	Optional<Cube> findById(@Param(value = "id") UUID id);

	List<Cube> findAllByName(String name);
}
