package com.cubershop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tb_color_pattern")
public class ColorPattern {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "name", unique = true, nullable = false, length = 127)
	private String name;

	@Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMPTZ")
	private Calendar createDate;

	@OneToMany(mappedBy = "colorPattern", cascade = CascadeType.ALL)
	private List<Cube> cubes;

	public ColorPattern(String name) {
		this.name = name;
		this.createDate = Calendar.getInstance();
		this.cubes =  new Vector<>();
		this.id = null;
	}

	public ColorPattern() {
		this("traditional");
	}

	public void addCube(final Cube cube) {
		if (!this.cubes.contains(cube))
			this.cubes.add(cube);
	}
}
