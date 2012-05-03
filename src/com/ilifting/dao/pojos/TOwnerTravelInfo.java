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
 * TOwnerTravelInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OWNER_TRAVEL_INFO", schema = "dbo", catalog = "ilifting")
public class TOwnerTravelInfo implements java.io.Serializable {

	public enum status {
		on, success, fail
	}

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 387853978064733667L;
	private Integer id;
	private TUserinfo owner;
	private Integer passengerNum;
	private Integer curNum;
	private String origin;
	private String destination;
	private Timestamp startTime;
	private Double timeLimit;
	private Double originLimit;
	private String passengerGender;
	private String status;
	private Double creditValue;
	private Timestamp endTime;

	private Set<TUserEvaluateOwner> TUserEvaluateOwners = new HashSet<TUserEvaluateOwner>(
			0);
	private Set<TPassengerTravelInfo> TPassengerTravelInfos = new HashSet<TPassengerTravelInfo>(
			0);

	// Constructors

	/** default constructor */
	public TOwnerTravelInfo() {
	}

	/** full constructor */
	public TOwnerTravelInfo(TUserinfo TUserinfo, Integer passengerNum,
			Integer curNum, String origin, String destination,
			Timestamp startTime, Double timeLimit, Double originLimit,
			String passengerGender, String status, Double creditValue,
			Set<TUserEvaluateOwner> TUserEvaluateOwners,
			Set<TPassengerTravelInfo> TPassengerTravelInfos) {
		this.owner = TUserinfo;
		this.passengerNum = passengerNum;
		this.curNum = curNum;
		this.origin = origin;
		this.destination = destination;
		this.startTime = startTime;
		this.timeLimit = timeLimit;
		this.originLimit = originLimit;
		this.passengerGender = passengerGender;
		this.status = status;
		this.creditValue = creditValue;
		this.TUserEvaluateOwners = TUserEvaluateOwners;
		this.TPassengerTravelInfos = TPassengerTravelInfos;
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
	@JoinColumn(name = "OWNER_ID")
	public TUserinfo getOwner() {
		return this.owner;
	}

	public void setOwner(TUserinfo TUserinfo) {
		this.owner = TUserinfo;
	}

	@Column(name = "PASSENGER_NUM")
	public Integer getPassengerNum() {
		return this.passengerNum;
	}

	public void setPassengerNum(Integer passengerNum) {
		this.passengerNum = passengerNum;
	}

	@Column(name = "CUR_NUM")
	public Integer getCurNum() {
		return this.curNum;
	}

	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
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

	public Timestamp getEndTime() {
		return endTime;
	}

	@Column(name = "END_TIME", length = 23)
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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

	@Column(name = "PASSENGER_GENDER")
	public String getPassengerGender() {
		return this.passengerGender;
	}

	public void setPassengerGender(String passengerGender) {
		this.passengerGender = passengerGender;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREDIT_VALUE", precision = 53, scale = 0)
	public Double getCreditValue() {
		return this.creditValue;
	}

	public void setCreditValue(Double creditValue) {
		this.creditValue = creditValue;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOwnerTravelInfo")
	public Set<TUserEvaluateOwner> getTUserEvaluateOwners() {
		return this.TUserEvaluateOwners;
	}

	public void setTUserEvaluateOwners(
			Set<TUserEvaluateOwner> TUserEvaluateOwners) {
		this.TUserEvaluateOwners = TUserEvaluateOwners;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOwnerTravelInfo")
	public Set<TPassengerTravelInfo> getTPassengerTravelInfos() {
		return this.TPassengerTravelInfos;
	}

	public void setTPassengerTravelInfos(
			Set<TPassengerTravelInfo> TPassengerTravelInfos) {
		this.TPassengerTravelInfos = TPassengerTravelInfos;
	}

}