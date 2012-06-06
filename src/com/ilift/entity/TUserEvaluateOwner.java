package com.ilift.entity;

import javax.persistence.*;

/**
 * TUserEvaluateOwner entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USER_EVALUATE_OWNER", schema = "dbo", catalog = "ilifting")
public class TUserEvaluateOwner implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 109462449357546548L;
	private Integer id;
	private TUserinfo owner;
	private TUserinfo evaluater;
	private TOwnerTravelInfo TOwnerTravelInfo;
	private Double value;

	// Constructors

	/** default constructor */
	public TUserEvaluateOwner() {
	}

	/** full constructor */
	public TUserEvaluateOwner(TUserinfo TUserinfoByUserId,
			TUserinfo TUserinfoByEvaluaterId,
			TOwnerTravelInfo TOwnerTravelInfo, Double value) {
		this.owner = TUserinfoByUserId;
		this.evaluater = TUserinfoByEvaluaterId;
		this.TOwnerTravelInfo = TOwnerTravelInfo;
		this.value = value;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public TUserinfo getOwner() {
		return this.owner;
	}

	public void setOwner(TUserinfo owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVALUATER_ID")
	public TUserinfo getEvaluater() {
		return this.evaluater;
	}

	public void setEvaluater(TUserinfo evaluater) {
		this.evaluater = evaluater;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_TRAVEL_ID")
	public TOwnerTravelInfo getTOwnerTravelInfo() {
		return this.TOwnerTravelInfo;
	}

	public void setTOwnerTravelInfo(TOwnerTravelInfo TOwnerTravelInfo) {
		this.TOwnerTravelInfo = TOwnerTravelInfo;
	}

	@Column(name = "VALUE", precision = 53, scale = 0)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}