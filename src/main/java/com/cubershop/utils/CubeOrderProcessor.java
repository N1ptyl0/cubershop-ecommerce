package com.cubershop.utils;

import com.cubershop.entity.Cube;
import com.cubershop.exception.NotAcceptableOrderException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CubeOrderProcessor {

	public static List<Cube> process(List<Cube> list, String order) throws NotAcceptableOrderException {
		Map<String, Comparator<Cube>> stringComparatorMap = new HashMap<>();
		stringComparatorMap.put("alpha_asc", Comparator.comparing(Cube::getName));
		stringComparatorMap.put("alpha_desc", Comparator.comparing(Cube::getName).reversed());
		stringComparatorMap.put("price_asc", Comparator.comparing(Cube::getPrice));
		stringComparatorMap.put("price_desc", Comparator.comparing(Cube::getPrice).reversed());

		if(!stringComparatorMap.containsKey(order)) throw new NotAcceptableOrderException(order);

		return list.stream().sorted(stringComparatorMap.get(order)).collect(Collectors.toList());
	}
}
