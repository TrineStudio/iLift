package com.ilift.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * TGoodsInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_GOODS_INFO", schema = "dbo", catalog = "ilifting")
public class TGoodsInfo implements java.io.Serializable {

	// Fields

	/**
     * 
     */
	private static final long serialVersionUID = 8621871448239742435L;
	private Integer id;
	private TUserinfo TUserinfo;
	private String name;
	private Double quantity;
	private String description;
	private byte[] avatar;
	private Boolean show;
	private String status;
	private double money;
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	private Set<TLogisticsInfo> TLogisticsInfos = new HashSet<TLogisticsInfo>(0);

	// Constructors

	/** default constructor */
	public TGoodsInfo() {
	}

	/** full constructor */
	public TGoodsInfo(TUserinfo TUserinfo, String name, Double quantity,
			String description, byte[] avatar, Boolean show,
			Set<TLogisticsInfo> TLogisticsInfos) {
		this.TUserinfo = TUserinfo;
		this.name = name;
		this.quantity = quantity;
		this.description = description;
		this.avatar = avatar;
		this.show = show;
		this.TLogisticsInfos = TLogisticsInfos;
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
	public TUserinfo getTUserinfo() {
		return this.TUserinfo;
	}

	public void setTUserinfo(TUserinfo TUserinfo) {
		this.TUserinfo = TUserinfo;
	}

	@Column(name = "NAME")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "QUANTITY", precision = 53, scale = 0)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "AVATAR")
	public byte[] getAvatar() {
		return this.avatar;
	}

	public void setAvatar(byte[] avatar2) {
		this.avatar = avatar2;
	}

	@Column(name = "SHOW")
	public Boolean getShow() {
		return this.show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TGoodsInfo")
	public Set<TLogisticsInfo> getTLogisticsInfos() {
		return this.TLogisticsInfos;
	}

	public void setTLogisticsInfos(Set<TLogisticsInfo> TLogisticsInfos) {
		this.TLogisticsInfos = TLogisticsInfos;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}