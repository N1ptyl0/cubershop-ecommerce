package com.cubershop.database.base;

import com.cubershop.context.entity.Cube;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CubeDAOBase {

    UUID saveCube(Cube cube);

    List<Cube> findHomeCubes();

    List<Cube> findCubesByType(String type);

    List<Cube> findCubesByTypeAndOrder(String type, String order);

    Cube findCubeById(UUID uuid);

    byte[] findImageById(UUID uuid);

    List<Cube> findCubesByExpressionAndOrder(String expression, String order);

    List<String> findNamesByExpression(String expression);
}
