package com.cubershop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Builder
@NoArgsConstructor
@Table(name = "tb_cube")
@AllArgsConstructor
public class Cube {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "price", nullable = false, precision = 2)
    private double price;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "type_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_type_cube"), nullable = false
    )
    private Type type;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "color_pattern_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_colorPattern_cube"), nullable = false
    )
    private ColorPattern colorPattern;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "brand", nullable = false, length = 32)
    private String brand;

    @Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Calendar createDate;

    @Column(name = "last_update", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Calendar lastUpdate;

    @Column(name = "size", nullable = false, columnDefinition = "SMALLINT")
    private int size;

    @OneToMany(mappedBy = "cube", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Image> images;

    @Transient
    private Installment installment;

    @Column(name = "quantity", nullable = false, columnDefinition = "SMALLINT")
    private int quantity;

    @Column(name = "magnetic", nullable = false)
    private boolean magnetic;

    @Column(name = "stock", nullable = false)
    private boolean stock;
}
