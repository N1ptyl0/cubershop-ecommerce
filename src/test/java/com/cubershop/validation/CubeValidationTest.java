package com.cubershop.validation;

import com.cubershop.entity.Cube;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;

public final class CubeValidationTest {

    private static CubeValidation cubeValidation; // object under test
    private static Errors errors;
    private Cube cube;

    @BeforeAll
    static void setup() {
        cubeValidation = new CubeValidation();
    }

    @BeforeEach
    void init() {
        cube = new Cube();
        errors = new BeanPropertyBindingResult(cube, "cube");
    }

    @Test
    void mustValidateNullCube() {
        //given

        // when
        cubeValidation.validate(null, errors);

        // then
        assertThat(errors.hasErrors()).as("Presence of error").isTrue();
        assertThat(errors.getErrorCount()).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getGlobalErrors().get(0).getCode()).as("Validation of error code")
            .isEqualTo("cube.null");
        assertThat(errors.getGlobalErrors().get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Cube object can not be null");
    }

    @Test
    void mustValidateNameFieldNullOrEmpty() {
        // given
        cube.setName("");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("name")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("name")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("name").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.name.empty");
        assertThat(errors.getFieldErrors("name").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Name can not be empty");
    }

    @Test
    void mustValidateNameFieldTooShort() {
        // given
        cube.setName("ab");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("name")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("name")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("name").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.name.too.small");
        assertThat(errors.getFieldErrors("name").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Name can not be too small");
    }

    @Test
    void mustValidateNameFieldTooLong() {
        // given
        cube.setName("a".repeat(256));

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("name")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("name")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("name").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.name.too.long");
        assertThat(errors.getFieldErrors("name").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Name can not be too long");
    }

    @Test
    void mustValidateNameFieldWellFormatted() {
        // given
        cube.setName("#cube");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("name")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("name")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("name").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.name.invalid");
        assertThat(errors.getFieldErrors("name").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Name can not contain special characters");
    }

//    @Test
//    void mustValidatePriceFieldNull() {
//        // given
//        cube.setPrice(null);
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("price")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("price")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("price").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.price.empty");
//        assertThat(errors.getFieldErrors("price").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Price can not be empty");
//    }

//    @Test
//    void mustValidatePriceFieldZero() {
//        // given
//        Price price = new Price();
//        price.setValue(0d);
//        cube.setPrice(price);
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("price")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("price")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("price").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.price.zero");
//        assertThat(errors.getFieldErrors("price").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Price can not be zero");
//    }

//    @Test
//    void mustValidatePriceFieldNegative() {
//        // given
//        Price price = new Price();
//        price.setValue(-10d);
//        cube.setPrice(price);
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("price")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("price")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("price").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.price.negative");
//        assertThat(errors.getFieldErrors("price").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Price can not be a negative number");
//    }

//    @Test
//    void mustValidateTypeFieldNullOrEmpty() {
//        // given
//        cube.setType(null);
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("type")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("type")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("type").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.type.empty");
//        assertThat(errors.getFieldErrors("type").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Type can not be empty");
//    }

//    @Test
//    void mustValidateTypeFieldNotExist() {
//        // given
//        cube.setType("8x8x8");
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("type")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("type")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("type").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.type.not.exist");
//        assertThat(errors.getFieldErrors("type").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Type not acceptable");
//    }

    @Test
    void mustValidateDescriptionFieldNullOrEmpty() {
        // given
        cube.setDescription("");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("description")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("description")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("description").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.description.empty");
        assertThat(errors.getFieldErrors("description").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Description can not be empty");
    }

    @Test
    void mustValidateDescriptionFieldTooShort() {
        // given
        cube.setDescription("ab");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("description")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("description")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("description").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.description.too.small");
        assertThat(errors.getFieldErrors("description").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Description can not be too small");
    }

    @Test
    void mustValidateSizeFieldZero() {
        // given
        cube.setSize(0);

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("size")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("size")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("size").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.size.zero");
        assertThat(errors.getFieldErrors("size").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Size can not be zero");
    }

    @Test
    void mustValidateSizeFieldNegative() {
        // given
        cube.setSize(-10);

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("size")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("size")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("size").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.size.negative");
        assertThat(errors.getFieldErrors("size").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Size can not be a negative number");
    }

    @Test
    void mustValidateSizeFieldTooLong() {
        // given
        cube.setSize(204);

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("size")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("size")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("size").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.size.too.long");
        assertThat(errors.getFieldErrors("size").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Size can not be too long");
    }

    @Test
    void mustValidateBrandFieldNullOrEmpty() {
        // given
        cube.setBrand(null);

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("brand")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("brand")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("brand").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.brand.empty");
        assertThat(errors.getFieldErrors("brand").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Brand can not be empty");
    }


    @Test
    void mustValidateBrandFieldTooShort() {
        // given
        cube.setBrand("an");

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("brand")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("brand")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("brand").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.brand.too.small");
        assertThat(errors.getFieldErrors("brand").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Brand can not be too small");
    }

    @Test
    void mustValidateBrandFieldTooLong() {
        // given
        cube.setBrand("ant".repeat(100));

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("brand")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("brand")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("brand").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.brand.too.long");
        assertThat(errors.getFieldErrors("brand").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Brand can not be too long");
    }

    @Test
    void mustValidateColorPatternNullOrEmpty() {
        // given
        cube.setColorPattern(null);

        // when
        cubeValidation.validate(cube, errors);

        // then
        assertThat(errors.hasFieldErrors("colorPattern")).as("Presence of error").isTrue();
        assertThat(errors.getFieldErrorCount("colorPattern")).as("Quantity of error").isEqualTo(1);
        assertThat(errors.getFieldErrors("colorPattern").get(0).getCode())
            .as("Validation of error code").isEqualTo("cube.colorPattern.empty");
        assertThat(errors.getFieldErrors("colorPattern").get(0).getDefaultMessage())
            .as("Validation of error default message")
            .isEqualTo("Color pattern can not be empty");
    }

//    @Test
//    void mustValidateColorPatternTooShort() {
//        // given
//        cube.setColorPattern("ant");
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("colorPattern")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("colorPattern")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("colorPattern").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.colorPattern.too.small");
//        assertThat(errors.getFieldErrors("colorPattern").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("ColorPattern can not be too small");
//    }

//    @Test
//    void mustValidateColorPatternTooLong() {
//        // given
//        cube.setColorPattern("ant".repeat(25));
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("colorPattern")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("colorPattern")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("colorPattern").get(0).getCode())
//            .as("Validation of error code").isEqualTo("cube.colorPattern.too.long");
//        assertThat(errors.getFieldErrors("colorPattern").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("ColorPattern can not be too long");
//    }

//    @Test
//    void mustValidateImageFileSizeVeryLarge() {
//        //given
//        byte[] bytes = new byte[((int) 1e5) + 1];
//        cube.setImageFiles(List.of(
//            new MockMultipartFile("archive", bytes),
//            new MockMultipartFile("archive", new byte[]{})
//        ));
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("imageFiles")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("imageFiles")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("imageFiles").get(0).getCode())
//            .as("Validation of error code")
//            .isEqualTo("cube.imageFiles.size.exceeded");
//        assertThat(errors.getFieldErrors("imageFiles").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Some file exceeded the file size acceptable");
//    }

//    @Test
//    public void mustValidateListOfImageFileVeryShort() {
//        //given
//        cube.setImageFiles(
//            List.of(new MockMultipartFile("archive", new byte[]{}))
//        );
//
//        // when
//        cubeValidation.validate(cube, errors);
//
//        // then
//        assertThat(errors.hasFieldErrors("imageFiles")).as("Presence of error").isTrue();
//        assertThat(errors.getFieldErrorCount("imageFiles")).as("Quantity of error").isEqualTo(1);
//        assertThat(errors.getFieldErrors("imageFiles").get(0).getCode())
//            .as("Validation of error code")
//            .isEqualTo("cube.imageFiles.image.min.select");
//        assertThat(errors.getFieldErrors("imageFiles").get(0).getDefaultMessage())
//            .as("Validation of error default message")
//            .isEqualTo("Select at least two images");
//    }
}
