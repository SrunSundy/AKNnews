package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.user.User;

public interface UserRespositories {
	public int userRegister(User user);
	public User userLogin(String email,String password);
	public int enableUser(int id);
	public int updateUser(User user);
	public User getUser(int id);
	public int changePassword(String newpass,String oldpass,int id);
    public List<User> listUser(String key,int page);
    public String getCurrentImage(int id);
}
