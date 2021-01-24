package com.cubershop.context.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public final class ListProcessorTest {

    private final int N = 3;

    @Test
    public void groupBy_por_tres_com_lista_vazia() {
        List<String> items = Collections.emptyList();
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(0, result.size(), "size da list deve ser 0");
        assertTrue(result.isEmpty(), "A list deve estar vazia");
        assertEquals(Collections.emptyList(), result, "A result list deve ser igual a list esperada");
    }

    @Test
    public void groupBy_por_tres_com_cinco_items() {
        List<String> items = List.<String>of("a", "a", "a", "a", "a");
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(2, result.size(), "size da result list deve ser 2");
        assertEquals(3, result.get(0).size(), "size da list do primeiro elemento deve ser 3");
        assertEquals(2, result.get(1).size(), "size da list do segundo elemento deve ser 2");
        assertEquals(
            List.<List<String>>of(
                List.<String>of("a", "a", "a"),
                List.<String>of("a", "a")
            ),
            result,
            "A result list deve ser igual a list esperada"
        );
    }

    @Test
    public void groupBy_por_tres_com_quatro_items() {
        List<String> items = List.<String>of("a", "a", "a", "a");
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(2, result.size(), "size da result list deve ser 2");
        assertEquals(3, result.get(0).size(), "size da list do primeiro elemento deve ser 3");
        assertEquals(1, result.get(1).size(), "size da list do segundo elemento deve ser 1");
        assertEquals(
            List.<List<String>>of(
                List.<String>of("a", "a", "a"),
                List.<String>of("a")
            ),
            result
        );
    }

    @Test
    public void groupBy_por_tres_com_tres_items() {
        List<String> items = List.<String>of("a", "a", "a");
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(1, result.size(), "size da result list deve ser 1");
        assertEquals(3, result.get(0).size(), "size da list do primeiro elemento deve ser 3");
        assertEquals(
            List.<List<String>>of(List.<String>of("a", "a", "a")),
            result,
            "A result list deve ser igual a list esperada"
        );
    }

    @Test
    public void groupBy_por_tres_com_dois_items() {
        List<String> items = List.<String>of("a", "a");
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(1, result.size(), "size da result list deve ser 1");
        assertEquals(2, result.get(0).size(), "size da list do primeiro elemento deve ser 2");
        assertEquals(
            List.<List<String>>of(List.<String>of("a", "a")),
            result,
            "A result list deve ser igual a list esperada"
        );
    }

    @Test
    public void groupBy_por_tres_com_um_item() {
        List<String> items = List.<String>of("a");
        List<List<String>> result = ListProcessor.<String>groupBy(items, N);

        assertEquals(1, result.size(), "size da result list deve ser 1");
        assertEquals(1, result.get(0).size(), "size da list do primeiro elemento deve ser 1");
        assertEquals(
            List.<List<String>>of(List.<String>of("a")),
            result,
            "A result list deve ser igual a list esperada"
        );
    }

}
