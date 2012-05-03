package com.ilifting.dao.pojos;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TPassengerTravelInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_PASSENGER_TRAVEL_INFO", schema = "dbo", catalog = "ilifting")
public class TPassengerTravelInfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 7528882195653517802L;
	private Integer id;
	private TUserinfo passenger;
	private TOwnerTravelInfo TOwnerTravelInfo;
	private String origin;
	private String destination;
	private Timestamp startTime;
	private Double timeLimit;
	private Double originLimit;
	private String ownerGender;
	private String status;
	private Long creditValue;
	private Timestamp endTime;
	private Set<TUserEvaluatePassenger> TUserEvaluatePassengers = new HashSet<TUserEvaluatePassenger>(
			0);

	// Constructors

	/** default constructor */
	public TPassengerTravelInfo() {
	}

	/** full constructor */
	public TPassengerTravelInfo(TUserinfo TUserinfo,
			TOwnerTravelInfo TOwnerTravelInfo, String origin,
			String destination, Timestamp startTime, Double timeLimit,
			Double originLimit, String ownerGender, String status,
			Long creditValue,
			Set<TUserEvaluatePassenger> TUserEvaluatePassengers) {
		this.passenger = TUserinfo;
		this.TOwnerTravelInfo = TOwnerTravelInfo;
		this.origin = origin;
		this.destination = destination;
		this.startTime = startTime;
		this.timeLimit = timeLimit;
		this.originLimit = originLimit;
		this.ownerGender = ownerGender;
		this.status = status;
		this.creditValue = creditValue;
		this.TUserEvaluatePassengers = TUserEvaluatePassengers;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PASSENGER_ID")
	public TUserinfo getPassenger() {
		return this.passenger;
	}

	public void setPassenger(TUserinfo TUserinfo) {
		this.passenger = TUserinfo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_TRAVEL_ID")
	public TOwnerTravelInfo getTOwnerTravelInfo() {
		return this.TOwnerTravelInfo;
	}

	public void setTOwnerTravelInfo(TOwnerTravelInfo TOwnerTravelInfo) {
		this.TOwnerTravelInfo = TOwnerTravelInfo;
	}

	@Column(name = "ORIGIN")
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "DESTINATION")
	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Column(name = "START_TIME", length = 23)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	@Column(name = "END_TIME", length = 23)
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Column(name = "TIME_LIMIT", precision = 53, scale = 0)
	public Double getTimeLimit() {
		return this.timeLimit;
	}

	public void setTimeLimit(Double timeLimit) {
		this.timeLimit = timeLimit;
	}

	@Column(name = "ORIGIN_LIMIT", precision = 53, scale = 0)
	public Double getOriginLimit() {
		return this.originLimit;
	}

	public void setOriginLimit(Double originLimit) {
		this.originLimit = originLimit;
	}

	@Column(name = "OWNER_GENDER")
	public String getOwnerGender() {
		return this.ownerGender;
	}

	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREDIT_VALUE", precision = 18, scale = 0)
	public Long getCreditValue() {
		return this.creditValue;
	}

	public void setCreditValue(Long creditValue) {
		this.creditValue = creditValue;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TPassengerTravelInfo")
	public Set<TUserEvaluatePassenger> getTUserEvaluatePassengers() {
		return this.TUserEvaluatePassengers;
	}

	public void setTUserEvaluatePassengers(
			Set<TUserEvaluatePassenger> TUserEvaluatePassengers) {
		this.TUserEvaluatePassengers = TUserEvaluatePassengers;
	}

}