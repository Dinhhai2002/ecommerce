package com.web.ecommerce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "full_name")
	private String fullName;

	private String email;

	@Column(name = "avatar_id")
	private int avatarId;

	@Column(name = "avatar_url")
	private String avatarUrl;

	private String phone;

	private String password;

	private int gender;

	private String birthday;

	@Column(name = "access_token")
	private String accessToken;

	private int otp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "otp_created_at")
	@CreationTimestamp
	private Date otpCreatedAt;

	@Column(name = "is_confirm_otp")
	private int isConfirmOtp;

	@Column(name = "is_active")
	private int isActive;

}
