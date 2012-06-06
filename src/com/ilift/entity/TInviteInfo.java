package com.ilift.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * TInviteInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_INVITE_INFO", schema = "dbo", catalog = "ilifting")
public class TInviteInfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 3864817841224179862L;
	private Integer id;
	private TUserinfo passenger;
	private TPassengerTravelInfo TPassengerTravelInfo;
	private TUserinfo owner;
	private TOwnerTravelInfo TOwnerTravelInfo;
	private Timestamp time;
	private String status;
	private String type;
	private Boolean reponseChecked;

	// Constructors

	/** default constructor */
	public TInviteInfo() {
	}

	/** full constructor */
	public TInviteInfo(TUserinfo TUserinfoByPassengerTravelId,
			TPassengerTravelInfo TPassengerTravelInfo,
			TUserinfo TUserinfoByOwnerId, TOwnerTravelInfo TOwnerTravelInfo,
			Timestamp time, String status, String type) {
		this.passenger = TUserinfoByPassengerTravelId;
		this.TPassengerTravelInfo = TPassengerTravelInfo;
		this.owner = TUserinfoByOwnerId;
		this.TOwnerTravelInfo = TOwnerTravelInfo;
		this.time = time;
		this.status = status;
		this.type = type;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	@Column(name = "RESPONSE_CHECKED")
	public Boolean getReponseChecked() {
		return reponseChecked;
	}

	public void setReponseChecked(Boolean reponseChecked) {
		this.reponseChecked = reponseChecked;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSENGER_ID")
	public TUserinfo getPassenger() {
		return this.passenger;
	}

	public void setPassenger(TUserinfo passenger) {
		this.passenger = passenger;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ID")
	public TUserinfo getOwner() {
		return this.owner;
	}

	public void setOwner(TUserinfo owner) {
		this.owner = owner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_TRAVEL_ID")
	public TOwnerTravelInfo getTOwnerTravelInfo() {
		return this.TOwnerTravelInfo;
	}

	public void setTOwnerTravelInfo(TOwnerTravelInfo TOwnerTravelInfo) {
		this.TOwnerTravelInfo = TOwnerTravelInfo;
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

	@Column(name = "TYPE")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}