package com.cubershop.database.dao;

import com.cubershop.context.entity.Brand;
import com.cubershop.context.entity.ColorPattern;
import com.cubershop.context.entity.Price;
import com.cubershop.database.base.CubeDAOBase;
import com.cubershop.context.entity.Cube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.object.SqlCall;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.List;
import java.util.UUID;

@Service
public class CubeDAO implements CubeDAOBase {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConversionService converter;

    @Autowired
    public CubeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Cube rowMapper(ResultSet rs, int rowNum) throws SQLException {
        Cube cube = new Cube();

        cube.setId(rs.getObject("id", UUID.class));
        cube.setName(rs.getString("name"));
        cube.setType(rs.getString("type"));
        cube.setPrice(converter.convert(rs.getDouble("price"), Price.class));
        cube.setImageUUID((UUID[]) rs.getArray("source").getArray());
        cube.setSize(rs.getInt("size"));
        cube.setColorPattern(converter.convert(rs.getString("color_pattern").toUpperCase(), ColorPattern.class));
        cube.setBrand(converter.convert(rs.getString("brand").toUpperCase(), Brand.class));
        cube.setDescription(rs.getString("description"));
        return cube;
    }


    @Override
    public List<Cube> findHomeCubes() {
        String sql =
        "WITH a AS (SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = '2x2x2' "+
        "GROUP BY c.id ORDER BY c.price LIMIT 3), "+
        "b AS (SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = '3x3x3' "+
        "GROUP BY c.id ORDER BY c.price LIMIT 3), "+
        "c AS (SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = '4x4x4' "+
        "GROUP BY c.id ORDER BY c.price LIMIT 3), "+
        "d AS (SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = '5x5x5' "+
        "GROUP BY c.id ORDER BY c.price LIMIT 3) "+
        "SELECT * FROM a UNION ALL "+
        "SELECT * FROM b UNION ALL "+
        "SELECT * FROM c UNION ALL "+
        "SELECT * FROM d;";

        return jdbcTemplate.query(sql, this::rowMapper);
    }

    @Override
    public void insertCube(Cube cube) {
        String sql =
        "INSERT INTO cube (name, price, brand, color_pattern, type, size, description) "+
        "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        UUID uuid = this.jdbcTemplate.queryForObject(sql, UUID.class,
            cube.getName(),
            cube.getPrice().getValue(),
            cube.getBrand()+"",
            cube.getColorPattern()+"",
            cube.getType(),
            cube.getSize(),
            cube.getDescription().isEmpty() || cube.getDescription().length() < 3
            ? "Default description" : cube.getDescription()
        );

        if(cube.getImageFile().isEmpty()) return;

        this.jdbcTemplate.batchUpdate("INSERT INTO image (body, cube_id) VALUES (?, ?);",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MultipartFile multipartFile = cube.getImageFile().get(i);
                    try {
                        ps.setBytes(1, multipartFile.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ps.setObject(2, uuid);
                }

                @Override
                public int getBatchSize() {
                    return cube.getImageFile().size();
                }
            }
        );
    }

    @Override
    public List<Cube> findCubesByType(String type) {
        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = ? "+
        "GROUP BY c.id ORDER BY c.name";

        return jdbcTemplate.query(sql, this::rowMapper, type);
    }

    @Override
    public List<Cube> findCubesByTypeAndOrder(String type, String order, String sort) {
        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = ? "+
        "GROUP BY c.id "+
        String.format("ORDER BY %s %s;", order, sort);

        return jdbcTemplate.query(sql, this::rowMapper, type);
    }

    @Override
    public List<Cube> findCubesByIdList(String[] ids) {
        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.id IN "+
        "("+String.join(",", ids)+") "+
        "GROUP BY c.id ORDER BY name";

        return jdbcTemplate.query(sql, this::rowMapper);
    }

    @Override
    public Cube findCubeById(UUID uuid) {
        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.id = ? "+
        "GROUP BY c.id ORDER BY name";
        return jdbcTemplate.queryForObject(sql, this::rowMapper, uuid);
    }

    @Override
    public byte[] findImageById(UUID uuid) {
        String sql = "SELECT body FROM image WHERE id = ?";

        return this.jdbcTemplate.queryForObject(sql, byte[].class, uuid);
    }
}
