package com.web.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.dao.UserDao;
import com.web.ecommerce.entity.Users;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.service.UserService;

@Service("UsersService")
@Transactional(rollbackFor = Error.class)
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao usersDao;

	@Override
	public Users findOne(int id) throws Exception {
		return usersDao.findOne(id);
	}

	@Override
	public StoreProcedureListResult<Users> spGUsers(String keyword, int status, int role, Pagination pagination)
			throws Exception {
		return usersDao.spGUsers(keyword, status, role, pagination);
	}

	@Override
	public int deleteUsers(int id) throws Exception {
		return usersDao.deleteUsers(id);
	}

	@Override
	public Users findUsersByPhone(String phone) throws Exception {
		return usersDao.findUsersByPhone(phone);
	}

	@Override
	public Users findUsersByUsersName(String UsersName) throws Exception {
		return usersDao.findUsersByUsersName(UsersName);
	}

	@Override
	public void update(Users user) throws Exception {
		usersDao.update(user);

	}

	@Override
	public Users spUCreateUsers(String userName, String fullName, String email, String phone, String password,
			int gender, String birthday, int wardId, int districtId, int cityId, String fullAddress, int role) throws Exception {

		return usersDao.spUCreateUsers(userName, fullName, email, phone, password, gender, birthday, wardId, districtId,
				cityId, fullAddress, role);
	}

	@Override
	public Users findUsersByEmail(String email, int isGoogle) throws Exception {
		return usersDao.findUsersByEmail(email, isGoogle);
	}

	@Override
	public void findUsersByIdAndUpdateIsActive(int id, int isActive) {
		usersDao.findUsersByIdAndUpdateIsActive(id, isActive);

	}

	@Override
	public Users findUsersByUsersNameAndEmail(String UsersName, String email) throws Exception {
		return usersDao.findUsersByUsersNameAndEmail(UsersName, email);
	}

	@Override
	public List<Users> getAll() throws Exception {
		return usersDao.getAll();
	}

	@Override
	public void create(Users user) throws Exception {
		usersDao.create(user);
	}

	@Override
	public Users findUsersByUsersNameAndPassword(String usersName, String password) throws Exception {
		return usersDao.findUsersByUsersNameAndPassword(usersName, password);
	}

	@Override
	public List<Users> findByIds(List<Integer> ids) throws Exception {
		return usersDao.findByIds(ids);
	}

}
