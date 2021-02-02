package com.cubershop.context.validation;

import com.cubershop.context.entity.Cube;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
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

        if(cube.getPrice() == null)
            errors.rejectValue("price", "cube.price.empty", "Price can not be empty");
        else if(cube.getPrice().getValue() == 0)
            errors.rejectValue("price", "cube.price.zero", "Price can not be zero");
        else if(cube.getPrice().getValue() < 0)
            errors.rejectValue("price", "cube.price.negative",
                "Price can not be a negative number");

        if(cube.getType() == null || cube.getType().isEmpty())
            errors.rejectValue("type", "cube.type.empty", "Type can not be empty");
        else if(Stream.<String>of("2x2x2", "3x3x3", "4x4x4", "5x5x5", "big")
            .noneMatch(v -> v.equals(cube.getType())))
            errors.rejectValue("type", "cube.type.not.exist", "Type not acceptable");

         if(cube.getDescription().isEmpty() || cube.getDescription() == null)
             errors.rejectValue("description",
             "cube.description.empty", "Description can not be empty");
         else if(cube.getDescription().length() < 3)
             errors.rejectValue("description",
             "cube.description.too.small", "Description can not be too small");

        if(cube.getSize() < 0)
            errors.rejectValue("size", "cube.size.negative",
                "Size can not be a negative number");
        else if(cube.getSize() == 0)
            errors.rejectValue("size", "cube.size.zero",
                "Size can not be zero");
        else if(cube.getSize() > 200)
            errors.rejectValue("size", "cube.size.too.long",
            "Size can not be too long");

        if(cube.getBrand() == null)
            errors.rejectValue("brand",
                "cube.brand.empty", "Brand can not be empty");
        else if(cube.getBrand().length() < 3)
            errors.rejectValue("brand", "cube.brand.too.small", "Brand can not be too small");
        else if(cube.getBrand().length() > 100)
            errors.rejectValue("brand", "cube.brand.too.long", "Brand can not be too long");

        if(cube.getColorPattern() == null)
            errors.rejectValue("colorPattern",
                "cube.colorPattern.empty", "Color pattern can not be empty");
        else if(cube.getColorPattern().length() < 4)
            errors.rejectValue("colorPattern", "cube.colorPattern.too.small", "ColorPattern can not be too small");
        else if(cube.getColorPattern().length() > 25)
            errors.rejectValue("colorPattern", "cube.colorPattern.too.long", "ColorPattern can not be too long");

        if(cube.getImageFile().stream().anyMatch(v -> {
            try {
                return v.getBytes().length > 1e5;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        })) {
            errors.rejectValue("imageFile",
                "cube.imageFile.size.exceeded",
                "Some file exceeded the file size acceptable");
        }
        else if(cube.getImageFile().size() < 2) {
            errors.rejectValue("imageFile",
                "cube.imageFile.image.min.select",
                "Select at least two images");
        }
    }
}
