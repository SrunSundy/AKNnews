package com.spring.akn.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.akn.entities.frmApiDoc.FrmUser;
import com.spring.akn.entities.frmApiDoc.FrmUserAdd;
import com.spring.akn.entities.frmApiDoc.FrmUserChangePwd;
import com.spring.akn.entities.frmApiDoc.FrmUserLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserUpdate;
import com.spring.akn.entities.user.User;
import com.spring.akn.repositories.UserRespositories;
import com.spring.akn.services.UserServices;

@Service
public class UserServicesImpl implements UserServices{
    @Autowired
    UserRespositories userRespositories;
    

	public int userRegister(FrmUserAdd user) {	
		return userRespositories.userRegister(user);
	}


	public FrmUser userLogin(FrmUserLogin user) {
		return userRespositories.userLogin(user);
	}

/*
	public int enableUser(int id) {
		return userRespositories.enableUser(id);
	}

   */	
	
	public int updateUser(FrmUserUpdate user) {
		return userRespositories.updateUser(user);
	}

	/*
	public User getUser(int id) {
		return userRespositories.getUser(id);
	}*/

	
	public int changePassword(FrmUserChangePwd user) {
		return userRespositories.changePassword(user);
	}

/*
	@Override
	public List<User> listUser(String key, int page,int row) {
		return userRespositories.listUser(key, page,row);
	}


	@Override
	public String getCurrentImage(int id) {
		return userRespositories.getCurrentImage(id);
	}


	@Override
	public int updateUserImage(String imagename, int id) {
		return userRespositories.updateUserImage(imagename, id);
	}*/


	@Override
	public User findUserByUserName(String username) {
		return userRespositories.findUserByUserName(username);
	}





	/*@Override
	public int getUserTotalPage(String key, int row) {
		
		return userRespositories.getUserTotalPage(key, row);
	}


	@Override
	public int getUserTotalRecords(String key) {
	
		return userRespositories.getUserTotalRecords(key);
	}


	@Override
	public List<User> listNewUser() {
		return userRespositories.listNewUser();
	}


	@Override
	public List<User> listNewAdmin() {
		return userRespositories.listNewAdmin();
	}*/
}
