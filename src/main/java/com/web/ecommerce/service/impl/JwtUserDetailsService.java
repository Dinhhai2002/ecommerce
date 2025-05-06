package com.web.ecommerce.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.ecommerce.dao.UserDao;
import com.web.ecommerce.dao.UserRoleDao;
import com.web.ecommerce.dao.RoleDao;
import com.web.ecommerce.entity.Users;

@Service("UserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    RoleDao roleDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = null;
		try {
			user = userDao.findUsersByUsersName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

        // Lấy danh sách role của user
        List<Integer> roleIds = userRoleDao.findRoleIdsByUserId(user.getId());
        List<String> roleNames = roleDao.findRoleNamesByIds(roleIds);
        Collection<? extends GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

		return new User(user.getUserName(), user.getPassword(), authorities);
	}
}