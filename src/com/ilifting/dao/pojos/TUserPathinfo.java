package com.ilifting.dao.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * TUserPathinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USER_PATHINFO", schema = "dbo", catalog = "ilifting")
public class TUserPathinfo implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -754597235898631797L;
	// Fields

	private Integer id;
	private TUserinfo TUserinfo;
	private String origin;
	private String destination;
	private Timestamp startTime;

	private String type;

	// Constructors

	/** default constructor */
	public TUserPathinfo() {
	}

	/** minimal constructor */
	public TUserPathinfo(TUserinfo TUserinfo) {
		this.TUserinfo = TUserinfo;
	}

	/** full constructor */
	public TUserPathinfo(TUserinfo TUserinfo, String origin,
			String destination, Timestamp startTime) {
		this.TUserinfo = TUserinfo;
		this.origin = origin;
		this.destination = destination;
		this.startTime = startTime;
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
	@JoinColumn(name = "USER_ID", nullable = false)
	public TUserinfo getTUserinfo() {
		return this.TUserinfo;
	}

	public void setTUserinfo(TUserinfo TUserinfo) {
		this.TUserinfo = TUserinfo;
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

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "START_TIME", length = 23)
	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

}