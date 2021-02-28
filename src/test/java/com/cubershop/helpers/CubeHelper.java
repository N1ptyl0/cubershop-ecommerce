package com.cubershop.helpers;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Installment;
import com.cubershop.entity.Price;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public final class CubeHelper extends Cube {

	private final Cube cube = new Cube();

	public static CubeHelper builder() {
		return new CubeHelper();
	}

	public CubeHelper name(String name) {
		this.cube.setName(name);
		return this;
	}

	public CubeHelper price(Price price) {
		this.cube.setPrice(price);
		return this;
	}

	public CubeHelper type(String type) {
		this.cube.setType(type);
		return this;
	}

	public CubeHelper description(String description) {
		this.cube.setDescription(description);
		return this;
	}

	public CubeHelper brand(String brand) {
		this.cube.setBrand(brand);
		return this;
	}

	public CubeHelper colorPattern(String colorPattern) {
		this.cube.setColorPattern(colorPattern);
		return this;
	}

	public CubeHelper installment(Installment installment) {
		this.cube.setInstallment(installment);
		return this;
	}

	public CubeHelper quantity(int quantity) {
		this.cube.setQuantity(quantity);
		return this;
	}

	public CubeHelper stock(boolean stock) {
		this.cube.setStock(stock);
		return this;
	}

	public CubeHelper magnetic(boolean magnetic) {
		this.cube.setStock(magnetic);
		return this;
	}

	public CubeHelper withImages(int quantity) {
		List<MultipartFile> multipartFiles = new Vector<>();
		for(int i = 1; i <= quantity; i++)
			multipartFiles.add(new MockMultipartFile("cube "+i, new byte[]{1, 1, 1, 1}));
		this.cube.setImageFiles(multipartFiles);
		return this;
	}

	public CubeHelper withImage() {
		return this.withImages(1);
	}

	public CubeHelper noImage() {
		this.cube.setImageFiles(Collections.emptyList());
		return this;
	}

	public static List<Cube> getFiveCubes() {
		return getFiveCubesWithType("big");
	}

	public static List<Cube> getFiveCubesWithType(String type) {
		return List.of(
			CubeHelper.builder().withImage().type(type).name("Blue cube").build(),
			CubeHelper.builder().withImage().type(type).name("Yellow cube").build(),
			CubeHelper.builder().withImage().type(type).name("Green cube").build(),
			CubeHelper.builder().withImage().type(type).name("Pink cube").build(),
			CubeHelper.builder().withImage().type(type).name("Red cube").build()
		);
	}

	public static List<Cube> getTenCubes() {
		return List.of(
			CubeHelper.builder().name("Blue cube").build(),
			CubeHelper.builder().name("Yellow cube").build(),
			CubeHelper.builder().name("Green cube").build(),
			CubeHelper.builder().name("Pink cube").build(),
			CubeHelper.builder().name("Brown cube").build(),
			CubeHelper.builder().name("Purple cube").build(),
			CubeHelper.builder().name("Black cube").build(),
			CubeHelper.builder().name("White cube").build(),
			CubeHelper.builder().name("Red cube").build(),
			CubeHelper.builder().name("Orange cube").build()
		);
	}

	public Cube build() {
		return this.cube;
	}
}
