package com.web.ecommerce.dao;

import java.util.List;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.entity.Users;
import com.web.ecommerce.model.StoreProcedureListResult;

public interface UserDao {

	Users spUCreateUsers(String userName, String fullName, String email, String phone, String password, int gender,
			String birthday, int wardId, int districtId, int cityId, String fullAddress, int role) throws Exception;

	void create(Users user) throws Exception;

	Users findOne(int id) throws Exception;

	void update(Users user) throws Exception;

	Users spUUpdateUser(int id, String firstName, String lastName, String fullName, int gender, String email,
			int wardId, int cityId, int districtId, String fbUid, String ggUid, String appleUid, String phone)
			throws Exception;

	StoreProcedureListResult<Users> spGUsers(String keyword, int status, int role, Pagination pagination)
			throws Exception;

	int deleteUsers(int id) throws Exception;

	List<Users> getAll() throws Exception;

	void findUsersByIdAndUpdateIsActive(int id, int isActive);

	Users findUsersByPhone(String phone) throws Exception;

	Users findUsersByUsersName(String usersName) throws Exception;

	Users findUsersByUsersNameAndPassword(String usersName, String password) throws Exception;

	Users findUsersByUsersNameAndEmail(String UsersName, String email) throws Exception;

	Users findUsersByEmail(String email, int isGoogle) throws Exception;

	List<Users> findByIds(List<Integer> ids) throws Exception;

}
