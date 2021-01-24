package com.cubershop.context.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.cubershop.context.entity.Cube;
import com.cubershop.context.entity.Price;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.*;

public final class CubeValidationTest {

    private static CubeValidation cubeValidation;
    private Cube cube;
    private static Errors errors;

    @BeforeAll
    public static void initAll() {
        cubeValidation = new CubeValidation();
    }

    @BeforeEach
    public void initBoilerplate() {
        cube = new Cube();

        errors = new AbstractBindingResult("cube") {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String field) {
                return null;
            }
        };
    }

    @Test
    public void deve_dar_suporte_a_classe_Cube() {
        assertTrue(cubeValidation.supports(Cube.class));
    }

    @Test
    public void deve_validar_objeto_alvo_null_ou_nao_sendo_do_tipo_Cube() {
        cubeValidation.validate(null, errors);

        assertTrue(errors.hasErrors(), "Deve haver erros globais");
        assertEquals(1, errors.getErrorCount(), "Quantidade de erros deve ser igual a 1");
        assertEquals("cube.null", errors.getGlobalErrors().get(0).getCode(),
            "Code do erro deve ser igual ao esperado");
        assertEquals("Cube object can not be null", errors.getGlobalErrors()
            .get(0).getDefaultMessage(), "Default message do erro deve ser igual ao esperado");
    }

    @Test
    public void deve_validar_campo_name_vazio_ou_null() {
        cube.setName("");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("name"), "Deve haver erros no campo name");
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getCode().equals("cube.name.empty")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Name can not be empty")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_name_muito_curto() {
        cube.setName("ab");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("name"), "Deve haver erros no campo name");
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getCode().equals("cube.name.too.small")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Name can not be too small")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_name_muito_long() {
        cube.setName("a".repeat(256));
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("name"), "Deve haver erros no campo name");
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getCode().equals("cube.name.too.long")),
    "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Name can not be too long")),
        "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_name_invalido() {
        cube.setName("#cube");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("name"), "Deve haver erros no campo name");
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getCode().equals("cube.name.invalid")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("name").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Name can not contain special characters")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_price_null() {
        cube.setPrice(null);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("price"), "Deve haver erros no campo price");
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getCode().equals("cube.price.empty")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Price can not be empty")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_price_zero() {
        Price price = new Price();
        price.setValue(0d);
        cube.setPrice(price);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("price"), "Deve haver erros no campo price");
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getCode().equals("cube.price.zero")),
        "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Price can not be zero")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_price_negative() {
        Price price = new Price();
        price.setValue(-10d);
        cube.setPrice(price);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("price"), "Deve haver erros no campo price");
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getCode().equals("cube.price.negative")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("price").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Price can not be a negative number")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_type_vazio_ou_null() {
        cube.setType(null);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("type"), "Deve haver erros no campo type");
        assertTrue(
            errors.getFieldErrors("type").stream()
                .anyMatch(e -> e.getCode().equals("cube.type.empty")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("type").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Type can not be empty")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_type_que_nao_existe() {
        cube.setType("8x8x8");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("type"), "Deve haver erros no campo type");
        assertTrue(
            errors.getFieldErrors("type").stream()
                .anyMatch(e -> e.getCode().equals("cube.type.not.exist")),
        "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("type").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Type not acceptable")),
        "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_description_vazio_ou_null() {
        cube.setDescription("");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("description"), "Deve haver erros no campo description");
        assertTrue(
            errors.getFieldErrors("description").stream()
                .anyMatch(e -> e.getCode().equals("cube.description.empty")),
    "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("description").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Description can not be empty")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_description_muito_curto() {
        cube.setDescription("ab");
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("description"), "Deve haver erros no campo description");
        assertTrue(
            errors.getFieldErrors("description").stream()
                .anyMatch(e -> e.getCode().equals("cube.description.too.small")),
        "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("description").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Description can not be too small")),
        "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_size_zero() {
        cube.setSize(0);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("size"), "Deve haver erros no campo size");
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getCode().equals("cube.size.zero")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Size can not be zero")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_size_negativo() {
        cube.setSize(-10);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("size"), "Deve haver erros no campo size");
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getCode().equals("cube.size.negative")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Size can not be a negative number")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_size_muito_grande() {
        cube.setSize(204);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("size"),"Deve haver erros no campo size");
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getCode().equals("cube.size.too.long")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("size").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Size can not be too long")),
            "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_brand_vazio_ou_null() {
        cube.setBrand(null);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("brand"), "Deve haver erros no campo brand");
        assertTrue(
            errors.getFieldErrors("brand").stream()
                .anyMatch(e -> e.getCode().equals("cube.brand.empty")),
        "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("brand").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Brand can not be empty")),
        "Default message do erro deve ser igual ao esperado"
        );
    }

    @Test
    public void deve_validar_campo_colorPattern_vazio_ou_null() {
        cube.setColorPattern(null);
        cubeValidation.validate(cube, errors);

        assertTrue(errors.hasFieldErrors("colorPattern"), "Deve haver erros no campo colorPattern");
        assertTrue(
            errors.getFieldErrors("colorPattern").stream()
                .anyMatch(e -> e.getCode().equals("cube.colorPattern.empty")),
            "Code do erro deve ser igual ao esperado"
        );
        assertTrue(
            errors.getFieldErrors("colorPattern").stream()
                .anyMatch(e -> e.getDefaultMessage().equals("Color pattern can not be empty")),
            "Default message do erro deve ser igual ao esperado"
        );
    }
}
