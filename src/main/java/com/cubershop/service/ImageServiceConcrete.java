package com.cubershop.service;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Image;
import com.cubershop.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceConcrete implements ImageService {

	private final ImageRepository imageRepository;

	@Autowired
	public ImageServiceConcrete (ImageRepository repository) {
		this.imageRepository = repository;
	}

	@Override
	@Transactional
	public Optional<Image> findById(UUID id) {
		return imageRepository.findById(id);
	}

	@Override
	public List<Image> findByCube(Cube cube) {
		return null;
	}

	@Override
	public long count () {
		return imageRepository.count();
	}
}
