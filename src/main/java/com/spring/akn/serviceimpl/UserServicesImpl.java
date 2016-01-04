package com.spring.akn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;
import com.spring.akn.services.UserServices;

@Service
public class UserServicesImpl implements UserServices{
    @Autowired
    UserRespositories userRespositories;
    

	public int userRegister(User user) {	
		return userRespositories.userRegister(user);
	}


	public User userLogin(String email, String password) {
		return userRespositories.userLogin(email, password);
	}


	public int enableUser(int id) {
		return userRespositories.enableUser(id);
	}

	
	public int updateUser(User user) {
		return userRespositories.updateUser(user);
	}

	
	public User getUser(int id) {
		return userRespositories.getUser(id);
	}

	
	public int changePassword(String oldpass, String newpass, int id) {
		if(userRespositories.getCurrentPass(id).getPassword()!=null){
			if(oldpass.equals(userRespositories.getCurrentPass(id).getPassword())){
				return userRespositories.changePassword(newpass, id);
			}
		}
		return 0;
	}
	
}
