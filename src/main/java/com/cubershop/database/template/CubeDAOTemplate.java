package com.cubershop.database.template;

import com.cubershop.entity.Cube;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CubeDAOTemplate {

    UUID saveCube(Cube cube);

    Optional<List<Cube>> findHomeCubes();

    Optional<List<Cube>> findCubesByType(String type);

    Optional<List<Cube>> findCubesByIdList(UUID[] uuids);

    Optional<Cube> findCubeById(UUID uuid);

    Optional<byte[]> findImageById(UUID uuid);

    Optional<List<Cube>> findCubesByName(String name);
}
