package com.cubershop.database.dao;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Installment;
import com.cubershop.entity.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class CubeDAO implements com.cubershop.database.template.CubeDAOTemplate {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ConversionService converter;

    @Autowired
    public CubeDAO (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Cube rowMapper(ResultSet rs, int rowNum) throws SQLException {
        Cube cube = new Cube();

        cube.setId(rs.getObject("id", UUID.class));
        cube.setName(rs.getString("name"));
        cube.setType(rs.getString("type"));
        cube.setPrice(converter.convert(rs.getDouble("price"), Price.class));
        cube.setInstallment(new Installment(converter.convert(rs.getDouble("price"), Price.class), 3));
        cube.setImageUUID((UUID[]) rs.getArray("source").getArray());
        cube.setSize(rs.getInt("size"));
        cube.setColorPattern(rs.getString("color_pattern").toUpperCase());
        cube.setBrand(rs.getString("brand").toUpperCase());
        cube.setDescription(rs.getString("description"));
        cube.setMagnetic(rs.getBoolean("is_magnetic"));
        cube.setQuantity(rs.getInt("quantity"));
        cube.setStock(rs.getBoolean("in_stock"));

        return cube;
    }


    @Override
    public Optional<List<Cube>> findHomeCubes() {
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

        return Optional.ofNullable(jdbcTemplate.query(sql, this::rowMapper));
    }

    @Override
    public UUID saveCube(Cube cube) {
        if(cube == null) return null;

        String sql =
        "INSERT INTO cube (name, price, brand, color_pattern, "+
        "type, size, description, in_stock, is_magnetic, quantity) "+
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        UUID uuid = this.jdbcTemplate.queryForObject(sql, UUID.class,
            cube.getName(),
            cube.getPrice().getValue(),
            cube.getBrand().toUpperCase(),
            cube.getColorPattern().toUpperCase(),
            cube.getType(),
            cube.getSize(),
            cube.getDescription().isEmpty() || cube.getDescription().length() < 3
            ? "Default description" : cube.getDescription(),
            cube.isStock(),
            cube.isMagnetic(),
            cube.getQuantity()
        );

        if(!cube.getImageFiles().isEmpty())
            saveImageFiles(cube.getImageFiles(), uuid);

        return uuid;
    }

    private void saveImageFiles(List<MultipartFile> multipartFiles, UUID cubeUUID) {
        //noinspection NullableProblems
        this.jdbcTemplate.batchUpdate("INSERT INTO image (body, cube_id) VALUES (?, ?);",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    MultipartFile multipartFile = multipartFiles.get(i);
                    try {
                        ps.setBytes(1, multipartFile.getBytes());
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    ps.setObject(2, cubeUUID);
                }

                @Override
                public int getBatchSize() {
                    return multipartFiles.size();
                }
            }
        );
    }

    @Override
    public Optional<List<Cube>> findCubesByType(String type) {
        if(type == null || type.isEmpty()) return Optional.ofNullable(null);

        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.type = ? "+
        "GROUP BY c.id ORDER BY c.name";

        return Optional.ofNullable(jdbcTemplate.query(sql, this::rowMapper, type));
    }

    @Override
    public Optional<List<Cube>> findCubesByIdList(UUID[] uuids) {
        if(uuids == null || uuids.length == 0) return Optional.ofNullable(null);

        String[] stringUUIDS = Stream.of(uuids).map(uuid -> "'"+uuid+"'").toArray(String[]::new);

        String sql = String.format(
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.id IN "+
        "(%s) GROUP BY c.id ORDER BY name", String.join(",", stringUUIDS));

        return Optional.ofNullable(jdbcTemplate.query(sql, this::rowMapper));
    }

    @Override
    public Optional<Cube> findCubeById(UUID uuid) {
        if(uuid == null || !existsCube(uuid))
            return Optional.ofNullable(null);

        String sql =
        "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.id = ? "+
        "GROUP BY c.id ORDER BY name";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::rowMapper, uuid));
    }

    @Override
    public Optional<byte[]> findImageById(UUID uuid) {
        if(uuid == null || !existsImage(uuid))
            return Optional.ofNullable(null);

        String sql = "SELECT body FROM image WHERE id = ?";

        return Optional.ofNullable(this.jdbcTemplate.queryForObject(sql, byte[].class, uuid));
    }

    @Override
    public Optional<List<Cube>> findCubesByName(String name) {
        if(name == null || name.isEmpty()) return Optional.ofNullable(null);

        String sql = "SELECT c.*, ARRAY_AGG(i.id) AS source FROM cube AS c "+
        "INNER JOIN image AS i ON c.id = i.cube_id WHERE c.name LIKE ? "+
        "GROUP BY c.id ORDER BY name;";

        return Optional.ofNullable(this.jdbcTemplate.query(sql, this::rowMapper, "%"+name+"%"));
    }

    private boolean existsCube(UUID uuid) {
        Integer value = jdbcTemplate
        .queryForObject("SELECT COUNT(*) FROM cube WHERE id = ?;", Integer.class, uuid);

        return value != null && value == 1;
    }

    private boolean existsImage(UUID uuid) {
        Integer value = jdbcTemplate
        .queryForObject("SELECT COUNT(*) FROM image WHERE id = ?;", Integer.class, uuid);

        return value != null && value == 1;
    }
}
