package com.spring.akn.repositories;

import java.util.List;

import com.spring.akn.entities.frmApiDoc.FrmUserAdd;
import com.spring.akn.entities.frmApiDoc.FrmUserChangePwd;
import com.spring.akn.entities.frmApiDoc.FrmUserLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserUpdate;
import com.spring.akn.entities.user.User;

public interface UserRespositories {
	public int userRegister(FrmUserAdd user);
	public User userLogin(FrmUserLogin user);
	public int enableUser(int id);
	public int updateUser(FrmUserUpdate user);
	public User getUser(int id);
	public int changePassword(FrmUserChangePwd user);
    public List<User> listUser(String key,int page,int row);
    public String getCurrentImage(int id);
    public User findUserByUserName(String username);
    public int updateUserImage(String imagename,int id);
	public int getUserTotalPage(String key,int row); 
	public int getUserTotalRecords(String key);
    public List<User> listNewUser();
    public List<User> listNewAdmin();
}
