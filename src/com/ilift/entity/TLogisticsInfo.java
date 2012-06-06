package com.ilift.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * TLogisticsInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_LOGISTICS_INFO", schema = "dbo", catalog = "ilifting")
public class TLogisticsInfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 7424750182767015586L;
	private Integer id;
	private TGoodsInfo TGoodsInfo;
	private Integer travelId;
	private Timestamp startTime;
	private Timestamp endTime;
	private String origin;
	private String destination;
	private Double timeLimit;
	private Double originLimit;
	private String status;
	private Double money;
	private String travelType;

	// Constructors

	/** default constructor */
	public TLogisticsInfo() {
	}

	/** full constructor */
	public TLogisticsInfo(TGoodsInfo TGoodsInfo, Integer travelId,
			Timestamp startTime, Timestamp endTime, String origin,
			String destination, Double timeLimit, Double originLimit,
			String status, Double money) {
		this.TGoodsInfo = TGoodsInfo;
		this.travelId = travelId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.origin = origin;
		this.destination = destination;
		this.timeLimit = timeLimit;
		this.originLimit = originLimit;
		this.status = status;
		this.money = money;
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
	@JoinColumn(name = "GOODS_ID")
	public TGoodsInfo getTGoodsInfo() {
		return this.TGoodsInfo;
	}

	public void setTGoodsInfo(TGoodsInfo TGoodsInfo) {
		this.TGoodsInfo = TGoodsInfo;
	}

	@Column(name = "START_TIME", length = 23)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 23)
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
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

	@Column(name = "STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "MONEY", precision = 53, scale = 0)
	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Column(name = "TRAVEL_ID")
	public Integer getTravelId() {
		return travelId;
	}

	public void setTravelId(Integer travelId) {
		this.travelId = travelId;
	}

	@Column(name = "TRAVEL_TYPE")
	public String getTravelType() {
		return travelType;
	}

	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}

}