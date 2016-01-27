package com.spring.akn.controller.restcontroller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.akn.entities.frmApiDoc.FrmLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserAdd;
import com.spring.akn.entities.frmApiDoc.FrmUserChangePwd;
import com.spring.akn.entities.frmApiDoc.FrmUserLogin;
import com.spring.akn.entities.frmApiDoc.FrmUserUpdate;
import com.spring.akn.entities.user.User;
import com.spring.akn.serviceimpl.UserServicesImpl;
import com.spring.akn.services.UserServices;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
	@Autowired
	UserServices userServices;

	// login process
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody FrmUserLogin user) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();

		/*
		 * // for get data from json string JSONParser jsonParser = new
		 * JSONParser(); JSONObject jsonObject = (JSONObject)
		 * jsonParser.parse(data); // get a String from the JSON object String
		 * email = (String) jsonObject.get("email"); String password = (String)
		 * jsonObject.get("password");
		 */

		if (userServices.userLogin(user) != null) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "LOGIN SUCCESS");
			map.put("DATA", userServices.userLogin(user));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "LOGIN FAILD");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// register proccess
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> addUser(@RequestBody FrmUserAdd user) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (userServices.userRegister(user) == 0) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "FAILD TO INSET USERT");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "SUCCESS TO INSET USERT");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// User Status process

	@RequestMapping(value = "toggle/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Map<String, Object>> toggleStatus(@PathVariable("id") int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userServices.enableUser(id) == 0) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "FAILD TO UPDTE USERT STATUS");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "SUCCESS TO UPDTE USERT STATUS");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// change password process
	@RequestMapping(value = "/changepwd", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> changePassword(@RequestBody FrmUserChangePwd user)
			throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();

		/*
		 * // for get data from json string JSONParser jsonParser = new
		 * JSONParser(); JSONObject jsonObject = (JSONObject)
		 * jsonParser.parse(data); // get a String from the JSON object String
		 * oldpass = (String) jsonObject.get("oldpass"); String newpass =
		 * (String) jsonObject.get("newpass"); String uid=(String)
		 * jsonObject.get("id"); int id=Integer.parseInt(uid);
		 */

		if (userServices.changePassword(user) == 0) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "FAILD TO CHANGE PASSWORD");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "PASSWORD HAVE BEEN CHANGED");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// Update user infor process
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> updateUser(@RequestBody FrmUserUpdate user) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userServices.updateUser(user) == 0) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "FAILD TO UPDTE USERT INFORMATION");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}

		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "SUCCESS TO UPDTE USERT INFORMATION");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// list user
	@RequestMapping(value = "/{page}/{row}/{key}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listUser(@PathVariable("page") int page,@PathVariable("row") int row,
			@PathVariable("key") String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (userServices.listUser(key, page,row) == null) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "USER NOT FOUND");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("DATA", userServices.listUser(key, page,row));
		map.put("TOTALPAGE", userServices.getUserTotalPage(key, row));
		map.put("TOTALRECORD", userServices.getUserTotalRecords(key));
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "USER FOUND");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	// upload image
	/*
	 * @RequestMapping(value="/upload", method= RequestMethod.POST ) public
	 * ResponseEntity<Map<String,Object>> uploadImage( @RequestParam("file")
	 * MultipartFile file, HttpServletRequest request){ Map<String, Object> map
	 * = new HashMap<String, Object>(); if(!file.isEmpty()){ try{ UUID uuid =
	 * UUID.randomUUID(); String originalFilename = file.getOriginalFilename();
	 * String extension =
	 * originalFilename.substring(originalFilename.lastIndexOf(".")+1);
	 * 
	 * 
	 * 
	 * //for random file name String randowFileName=uuid+"."+extension;
	 * 
	 * String filename =randowFileName; byte[] bytes = file.getBytes();
	 * 
	 * // creating the directory to store file String savePath =
	 * request.getSession().getServletContext().getRealPath("/resources/images/"
	 * ); System.out.println(savePath); File path = new File(savePath);
	 * if(!path.exists()){ path.mkdir(); }
	 * 
	 * 
	 * // creating the file on server File serverFile = new File(savePath +
	 * File.separator + filename ); BufferedOutputStream stream = new
	 * BufferedOutputStream(new FileOutputStream(serverFile));
	 * stream.write(bytes); stream.close();
	 * 
	 * System.out.println(serverFile.getAbsolutePath()); System.out.println(
	 * "You are successfully uploaded file " + filename); map.put("MESSAGE",
	 * "UPLOAD IMAGE SUCCESS"); map.put("STATUS", HttpStatus.OK.value());
	 * map.put("IMAGE", request.getContextPath() + "/images/" + filename);
	 * return new ResponseEntity<Map<String,Object>> (map, HttpStatus.OK);
	 * }catch(Exception e){ System.out.println("You are failed to upload  => " +
	 * e.getMessage()); } }else{ System.err.println("File not found"); } return
	 * null; }
	 */
	// upload change image
	@RequestMapping(value = "/editupload", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> editUploadImage(@RequestParam("file") MultipartFile file,
			@RequestParam("id") int id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!file.isEmpty()) {
			try {
				UUID uuid = UUID.randomUUID();
				String originalFilename = file.getOriginalFilename();
				String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

				if (extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg")
						|| extension.equals("gif")) {
					if (userServices.getCurrentImage(id).equals("user.jpg")) {
						// for random file name
						String randowFileName = uuid + "." + extension;
						userServices.updateUserImage(randowFileName, id);
					}
					// for random file name
					String randowFileName = userServices.getCurrentImage(id);
					String filename = randowFileName;
					byte[] bytes = file.getBytes();

					// creating the directory to store file
					String savePath = request.getSession().getServletContext().getRealPath("/resources/images/user");
					System.out.println(savePath);
					File path = new File(savePath);
					if (!path.exists()) {
						path.mkdir();
					}

					// creating the file on server
					File serverFile = new File(savePath + File.separator + filename);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
					System.out.println(serverFile.getAbsolutePath());
					System.out.println("You are successfully uploaded file " + filename);
					map.put("MESSAGE", "UPLOAD IMAGE SUCCESS");
					map.put("STATUS", HttpStatus.OK.value());
					map.put("IMAGE", request.getContextPath() + "/images/user" + filename);
					return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
				} else {

				}
				map.put("MESSAGE", "UPLOAD IMAGE FAILED (extension image must .gif, .jpeg, .png, .jpg)");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

			} catch (Exception e) {
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		} else {
			System.err.println("File not found");
		}
		return null;
	}
	
	// web user login 
	@RequestMapping(value = "/weblogin", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> webLogin(@RequestBody FrmLogin frmLogin) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (userServices.findUserByUserName(frmLogin.getEmail())==null) {
			map.put("STATUS", HttpStatus.NOT_FOUND.value());
			map.put("MESSAGE", "USER NOT FOUND");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		map.put("DATA", userServices.findUserByUserName(frmLogin.getEmail()));
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE", "USER FOUND");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
   

}
