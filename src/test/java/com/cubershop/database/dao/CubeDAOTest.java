package com.cubershop.database.dao;

import com.cubershop.database.template.CubeDAOTemplate;
import com.cubershop.entity.Cube;
import com.cubershop.helpers.CubeHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings({"OptionalGetWithoutIsPresent", "unchecked"})
final class CubeDAOTest {

    private JdbcTemplate jdbcTemplate;
    private CubeDAOTemplate cubeDAO;

    @BeforeEach
    void setup() {
        this.jdbcTemplate = mock(JdbcTemplate.class);
        this.cubeDAO = new CubeDAO(jdbcTemplate);
    }

    @Test
    void whenFindHomeCubesIsInvokedThenReturnsListCorrectly() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
            .thenReturn(expectedList);

        // when
        List<Cube> actualList = this.cubeDAO.findHomeCubes().get();

        // then
        assertThat(actualList)
            .as("Empty check").isNotEmpty()
            .as("Null check").isNotNull()
            .as("Size of actual list").hasSize(5)
            .extracting("name", String.class)
            .as("Validation of the list")
                .containsExactly("Blue cube", "Yellow cube", "Green cube", "Pink cube", "Red cube");

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void whenSaveCubeIsInvokedWithNullCubeThenDoNothingAndReturnsNullUUID() {
        // given
        when(jdbcTemplate.queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString(),
            anyBoolean(),
            anyBoolean(),
            anyInt()
        )).thenReturn(UUID.randomUUID());

        when(this.jdbcTemplate.batchUpdate(anyString(),
            any(BatchPreparedStatementSetter.class))).thenReturn(new int[]{});

        // when
        UUID uuid = cubeDAO.saveCube(null);

        // then
        assertThat(uuid).as("Null check").isNull();

        verify(jdbcTemplate, never()).queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString(),
            anyBoolean(),
            anyBoolean(),
            anyInt()
        );

        verify(jdbcTemplate, never()).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void whenACubeIsSavedWithoutImageThenShouldProceedCorrectlyAndReturnsUUID() {
        // given
        Cube cube = CubeHelper.builder().noImage().build();

        when(jdbcTemplate.queryForObject(
           anyString(),
           eq(UUID.class),
           anyString(),
           anyDouble(),
           anyString(),
           anyString(),
           anyString(),
           anyInt(),
           anyString(),
           anyBoolean(),
           anyBoolean(),
           anyInt()
        )).thenReturn(UUID.randomUUID());

        when(this.jdbcTemplate.batchUpdate(anyString(),
            any(BatchPreparedStatementSetter.class))).thenReturn(new int[]{});

        // when
        UUID uuid = cubeDAO.saveCube(cube);

        // then
        assertThat(uuid).as("Null check").isNotNull();

        verify(jdbcTemplate, times(1)).queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString(),
            anyBoolean(),
            anyBoolean(),
            anyInt()
       );

       verify(jdbcTemplate, never()).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void whenACubeIsSavedWithImageThenShouldProceedCorrectlyAndReturnsUUID()  {
        Cube cube = CubeHelper.builder().withImage().build();

        when(jdbcTemplate.queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString(),
            anyBoolean(),
            anyBoolean(),
            anyInt()
        )).thenReturn(UUID.randomUUID());

        when(this.jdbcTemplate.batchUpdate(anyString(),
            any(BatchPreparedStatementSetter.class))).thenReturn(new int[]{});

        // when
        UUID actualUUID = cubeDAO.saveCube(cube);

        //then
        assertThat(actualUUID).as("Null check").isNotNull();

        verify(jdbcTemplate, times(1)).queryForObject(
            anyString(),
            eq(UUID.class),
            anyString(),
            anyDouble(),
            anyString(),
            anyString(),
            anyString(),
            anyInt(),
            anyString(),
            anyBoolean(),
            anyBoolean(),
            anyInt()
        );

        verify(jdbcTemplate, times(1)).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

    @Test
    void whenFindCubesByTypeIsCorrectlyInvokedThenReturnsExpectedList() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        List<Cube> actualList = this.cubeDAO.findCubesByType("2x2x2").get();

        // then
        assertThat(actualList)
            .as("Empty check").isNotEmpty()
            .as("Null check").isNotNull()
            .as("Size of actual list").hasSize(5)
            .as("Validation of the list").extracting("name", String.class)
            .containsExactly("Blue cube", "Yellow cube", "Green cube", "Pink cube", "Red cube");

        verify(jdbcTemplate, times(1))
            .query(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    void whenFindCubesByTypeIsInvokedWithNullTypeThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        Optional<List<Cube>> actualList = this.cubeDAO.findCubesByType(null);

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never())
            .query(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    void whenFindCubesByTypeIsInvokedWithEmptyTypeThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        Optional<List<Cube>> actualList = this.cubeDAO.findCubesByType("");

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never())
            .query(anyString(), any(RowMapper.class), anyString());
    }
    
    @Test
    void whenFindCubesByIdListIsInvokedCorrectlyThenReturnsExpectedList() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedList);
        
        // when
        List<Cube> actualList = cubeDAO.findCubesByIdList(new UUID[]{UUID.randomUUID()}).get();
        
        // then
        assertThat(actualList)
            .as("Empty check").isNotEmpty()
            .as("Null check").isNotNull()
            .as("Size of actual list").hasSize(5)
            .as("Validation of the list").extracting("name", String.class)
            .containsExactly("Blue cube", "Yellow cube", "Green cube", "Pink cube", "Red cube");
        
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
        verify(jdbcTemplate, only()).query(anyString(), any(RowMapper.class));
    }

    @Test
    void whenFindCubesByIdListIsInvokedWithEmptyUUIDListThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedList);

        // when
        Optional<List<Cube>> actualList = cubeDAO.findCubesByIdList(new UUID[]{});

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class));
    }

    @Test
    void whenFindCubesByIdListIsInvokedWithNullUUIDListThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedList);

        // when
        Optional<List<Cube>> actualList = cubeDAO.findCubesByIdList(null);

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class));
    }
    
    @Test
    void whenFindCubeByIdIsInvokedWithValidIdThenReturnsExpectedCube() {
        // given
        Cube expectedCube = CubeHelper.builder().name("Blue cube").build();

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), isA(UUID.class)))
            .thenReturn(expectedCube);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
            .thenReturn(1);

        // when
        Cube actualCube = this.cubeDAO.findCubeById(UUID.randomUUID()).get();

        // then
        assertThat(actualCube).as("Null check").isNotNull()
            .as("Validation of the cube").isEqualTo(expectedCube);
        
        verify(jdbcTemplate, times(1))
            .queryForObject(anyString(), any(RowMapper.class), any(UUID.class));

        verify(jdbcTemplate, times(1))
            .queryForObject(anyString(), eq(Integer.class), any(UUID.class));
    }

    @Test
    void whenFindCubeByIdIsInvokedWithNullIdThenReturnsEmptyOptional() {
        // given
        Cube expectedCube = CubeHelper.builder().name("Blue cube").build();

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), isA(UUID.class)))
            .thenReturn(expectedCube);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
            .thenReturn(1);

        // when
        Optional<Cube> actualCube = this.cubeDAO.findCubeById(null);

        // then
        assertThat(actualCube.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never())
            .queryForObject(anyString(), any(RowMapper.class), any(UUID.class));

        verify(jdbcTemplate, never())
            .queryForObject(anyString(), eq(Integer.class), any(UUID.class));
    }

    @Test
    void whenFindImageByIdIsInvokedWithValidIdThenReturnsExpectedImage() {
        // given
        byte[] expectedBytes = {123, 124, 125, 126, 127};

        when(jdbcTemplate.queryForObject(anyString(), eq(byte[].class), any(UUID.class)))
            .thenReturn(expectedBytes);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
            .thenReturn(1);

        // when
        byte[] actualBytes = this.cubeDAO.findImageById(UUID.randomUUID()).get();

        // then
        assertThat(actualBytes).as("Null check").isNotNull()
           .as("Empty check").isNotEmpty()
           .as("Validation of the bytes")
           .containsExactly(new byte[]{123, 124, 125, 126, 127});

        verify(jdbcTemplate, times(1))
            .queryForObject(anyString(), eq(byte[].class), isA(UUID.class));

        verify(jdbcTemplate, times(1))
            .queryForObject(anyString(), eq(Integer.class), any(UUID.class));
    }

    @Test
    void whenFindImageByIdIsInvokedWithNullIdThenReturnsEmptyOptional() {
        // given
        byte[] expectedBytes = {123, 124, 125, 126, 127};

        when(jdbcTemplate.queryForObject(anyString(), eq(byte[].class), any(UUID.class)))
            .thenReturn(expectedBytes);

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(UUID.class)))
            .thenReturn(1);

        // when
        Optional<byte[]> actualBytes = this.cubeDAO.findImageById(null);

        // then
        assertThat(actualBytes.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never())
            .queryForObject(anyString(), eq(byte[].class), isA(UUID.class));

        verify(jdbcTemplate, never())
            .queryForObject(anyString(), eq(Integer.class), any(UUID.class));
    }

    @Test
    void whenFindCubesByExpressionIsInvokedThenReturnsExpectedNames() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        List<Cube> actualList = this.cubeDAO.findCubesByName("exp").get();

        // then
        assertThat(actualList).as("Size of the actual list").hasSize(5)
            .as("Null check").isNotNull()
            .as("Empty check").isNotEmpty()
            .as("Validation of cubes")
            .extracting("name", String.class)
            .containsExactlyInAnyOrderElementsOf(
                expectedList.stream().map(Cube::getName).collect(Collectors.toList())
            );

        verify(jdbcTemplate, times(1))
            .query(anyString(), any(RowMapper.class), anyString());
        verify(jdbcTemplate, only())
            .query(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    void whenFindCubesByExpressionIsInvokedWithEmptyExpressionThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        Optional<List<Cube>> actualList = this.cubeDAO.findCubesByName("");

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class), anyString());
    }

    @Test
    void whenFindCubesByExpressionIsInvokedWithNullExpressionThenReturnsEmptyOptional() {
        // given
        List<Cube> expectedList = CubeHelper.getFiveCubes();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(expectedList);

        // when
        Optional<List<Cube>> actualList = this.cubeDAO.findCubesByName(null);

        // then
        assertThat(actualList.isEmpty()).as("Empty check").isTrue();

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class), anyString());
    }
}
