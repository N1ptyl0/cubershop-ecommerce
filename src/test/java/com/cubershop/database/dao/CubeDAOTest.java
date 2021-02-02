package com.cubershop.database.dao;

import com.cubershop.context.entity.Cube;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

public final class CubeDAOTest {

    private JdbcTemplate jdbcTemplate;
    private CubeDAO cubeDAO;

    @BeforeEach
    public void init() {
        this.jdbcTemplate = mock(JdbcTemplate.class);
        this.cubeDAO = new CubeDAO(jdbcTemplate);
    }

    @Test
    public void findHomeCubes_test() {
        // given
        List<Cube> expectedList = List.<Cube>of(new Cube(), new Cube(), new Cube(), new Cube(), new Cube());

        when(this.jdbcTemplate.query(anyString(), any(RowMapper.class)))
            .thenReturn(expectedList);

        // when
        List<Cube> resultingList = this.cubeDAO.findHomeCubes();

        // then
        assertEquals(5, resultingList.size(), "size of resulting list");
        assertEquals(expectedList, resultingList, "Resulting list equals to expected list");

        verify(this.jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    public void insertCube_without_mageFile_test() {
       Cube cube = new Cube();

       when(this.jdbcTemplate.queryForObject(
           anyString(),
           eq(UUID.class),
           anyString(),
           anyDouble(),
           anyString(),
           anyString(),
           anyString(),
           anyInt(),
           anyString()
       )).thenReturn(UUID.randomUUID());

        when(this.jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
            .thenReturn(new int[]{1});

        cubeDAO.saveCube(cube);

        verify(this.jdbcTemplate, times(1)).queryForObject(
           anyString(),
           eq(UUID.class),
           anyString(),
           anyDouble(),
           anyString(),
           anyString(),
           anyString(),
           anyInt(),
           anyString()
       );
       verify(this.jdbcTemplate, never()).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    public void insertCube_with_ImageFile_test()  {
        Cube cube = new Cube();
        cube.setImageFile(List.<MultipartFile>of(new MockMultipartFile("noname", new byte[]{})));

        when(this.jdbcTemplate.queryForObject(
           anyString(),
           eq(UUID.class),
           anyString(),
           anyDouble(),
           anyString(),
           anyString(),
           anyString(),
           anyInt(),
           anyString()
        )).thenReturn(UUID.randomUUID());

        when(this.jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
           .thenReturn(new int[]{1});

        cubeDAO.saveCube(cube);

        verify(this.jdbcTemplate, times(1)).queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString()
        );
        verify(this.jdbcTemplate, times(1)).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    public void findCubesByType_test() {
        // given
        List<Cube> expectedList = List.<Cube>of(new Cube(), new Cube(), new Cube());

        when(this.jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        List<Cube> resultingList = this.cubeDAO.findCubesByType("2x2x2");

        // then
        assertNotNull(resultingList);
        assertEquals(3, resultingList.size(), "Size of resulting list");
        assertEquals(expectedList, resultingList);

        verify(this.jdbcTemplate, times(1))
            .query(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    public void findCubesByTypeAndOrder_test() {
        List<Cube> expectedList = List.<Cube>of(new Cube(), new Cube(), new Cube());

        when(this.jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        List<Cube> resultingList = this.cubeDAO.findCubesByTypeAndOrder("2x2x2", "alpha_asc");

        assertNotNull(resultingList);
        assertEquals(3, resultingList.size(), "Size of resulting list");
        assertEquals(expectedList, resultingList);

        verify(this.jdbcTemplate, times(1))
            .query(anyString(), any(RowMapper.class), anyString());
    }

//    @Test
//    public void findCubesByIdList_test() {
//        List<Cube> expectedList = List.<Cube>of(new Cube(), new Cube(), new Cube());
//
//        when(this.jdbcTemplate.query(anyString(), any(RowMapper.class)))
//            .thenReturn(expectedList);
//
//        List<Cube> resultingList = this.cubeDAO.findCubesByIdList(new String[]{});
//
//        assertNotNull(resultingList);
//        assertEquals(3, resultingList.size(), "Size of resulting list");
//        assertEquals(expectedList, resultingList);
//
//        verify(this.jdbcTemplate, times(1))
//            .query(anyString(), any(RowMapper.class));
//    }

    @Test
    public void findCubeById_test() {
        Cube expectedCube = new Cube();

        when(this.jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), any(UUID.class)))
            .thenReturn(expectedCube);

        Cube resultingCube = this.cubeDAO.findCubeById(UUID.randomUUID());

        assertNotNull(resultingCube);
        assertEquals(expectedCube, resultingCube);

        verify(this.jdbcTemplate, times(1))
            .queryForObject(anyString(), any(RowMapper.class), any(UUID.class));
    }

    @Test
    public void findImageById_test() {
        byte[] expectedBytes = {};

        when(this.jdbcTemplate.queryForObject(anyString(), eq(byte[].class), any(UUID.class)))
            .thenReturn(expectedBytes);

        byte[] resultingBytes = this.cubeDAO.findImageById(UUID.randomUUID());

        assertNotNull(resultingBytes);
        assertArrayEquals(expectedBytes, resultingBytes);

        verify(this.jdbcTemplate, times(1)).queryForObject(anyString(), eq(byte[].class), any(UUID.class));
    }
}
