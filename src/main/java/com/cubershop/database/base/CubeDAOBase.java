package com.cubershop.database.base;

import com.cubershop.context.entity.Cube;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CubeDAOBase extends WishlistDAOBase {

    void insertCube(Cube cube);

    List<Cube> findHomeCubes();

    List<Cube> findCubesByType(String type);

    List<Cube> findCubesByTypeAndOrder(String type, String order, String sort);

    @Override
    List<Cube> findCubesByIdList(String[] ids);

    Cube findCubeById(UUID uuid);

    byte[] findImageById(UUID uuid);
}
