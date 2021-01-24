package com.cubershop.database.base;

import com.cubershop.context.entity.Cube;

import java.util.List;

public interface WishlistDAOBase {

    List<Cube> findCubesByIdList(String[] idList);
}
