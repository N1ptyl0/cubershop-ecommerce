package com.cubershop.database.dao;

import com.cubershop.database.base.WishlistDAOBase;
import com.cubershop.context.entity.Cube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistDAO {

    @Autowired
    private WishlistDAOBase wishlistDAOBase;

    public List<Cube> findCubesByIdList(String[] codes) {
        return wishlistDAOBase.findCubesByIdList(codes);
    }

    public List<Cube> findCubesByIdList(List<String> codes) {
        return findCubesByIdList(codes.toArray(String[]::new));
    }
}
