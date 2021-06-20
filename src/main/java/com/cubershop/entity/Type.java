package com.cubershop.entity;

import com.cubershop.entity.converter.OptionTypeConverter;
import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_type")
@Builder
@AllArgsConstructor
public class Type {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "name", unique = true, nullable = false, length = 10)
	@Convert(converter = OptionTypeConverter.class)
	private Name name;

	@Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMPTZ")
	private Calendar createDate;

	@OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
	private List<Cube> cubes;

	public static enum Name {
		_2x2x2, _3x3x3, _4x4x4, _5x5x5, BIG
	}

	public Type(Name name) {
		 this.name = name;
		 this.cubes = new Vector<>();
		 this.createDate = Calendar.getInstance();
		 this.id = null;
	}

	public Type() {
		this(Name.BIG);
	}

	public void addCube(final Cube cube) {
		if (!this.cubes.contains(cube))
			this.cubes.add(cube);
	}
}
