package com.cubershop.service;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Image;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageService {

	Optional<Image> findById(UUID uuid);

	List<Image> findByCube(Cube cube);

	long count();
}
