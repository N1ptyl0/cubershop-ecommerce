package com.cubershop.controller;

import com.cubershop.VoidAppConfiguration;
import com.cubershop.entity.Cube;
import com.cubershop.exception.CubeNotFoundException;
import com.cubershop.helper.CubeHelper;
import com.cubershop.service.CubeService;
import com.cubershop.service.TypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(classes = VoidAppConfiguration.class)
public class PageControllerIntegrationTest {

	private MockMvc mockMvc;
	private CubeService cubeService;
	private TypeService typeService;
	private final List<Cube> fiveCubes = CubeHelper.builder().withCubes(5).withImages(2).get();

	@BeforeEach
	void setup() {
		this.cubeService = mock(CubeService.class);
		this.typeService = mock(TypeService.class);
		this.mockMvc = standaloneSetup(new PageController(this.cubeService, this.typeService)).build();
	}

	@Test
	void givenValidPath_whenGETHomePage_thenResponseOK() throws Exception {
		// given
		final String validPath = "/";
		given(cubeService.findAll()).willReturn(fiveCubes);

		// when
		mockMvc.perform(get(validPath).accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_HTML_VALUE))
			.andExpect(model().size(2))
			.andExpect(handler().methodName("homePage"));

		// then
		// verify
		verify(cubeService, times(1)).findAll();
		verify(cubeService, only()).findAll();
	}

	@Test
	void givenExistingId_whenGETDescriptionPage_thenResponsesOk() throws Exception {
		// given
		final UUID existentId = UUID.randomUUID();
		given(this.cubeService.findById(eq(existentId))).willReturn(Optional.ofNullable(CubeHelper.getOneCube(2)));

		// when
		mockMvc.perform(get("/description/"+existentId)
			.accept(MediaType.ALL))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.TEXT_HTML))
			.andExpect(model().size(2))
			.andExpect(handler().methodName("descriptionPage"));

		// then
		// verify
		verify(cubeService, times(1)).findById(eq(existentId));
		verify(cubeService, only()).findById(eq(existentId));
	}

	@Test
	void givenNonExistingId_whenGETDescriptionPage_thenResponsesNOT_FOUND() throws Exception {
		// given
		final UUID nonExistentId = UUID.randomUUID();

		given(cubeService .findById(eq(nonExistentId))).willReturn(Optional.ofNullable(null));

		// when
		MvcResult result = mockMvc.perform(get("/description/"+nonExistentId)
			.accept(MediaType.ALL))
			.andExpect(status().isNotFound())
			.andExpect(handler().methodName("descriptionPage")).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(CubeNotFoundException.class);

		// verify
		verify(cubeService, times(1)).findById(eq(nonExistentId));
		verify(cubeService, only()).findById(eq(nonExistentId));
	}

	@Test
	void givenMatchingExpression_whenGETSearchPage_thenResponseOK() throws Exception {
		// given
		final String matchingExpression = "abc";

		given(this.cubeService.findAllByName(eq(matchingExpression)))
			.willReturn(CubeHelper.builder().withCubes(2).withImages(2).get());

		// when
		mockMvc.perform(
			get("/search").param("exp", matchingExpression).accept(MediaType.ALL)
		)
		.andExpect(status().isOk())
		.andExpect(handler().methodName("searchPage"))
		.andExpect(model().size(5))
		.andExpect(view().name("catalog"))
		.andExpect(content().contentType(MediaType.TEXT_HTML));

		// then
		// verify
		verify(this.cubeService, times(1)).findAllByName(eq(matchingExpression));
		verify(this.cubeService, only()).findAllByName(eq(matchingExpression));
	}

	@Test
	void givenNonMatchingExpression_whenGETSearchPage_thenResponsesNOT_FOUND() throws Exception {
		// given
		final String nonMatchingExpression = "z";

		given(this.cubeService.findAllByName(eq(nonMatchingExpression))).willReturn(Collections.emptyList());

		// when
		MvcResult result = mockMvc.perform(
			get("/search").param("exp", nonMatchingExpression).accept(MediaType.ALL)
		)
	   .andExpect(status().isNotFound()).andReturn();

		// then
		assertThat(result.getResolvedException()).isNotNull();
		assertThat(result.getResolvedException()).isInstanceOf(CubeNotFoundException.class);

		// verify
		verify(this.cubeService, times(1)).findAllByName(eq(nonMatchingExpression));
		verify(this.cubeService, only()).findAllByName(eq(nonMatchingExpression));
	}

//	@Test
//	void whenGETSearchPageWithNotAcceptableOrderParamThenResponseBAD_REQUEST() throws Exception {
//		// given
//		final String givenExpression = "x";
//		final String notAcceptableOrderUnderTest = "theta_asc";
//
//		given(this.cubeService.findAllByName(eq(givenExpression)))
//			.willReturn(Optional.ofNullable(fiveCubes));
//
//		// when
//		MvcResult result = mockMvc.perform(
//			get("/search")
//			.param("exp", givenExpression).accept(MediaType.ALL)
//			.param("order", notAcceptableOrderUnderTest)
//		)
//		.andExpect(status().isBadRequest()).andReturn();
//
//		// then
//		assertThat(result.getResolvedException()).isNotNull();
//		assertThat(result.getResolvedException()).isInstanceOf(OrderNotAcceptableException.class);
//
//		// verify
//		verify(this.cubeService
//				, times(1))
//			.findCubesByName(eq(givenExpression));
//		verify(this.cubeService
//				, only())
//			.findCubesByName(eq(givenExpression));
//	}

//	@Test
//	void whenGETSearchPageWithOKOrderParamThenResponseOK() throws Exception {
//		// given
//		final String givenExpression = "x";
//		final String okOrderUnderTest = "price_asc";
//
//		given(this.cubeService
//					  .findCubesByName(eq(givenExpression)))
//			.willReturn(Optional.ofNullable(this.fiveCubes));
//
//		// when
//		mockMvc.perform(
//			get("/search")
//			.param("exp", givenExpression).accept(MediaType.ALL)
//			.param("order", okOrderUnderTest)
//		)
//		.andExpect(status().isOk())
//		.andExpect(model().size(5))
//		.andExpect(content().contentType(MediaType.TEXT_HTML))
//		.andExpect(handler().methodName("searchPage"))
//		.andExpect(view().name("catalog"));
//
//		// then
//		// verify
//		verify(this.cubeService
//				, times(1))
//			.findCubesByName(eq(givenExpression));
//		verify(this.cubeService
//				, only())
//			.findCubesByName(eq(givenExpression));
//	}
}
