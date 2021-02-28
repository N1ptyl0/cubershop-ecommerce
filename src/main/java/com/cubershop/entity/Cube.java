package com.cubershop.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Cube {

    private int size;
    private UUID id;
    private Price price;
    private String name, type, description, brand, colorPattern;
    private UUID[] imageUUID;
    private List<MultipartFile> imageFiles;
    private Installment installment;
    private int quantity;
    private boolean stock, magnetic;

    public Cube() {
        this.id = UUID.randomUUID();
        this.size = 50;
        this.price = new Price();
        this.name = this.type = this.description = this.brand = this.colorPattern = "";
        this.imageUUID = new UUID[]{UUID.randomUUID(), UUID.randomUUID()};
        //noinspection unchecked
        this.imageFiles = Collections.EMPTY_LIST;
        this.installment = new Installment(this.price, 3);
    }
}
