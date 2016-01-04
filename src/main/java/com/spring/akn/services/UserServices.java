package com.spring.akn.services;

import com.spring.akn.entities.user.User;

public interface UserServices {
	public int userRegister(User user);
	public User userLogin(String email,String password);
	public int enableUser(int id);
	public int updateUser(User user);
	public User getUser(int id);
	public int changePassword( String oldpass,String newpass,int id);

}
