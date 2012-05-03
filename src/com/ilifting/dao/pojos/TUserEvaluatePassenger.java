package com.ilifting.dao.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TUserEvaluatePassenger entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USER_EVALUATE_PASSENGER", schema = "dbo", catalog = "ilifting")
public class TUserEvaluatePassenger implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = -4868006755838707975L;
	private Integer id;
	private TUserinfo passenger;
	private TUserinfo evaluater;
	private TPassengerTravelInfo TPassengerTravelInfo;
	private Double value;

	// Constructors

	/** default constructor */
	public TUserEvaluatePassenger() {
	}

	/** full constructor */
	public TUserEvaluatePassenger(TUserinfo TUserinfoByUserId,
			TUserinfo TUserinfoByEvaluaterId,
			TPassengerTravelInfo TPassengerTravelInfo, Double value) {
		this.passenger = TUserinfoByUserId;
		this.evaluater = TUserinfoByEvaluaterId;
		this.TPassengerTravelInfo = TPassengerTravelInfo;
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
	public TUserinfo getPassenger() {
		return this.passenger;
	}

	public void setPassenger(TUserinfo passenger) {
		this.passenger = passenger;
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
	@JoinColumn(name = "PASSENGER_TRAVEL_ID")
	public TPassengerTravelInfo getTPassengerTravelInfo() {
		return this.TPassengerTravelInfo;
	}

	public void setTPassengerTravelInfo(
			TPassengerTravelInfo TPassengerTravelInfo) {
		this.TPassengerTravelInfo = TPassengerTravelInfo;
	}

	@Column(name = "VALUE", precision = 53, scale = 0)
	public Double getValue() {
		return this.value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

}