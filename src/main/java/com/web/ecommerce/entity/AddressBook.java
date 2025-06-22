package com.web.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "address_book")
public class AddressBook extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "full_name")
	private String fullName;

	private String phone;

	@Column(name = "ward_id")
	private Integer wardId;

	@Column(name = "ward_name")
	private String wardName;

	@Column(name = "district_id")
	private Integer districtId;

	@Column(name = "district_name")
	private String districtName;

	@Column(name = "city_id")
	private Integer cityId;

	@Column(name = "city_name")
	private String cityName;

	@Column(name = "full_address")
	private String fullAddress;

	@Column(name = "is_default")
	private int isDefault;

	private int status;
}
