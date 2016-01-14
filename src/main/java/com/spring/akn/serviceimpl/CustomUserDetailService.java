package com.spring.akn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;

@Service("CustomUserDetailService")
public class CustomUserDetailService  implements UserDetailsService{

	@Autowired
	UserRespositories userDaoImpl;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	    User user = userDaoImpl.findUserByUserName(username);
		if (user == null) {
			System.out.println("User not found");
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

}
