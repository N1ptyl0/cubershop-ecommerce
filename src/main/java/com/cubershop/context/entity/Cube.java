package com.cubershop.context.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public final class Cube implements Comparable<Cube> {

    private int size;
    private UUID id;
    private Price price;
    private String name, type, description;
    private UUID[] imageUUID;
    private List<MultipartFile> imageFile;
    private Brand brand;
    private ColorPattern colorPattern;

    public Cube() {
        this.id = UUID.randomUUID();
        this.size = 50;
        this.price = new Price();
        this.name = this.type = this.description = "";
        this.imageUUID = new UUID[]{UUID.randomUUID()};
        //noinspection unchecked
        this.imageFile = Collections.EMPTY_LIST;
        this.brand = Brand.FELLOW;
        this.colorPattern = ColorPattern.BEAUTY;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ColorPattern getColorPattern() {
        return colorPattern;
    }

    public void setColorPattern(ColorPattern colorPattern) {
        this.colorPattern = colorPattern;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public UUID[] getImageUUID() {
        return imageUUID;
    }

    public void setImageUUID(UUID[] imageUUID) {
        this.imageUUID = imageUUID;
    }

    public List<MultipartFile> getImageFile() {
        return imageFile;
    }

    public void setImageFile(List<MultipartFile> imageFile) {
        this.imageFile = imageFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(Objects.isNull(obj) || !(obj instanceof Cube)) return false;
        if(obj == this) return true;

        Cube cube = (Cube) obj;

        return this.id.equals(cube.getId()) && this.size == cube.getSize()
        && this.price.equals(cube.getPrice()) && this.name.equals(cube.getName())
        && this.type.equals(cube.getType()) && this.brand.equals(cube.getBrand())
        && this.colorPattern.equals(cube.getColorPattern())
        && Arrays.equals(this.imageUUID, cube.getImageUUID())
        && this.imageFile.equals(cube.getImageFile());
    }

    @Override
    public int compareTo(Cube o) {
        if(Objects.isNull(o)) throw new NullPointerException("The specified object is null");
        if(!(o instanceof Cube))
            throw new ClassCastException("The specified object's type prevents it from being compared to this object");

        return this.id.compareTo(o.getId());
    }
}
