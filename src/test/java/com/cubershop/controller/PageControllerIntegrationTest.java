package com.cubershop.controller;

import com.cubershop.VoidAppConfiguration;
import com.cubershop.database.template.CubeDAOTemplate;
import com.cubershop.entity.Cube;
import com.cubershop.exception.CubeNotFoundException;
import com.cubershop.exception.OrderNotAcceptableException;
import com.cubershop.helpers.CubeHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(classes = VoidAppConfiguration.class)
public class PageControllerIntegrationTest {

	private MockMvc mockMvc;
	private CubeDAOTemplate cubeDAOTemplate;
	private final List<Cube> fiveCubes = CubeHelper.getFiveCubes();

	@BeforeEach
	void setup() {
		// Initialize mocking for CubeDAOTemplate
		this.cubeDAOTemplate = mock(CubeDAOTemplate.class);
		this.mockMvc = standaloneSetup(new PageController(cubeDAOTemplate)).build();
	}

	@Test
	void whenGETHomePageWithValidPathThenResponsesOK() throws Exception {
		// given
		given(cubeDAOTemplate.findHomeCubes()).willReturn(Optional.ofNullable(fiveCubes));

		// when
		mockMvc.perform(get("/").accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_HTML_VALUE))
			.andExpect(model().size(2))
			.andExpect(handler().methodName("homePage"));

		// then
		// verify
		verify(cubeDAOTemplate, times(1)).findHomeCubes();
		verify(cubeDAOTemplate, only()).findHomeCubes();
	}

	@Test
	void whenGETHomePageButNoCubeIsFoundThenResponsesNOT_FOUND() throws Exception {
		// given
		given(cubeDAOTemplate.findHomeCubes()).willReturn(Optional.ofNullable(null));

		// when
		final MvcResult result = mockMvc.perform(get("/").accept(MediaType.ALL))
			.andExpect(status().isNotFound())
			.andExpect(handler().methodName("homePage"))
			.andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(CubeNotFoundException.class);

		// verify
		verify(cubeDAOTemplate, times(1)).findHomeCubes();
		verify(cubeDAOTemplate, only()).findHomeCubes();
	}

	@Test
	void whenGETCatalogPageWithNotAcceptableOrderParamThenResponsesBAD_REQUEST() throws Exception {
		// given
		final String givenType = "3x3x3";
		final String notAcceptableParamUnderTest = "beta_asc";

		given(cubeDAOTemplate.findCubesByType(eq(givenType)))
			.willReturn(Optional.ofNullable(fiveCubes));

		// when
		final MvcResult result = mockMvc.perform(get("/catalog/"+givenType)
			.param("order", notAcceptableParamUnderTest).accept(MediaType.ALL))
			.andExpect(status().isBadRequest()).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(OrderNotAcceptableException.class);

		// verify
		verify(cubeDAOTemplate, times(1)).findCubesByType(eq(givenType));
		verify(cubeDAOTemplate, only()).findCubesByType(eq(givenType));
	}

	@Test
	void whenGETCatalogPageWithOKOrderParamThenResponsesOK() throws Exception {
		// given
		final String givenType = "3x3x3";
		final String OKParamUnderTest = "price_asc";

		given(cubeDAOTemplate.findCubesByType(eq(givenType)))
			.willReturn(Optional.ofNullable(fiveCubes));

		// when
		mockMvc.perform(
			get("/catalog/"+givenType)
			.param("order", OKParamUnderTest).accept(MediaType.ALL)
		)
		.andExpect(status().isOk())
		.andExpect(model().size(5))
		.andExpect(handler().methodName("catalogPage"))
		.andExpect(view().name("catalog"));


		// then
		// verify
		verify(cubeDAOTemplate, times(1)).findCubesByType(eq(givenType));
		verify(cubeDAOTemplate, only()).findCubesByType(eq(givenType));
	}

	@Test
	void whenGETDescriptionPageWithExistingIDThenResponsesOk() throws Exception {
		// given
		final UUID existentIDUnderTest = UUID.randomUUID();
		final Cube givenCube = CubeHelper.builder().build();

		given(cubeDAOTemplate.findCubeById(eq(existentIDUnderTest)))
			.willReturn(Optional.ofNullable(givenCube));

		// when
		mockMvc.perform(get("/description/"+existentIDUnderTest)
			.accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_HTML))
			.andExpect(model().size(2))
			.andExpect(handler().methodName("descriptionPage"));

		// then
		// verify
		verify(cubeDAOTemplate, times(1)).findCubeById(eq(existentIDUnderTest));
		verify(cubeDAOTemplate, only()).findCubeById(eq(existentIDUnderTest));
	}

	@Test
	void whenGETDescriptionPageWithNonExistingIDThenResponsesNOT_FOUND() throws Exception {
		// given
		final UUID nonExistentIDUnderTest = UUID.randomUUID();

		given(cubeDAOTemplate.findCubeById(eq(nonExistentIDUnderTest)))
				.willReturn(Optional.ofNullable(null));
		// when
		MvcResult result = mockMvc.perform(get("/description/"+nonExistentIDUnderTest)
			.accept(MediaType.ALL))
			.andExpect(status().isNotFound())
			.andExpect(handler().methodName("descriptionPage")).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(CubeNotFoundException.class);

		// verify
		verify(cubeDAOTemplate, times(1)).findCubeById(eq(nonExistentIDUnderTest));
		verify(cubeDAOTemplate, only()).findCubeById(eq(nonExistentIDUnderTest));
	}

	@Test
	void whenGETSearchPageWithMatchingExpressionThenResponseOK() throws Exception {
		// given
		final String matchingExpressionUnderTest = "abc";

		given(this.cubeDAOTemplate.findCubesByName(eq(matchingExpressionUnderTest)))
			.willReturn(Optional.ofNullable(this.fiveCubes));

		// when
		mockMvc.perform(
			get("/search").param("exp", matchingExpressionUnderTest).accept(MediaType.ALL)
		)
		.andExpect(status().isOk())
		.andExpect(handler().methodName("searchPage"))
		.andExpect(model().size(5))
		.andExpect(view().name("catalog"))
		.andExpect(content().contentType(MediaType.TEXT_HTML));

		// then
		// verify
		verify(this.cubeDAOTemplate, times(1))
			.findCubesByName(eq(matchingExpressionUnderTest));
		verify(this.cubeDAOTemplate, only())
			.findCubesByName(eq(matchingExpressionUnderTest));
	}

	@Test
	void whenGETSearchPageWithUnmatchedExpressionThenResponseNOT_FOUND() throws Exception {
		// given
		final String unmatchedExpressionUnderTest = "z";

		given(this.cubeDAOTemplate.findCubesByName(eq(unmatchedExpressionUnderTest)))
			.willReturn(Optional.ofNullable(null));

		// when
		MvcResult result = mockMvc.perform(
			get("/search").param("exp", unmatchedExpressionUnderTest).accept(MediaType.ALL)
		)
	   .andExpect(status().isNotFound()).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(CubeNotFoundException.class);

		// verify
		verify(this.cubeDAOTemplate, times(1))
			.findCubesByName(eq(unmatchedExpressionUnderTest));
		verify(this.cubeDAOTemplate, only())
			.findCubesByName(eq(unmatchedExpressionUnderTest));
	}

	@Test
	void whenGETSearchPageWithNotAcceptableOrderParamThenResponseBAD_REQUEST() throws Exception {
		// given
		final String givenExpression = "x";
		final String notAcceptableOrderUnderTest = "theta_asc";

		given(this.cubeDAOTemplate.findCubesByName(eq(givenExpression)))
			.willReturn(Optional.ofNullable(fiveCubes));

		// when
		MvcResult result = mockMvc.perform(
			get("/search")
			.param("exp", givenExpression).accept(MediaType.ALL)
			.param("order", notAcceptableOrderUnderTest)
		)
		.andExpect(status().isBadRequest()).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(OrderNotAcceptableException.class);

		// verify
		verify(this.cubeDAOTemplate, times(1))
			.findCubesByName(eq(givenExpression));
		verify(this.cubeDAOTemplate, only())
			.findCubesByName(eq(givenExpression));
	}

	@Test
	void whenGETSearchPageWithOKOrderParamThenResponseOK() throws Exception {
		// given
		final String givenExpression = "x";
		final String okOrderUnderTest = "price_asc";

		given(this.cubeDAOTemplate.findCubesByName(eq(givenExpression)))
			.willReturn(Optional.ofNullable(this.fiveCubes));

		// when
		mockMvc.perform(
			get("/search")
			.param("exp", givenExpression).accept(MediaType.ALL)
			.param("order", okOrderUnderTest)
		)
		.andExpect(status().isOk())
		.andExpect(model().size(5))
		.andExpect(content().contentType(MediaType.TEXT_HTML))
		.andExpect(handler().methodName("searchPage"))
		.andExpect(view().name("catalog"));

		// then
		// verify
		verify(this.cubeDAOTemplate, times(1))
			.findCubesByName(eq(givenExpression));
		verify(this.cubeDAOTemplate, only())
			.findCubesByName(eq(givenExpression));
	}
}
