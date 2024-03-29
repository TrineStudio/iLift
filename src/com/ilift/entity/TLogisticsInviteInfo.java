package com.ilift.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * TLogisticsInviteInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_LOGISTICS_INVITE_INFO", schema = "dbo", catalog = "ilifting")
public class TLogisticsInviteInfo implements java.io.Serializable {
	
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6486804966438680443L;
	private Integer id;
	private TUserinfo user;
	private TLogisticsInfo TLogisticsInfo;
	private TUserinfo sender;
	private Integer travelId;
	private String senderType;
	private Timestamp time;
	private String status;
	private Boolean responseChecked;

	// Constructors

	/** default constructor */
	public TLogisticsInviteInfo() {
	}

	/** full constructor */
	public TLogisticsInviteInfo(TUserinfo user, TLogisticsInfo TLogisticsInfo,
			TUserinfo sender, Integer travelId, String senderType,
			Timestamp time, String status, Boolean responseChecked) {
		this.user = user;
		this.TLogisticsInfo = TLogisticsInfo;
		this.sender = sender;
		this.travelId = travelId;
		this.senderType = senderType;
		this.time = time;
		this.status = status;
		this.responseChecked = responseChecked;
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
	public TUserinfo getUser() {
		return this.user;
	}

	public void setUser(TUserinfo user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOGISTIC_ID")
	public TLogisticsInfo getTLogisticsInfo() {
		return this.TLogisticsInfo;
	}

	public void setTLogisticsInfo(TLogisticsInfo TLogisticsInfo) {
		this.TLogisticsInfo = TLogisticsInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER_ID")
	public TUserinfo getSender() {
		return this.sender;
	}

	public void setSender(TUserinfo sender) {
		this.sender = sender;
	}

	@Column(name = "TRAVEL_ID")
	public Integer getTravelId() {
		return this.travelId;
	}

	public void setTravelId(Integer travelId) {
		this.travelId = travelId;
	}

	@Column(name = "SENDER_TYPE")
	public String getSenderType() {
		return this.senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	@Column(name = "TIME", length = 23)
	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "RESPONSE_CHECKED")
	public Boolean getResponseChecked() {
		return this.responseChecked;
	}

	public void setResponseChecked(Boolean responseChecked) {
		this.responseChecked = responseChecked;
	}

}