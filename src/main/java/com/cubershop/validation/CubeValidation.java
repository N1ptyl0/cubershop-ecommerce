package com.cubershop.validation;

import com.cubershop.entity.Cube;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.stream.Stream;

public final class CubeValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Cube.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(!(target instanceof Cube) || target == null) {
            errors.reject("cube.null", "Cube object can not be null");
            return;
        }

        Cube cube = (Cube) target;

        validateNameField(cube, errors);
        validatePriceField(cube, errors);
        validateTypeField(cube, errors);
        validateDescriptionField(cube, errors);
        validateSizeField(cube, errors);
        validateBrandField(cube, errors);
        validateColorPatternField(cube, errors);
        validateImageFileField(cube, errors);
    }

    private void validateNameField(Cube cube, Errors errors) {
        if(cube.getName().isEmpty() || cube.getName() == null)
            errors.rejectValue("name", "cube.name.empty",
                "Name can not be empty");
        else if(cube.getName().length() < 3)
            errors.rejectValue("name", "cube.name.too.small",
                "Name can not be too small");
        else if(cube.getName().length() > 255)
            errors.rejectValue("name", "cube.name.too.long",
                "Name can not be too long");
        else if(!cube.getName().matches("^[0-9a-zA-Z._ &-]++$"))
            errors.rejectValue("name", "cube.name.invalid",
                "Name can not contain special characters");
    }

    private void validatePriceField(Cube cube, Errors errors) {
        if(Objects.isNull(cube))
            errors.rejectValue("price", "cube.price.empty", "Price can not be empty");
        else if(cube.getPrice() == 0)
            errors.rejectValue("price", "cube.price.zero", "Price can not be zero");
        else if(cube.getPrice() < 0)
            errors.rejectValue("price", "cube.price.negative",
                "Price can not be a negative number");
    }

    private void validateTypeField(Cube cube, Errors errors) {
        if(cube.getType() == null)
            errors.rejectValue("type", "cube.type.empty", "Type can not be empty");
        else if(Stream.of("_2x2x2", "_3x3x3", "_4x4x4", "_5x5x5", "big").noneMatch(v -> v.equals(cube.getType())))
            errors.rejectValue("type", "cube.type.not.exist",
                "Type not acceptable");
    }

    private void validateDescriptionField(Cube cube, Errors errors) {
        if(cube.getDescription().isEmpty() || cube.getDescription() == null)
            errors.rejectValue("description",
                "cube.description.empty", "Description can not be empty");
        else if(cube.getDescription().length() < 3)
            errors.rejectValue("description",
                "cube.description.too.small", "Description can not be too small");
    }

    private void validateSizeField(Cube cube, Errors errors) {
        if(cube.getSize() < 0)
            errors.rejectValue("size", "cube.size.negative",
                "Size can not be a negative number");
        else if(cube.getSize() == 0)
            errors.rejectValue("size", "cube.size.zero",
                "Size can not be zero");
        else if(cube.getSize() > 200)
            errors.rejectValue("size", "cube.size.too.long",
                "Size can not be too long");

    }

    private void validateBrandField(Cube cube, Errors errors) {
        if(cube.getBrand() == null || cube.getBrand().isEmpty())
            errors.rejectValue("brand",
                "cube.brand.empty", "Brand can not be empty");
        else if(cube.getBrand().length() < 3)
            errors.rejectValue("brand", "cube.brand.too.small", "Brand can not be too small");
        else if(cube.getBrand().length() > 100)
            errors.rejectValue("brand", "cube.brand.too.long",
                "Brand can not be too long");

    }

    private void validateColorPatternField(Cube cube, Errors errors) {
        if(cube.getColorPattern() == null)
            errors.rejectValue("colorPattern",
            "cube.colorPattern.empty", "Color pattern can not be empty");
        else if(cube.getColorPattern().getName().length() < 4)
            errors.rejectValue("colorPattern", "cube.colorPattern.too.small", "ColorPattern can not be too small");
        else if(cube.getColorPattern().getName().length() > 25)
            errors.rejectValue("colorPattern", "cube.colorPattern.too.long",
                "ColorPattern can not be too long");
    }

    private void validateImageFileField(Cube cube, Errors errors) {
        if(cube.getImages().stream().anyMatch(i -> i.getBody().length > 1e5)) {
            errors.rejectValue("imageFiles",
                "cube.imageFiles.size.exceeded",
                "Some file exceeded the file size acceptable");
        }
        else if(cube.getImages().size() < 2) {
            errors.rejectValue("imageFiles",
                "cube.imageFiles.image.min.select",
                "Select at least two images");
        }
    }
}
