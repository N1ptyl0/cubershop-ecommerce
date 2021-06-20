package com.cubershop.repository;

import com.cubershop.entity.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image, UUID> {
}
