package com.cubershop.helper;

import com.cubershop.entity.ColorPattern;
import com.cubershop.entity.Cube;
import com.cubershop.entity.Image;
import com.cubershop.entity.Type;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CubeHelper {

	public static CubeHelper builder() {
		return new CubeHelper();
	}

	private List<Cube> cubes = new Vector<>();

	private String generateRandomName(final int size) {
		final char[] chars = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};

		return new Random().ints(size, 0, chars.length).boxed()
			.map(i -> (chars[i]+""))
			.collect(Collectors.joining(""));
	}

	public CubeHelper withType(Type type) {
		this.cubes.forEach(cube -> cube.setType(type));
		return this;
	}

	public CubeHelper withColorPattern(ColorPattern colorPattern) {
		this.cubes.forEach(cube -> cube.setColorPattern(colorPattern));
		return this;
	}

	public CubeHelper withCubes(final int quantity) {
		ColorPattern colorPattern = ColorPattern.builder()
			.id(null)
			.name("Traditional")
			.cubes(new Vector<>())
			.createDate(Calendar.getInstance()).build();

		Type type = Type.builder()
			.name(Type.Name._3x3x3)
			.cubes(new Vector<>())
			.id(null)
			.createDate(Calendar.getInstance()).build();

		IntStream.range(0, quantity).boxed()
			.map(i -> {
				return Cube.builder()
					.brand("IG")
					.colorPattern(colorPattern)
					.name(generateRandomName(8))
					.lastUpdate(Calendar.getInstance())
					.price(90.10)
					.size(60)
					.type(type)
					.description(null)
					.id(null)
					.installment(null)
					.createDate(Calendar.getInstance())
					.images(null).build();
			})
			.forEach(cube -> {
				colorPattern.addCube(cube);
				type.addCube(cube);
				this.cubes.add(cube);
			});

		return this;
	}

	public CubeHelper withImages(final int quantity) {
		cubes.forEach(cube -> {
			cube.setImages(
				IntStream.range(0, quantity).boxed()
					.map(i -> Image.builder()
						.id(null)
						.createDate(Calendar.getInstance())
						.body(generateRandomName(5).getBytes()).build()
					)
					.peek(image -> image.setCube(cube))
					.collect(Collectors.toUnmodifiableList())
			);
		});

		return this;
	}

	public List<Cube> get() {
		return this.cubes;
	}

	public static Cube getOneCube(final int imageCount) {
		return builder().withCubes(1).withImages(imageCount).get().get(0);
	}

	public static Cube getOneCube(final int imageCount, final Type type) {
		return builder().withCubes(1).withImages(imageCount).withType(type).get().get(0);
	}

	public static Cube getOneCube(final int imageCount, final Type type, final ColorPattern colorPattern) {
		return builder().withCubes(1)
			.withImages(imageCount)
			.withType(type)
			.withColorPattern(colorPattern).get().get(0);
	}
}
