package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.common.utils.Utils;
import com.web.ecommerce.entity.Users;

import lombok.Data;

@Data
public class UserResponse {
	private int id;

	@JsonProperty("user_name")
	private String userName;

	@JsonProperty("full_name")
	private String fullName;

	private String email;

	private int avatarId;

	@JsonProperty("avatar_url")
	private String avatarUrl;

	private String phone;

	private int gender;

	private String birthday;

	@JsonProperty("ward_id")
	private int wardId;

	@JsonProperty("city_id")
	private int cityId;

	@JsonProperty("district_id")
	private int districtId;

	@JsonProperty("full_address")
	private String fullAddress;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("is_login")
	private int isLogin;

	private int role;

	@JsonProperty("is_active")
	private int isActive;

	@JsonProperty("is_google")
	private int isGoogle;

	@JsonProperty("point_promotion")
	private long point;
	
	@JsonProperty("cart_id")
	private int cartId;

	public UserResponse() {

	}

	public UserResponse(Users entity) {
		this.id = entity.getId();
		this.userName = entity.getUserName();
		this.fullName = entity.getFullName();
		this.gender = entity.getGender();
		this.birthday = entity.getBirthday() != null ? Utils.getDateStringSupplier(entity.getBirthday()) : "";
		this.email = entity.getEmail();
		this.avatarId = entity.getAvatarId();
		this.avatarUrl = entity.getAvatarUrl();
		this.wardId = entity.getWardId();
		this.cityId = entity.getCityId();
		this.districtId = entity.getDistrictId();
		this.phone = entity.getPhone();
		this.fullAddress = entity.getFullAddress();
		this.accessToken = entity.getAccessToken();
		this.isLogin = entity.getIsLogin();
		this.isActive = entity.getIsActive();
		this.role = entity.getRole();
		this.isGoogle = entity.getIsGoogle();
	}
	
	public UserResponse(Users entity,int cartId) {
		this.id = entity.getId();
		this.userName = entity.getUserName();
		this.fullName = entity.getFullName();
		this.gender = entity.getGender();
		this.birthday = entity.getBirthday() != null ? Utils.getDateStringSupplier(entity.getBirthday()) : "";
		this.email = entity.getEmail();
		this.avatarId = entity.getAvatarId();
		this.avatarUrl = entity.getAvatarUrl();
		this.wardId = entity.getWardId();
		this.cityId = entity.getCityId();
		this.districtId = entity.getDistrictId();
		this.phone = entity.getPhone();
		this.fullAddress = entity.getFullAddress();
		this.accessToken = entity.getAccessToken();
		this.isLogin = entity.getIsLogin();
		this.isActive = entity.getIsActive();
		this.role = entity.getRole();
		this.isGoogle = entity.getIsGoogle();
		this.cartId = cartId;
	}



	public List<UserResponse> mapToList(List<Users> entities) {
		return entities.stream().map(x -> new UserResponse(x)).collect(Collectors.toList());
	}

}