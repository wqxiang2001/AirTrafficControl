package com.binaryfountain.airtraffic.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "aircraft")
public class AirCraft implements Serializable, Persistable<Long> {

	private @Transient boolean isNew = true;

	@Override
	public boolean isNew() {
		return isNew;
	}

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1118011493262219238L;

	public enum CraftType {
		Emergency, VIP, Passenger, Cargo;
	}

	public enum CraftSize {
		LARGE, SMALL;
	}

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// @GeneratedValue(strategy = GenerationType.IDENTITY, generator =
	// "sequenceGenerator")
	// @SequenceGenerator(name = "sequenceGenerator", sequenceName =
	// "AIR_CRAFT_SEQ")

	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "TYPE", nullable = false)
	private CraftType type;

	@Column(name = "SIZE", nullable = false)
	private CraftSize size;

	AirCraft() {
	}

	public AirCraft(CraftType type, CraftSize size) {
		this.type = type;
		this.size = size;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CraftType getType() {
		return type;
	}

	public void setType(CraftType type) {
		this.type = type;
	}

	public CraftSize getSize() {
		return size;
	}

	public void setSize(CraftSize size) {
		this.size = size;
	}

}