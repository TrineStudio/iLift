package com.ilift.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;



/**
 * TUserinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USERINFO", schema = "dbo", catalog = "ilifting")
public class TUserinfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 3717484026350337376L;
	private Integer id;
	private String username;
	private String name;
	private String password;
	private String gender;
	private byte[] avatar;
	private String job;
	private String phone;
	private String carModel;
	private String college;
	private String passengerGender;
	private String ownerGender;
	private Double originLimit;
	private Double timeLimit;

	private String email;
	private String no;

	private Set<TUserPathinfo> TUserPathinfos = new HashSet<TUserPathinfo>(0);
	private Set<TUserEvaluatePassenger> TUserEvaluatePassengersForUserId = new HashSet<TUserEvaluatePassenger>(
			0);
	private Set<TUserEvaluatePassenger> TUserEvaluatePassengersForEvaluaterId = new HashSet<TUserEvaluatePassenger>(
			0);
	private Set<TUserEvaluateOwner> TUserEvaluateOwnersForEvaluaterId = new HashSet<TUserEvaluateOwner>(
			0);
	private Set<TUserEvaluateOwner> TUserEvaluateOwnersForUserId = new HashSet<TUserEvaluateOwner>(
			0);
	private Set<TOwnerTravelInfo> TOwnerTravelInfos = new HashSet<TOwnerTravelInfo>(
			0);
	private Set<TPassengerTravelInfo> TPassengerTravelInfos = new HashSet<TPassengerTravelInfo>(
			0);

	// Constructors

	/** default constructor */
	public TUserinfo() {
	}

	/** minimal constructor */
	public TUserinfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/** full constructor */
	public TUserinfo(String username, String password, String gender,
			byte[] avatar, String job, String phone, String carModel,
			String passengerGender, String ownerGender, Double originLimit,
			Double timeLimit, Set<TUserPathinfo> TUserPathinfos,
			Set<TUserEvaluatePassenger> TUserEvaluatePassengersForUserId,
			Set<TUserEvaluatePassenger> TUserEvaluatePassengersForEvaluaterId,
			Set<TUserEvaluateOwner> TUserEvaluateOwnersForEvaluaterId,
			Set<TUserEvaluateOwner> TUserEvaluateOwnersForUserId,
			Set<TOwnerTravelInfo> TOwnerTravelInfos,
			Set<TPassengerTravelInfo> TPassengerTravelInfos) {
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.avatar = avatar;
		this.job = job;
		this.phone = phone;
		this.carModel = carModel;
		this.passengerGender = passengerGender;
		this.ownerGender = ownerGender;
		this.originLimit = originLimit;
		this.timeLimit = timeLimit;
		this.TUserPathinfos = TUserPathinfos;
		this.TUserEvaluatePassengersForUserId = TUserEvaluatePassengersForUserId;
		this.TUserEvaluatePassengersForEvaluaterId = TUserEvaluatePassengersForEvaluaterId;
		this.TUserEvaluateOwnersForEvaluaterId = TUserEvaluateOwnersForEvaluaterId;
		this.TUserEvaluateOwnersForUserId = TUserEvaluateOwnersForUserId;
		this.TOwnerTravelInfos = TOwnerTravelInfos;
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

	@Column(name = "USERNAME", nullable = false)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "GENDER")
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "AVATAR")
	public byte[] getAvatar() {
		return this.avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	@Column(name = "JOB")
	public String getJob() {
		return this.job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "CAR_MODEL")
	public String getCarModel() {
		return this.carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	@Column(name = "passenger_gender")
	public String getPassengerGender() {
		return this.passengerGender;
	}

	public void setPassengerGender(String passengerGender) {
		this.passengerGender = passengerGender;
	}

	@Column(name = "owner_gender")
	public String getOwnerGender() {
		return this.ownerGender;
	}

	public void setOwnerGender(String ownerGender) {
		this.ownerGender = ownerGender;
	}

	@Column(name = "origin_limit", precision = 53, scale = 0)
	public Double getOriginLimit() {
		return this.originLimit;
	}

	public void setOriginLimit(Double originLimit) {
		this.originLimit = originLimit;
	}

	@Column(name = "time_limit", precision = 53, scale = 0)
	public Double getTimeLimit() {
		return this.timeLimit;
	}

	public void setTimeLimit(Double timeLimit) {
		this.timeLimit = timeLimit;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "COLLEGE")
	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "NO")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TUserinfo")
	public Set<TUserPathinfo> getTUserPathinfos() {
		return this.TUserPathinfos;
	}

	public void setTUserPathinfos(Set<TUserPathinfo> TUserPathinfos) {
		this.TUserPathinfos = TUserPathinfos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "passenger")
	public Set<TUserEvaluatePassenger> getTUserEvaluatePassengersForUserId() {
		return this.TUserEvaluatePassengersForUserId;
	}

	public void setTUserEvaluatePassengersForUserId(
			Set<TUserEvaluatePassenger> TUserEvaluatePassengersForUserId) {
		this.TUserEvaluatePassengersForUserId = TUserEvaluatePassengersForUserId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "evaluater")
	public Set<TUserEvaluatePassenger> getTUserEvaluatePassengersForEvaluaterId() {
		return this.TUserEvaluatePassengersForEvaluaterId;
	}

	public void setTUserEvaluatePassengersForEvaluaterId(
			Set<TUserEvaluatePassenger> TUserEvaluatePassengersForEvaluaterId) {
		this.TUserEvaluatePassengersForEvaluaterId = TUserEvaluatePassengersForEvaluaterId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "evaluater")
	public Set<TUserEvaluateOwner> getTUserEvaluateOwnersForEvaluaterId() {
		return this.TUserEvaluateOwnersForEvaluaterId;
	}

	public void setTUserEvaluateOwnersForEvaluaterId(
			Set<TUserEvaluateOwner> TUserEvaluateOwnersForEvaluaterId) {
		this.TUserEvaluateOwnersForEvaluaterId = TUserEvaluateOwnersForEvaluaterId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
	public Set<TUserEvaluateOwner> getTUserEvaluateOwnersForUserId() {
		return this.TUserEvaluateOwnersForUserId;
	}

	public void setTUserEvaluateOwnersForUserId(
			Set<TUserEvaluateOwner> TUserEvaluateOwnersForUserId) {
		this.TUserEvaluateOwnersForUserId = TUserEvaluateOwnersForUserId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
	public Set<TOwnerTravelInfo> getTOwnerTravelInfos() {
		return this.TOwnerTravelInfos;
	}

	public void setTOwnerTravelInfos(Set<TOwnerTravelInfo> TOwnerTravelInfos) {
		this.TOwnerTravelInfos = TOwnerTravelInfos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "passenger")
	public Set<TPassengerTravelInfo> getTPassengerTravelInfos() {
		return this.TPassengerTravelInfos;
	}

	public void setTPassengerTravelInfos(
			Set<TPassengerTravelInfo> TPassengerTravelInfos) {
		this.TPassengerTravelInfos = TPassengerTravelInfos;
	}

}