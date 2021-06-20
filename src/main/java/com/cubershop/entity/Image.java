package com.cubershop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;
import java.util.Calendar;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_image")
@Builder
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private UUID id;

	@Column(name = "body", nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private byte[] body;

	@Column(name = "create_date", nullable = false, columnDefinition = "TIMESTAMPTZ")
	private Calendar createDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "cube_id", referencedColumnName = "id",
		foreignKey = @ForeignKey(name = "fk_image_cube"), nullable = false
	)
	private Cube cube;
}