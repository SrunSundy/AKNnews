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

import com.spring.akn.entities.user.User;
import com.spring.akn.services.UserServices;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
	@Autowired
	UserServices userServices;

	
	//login process
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> login(@RequestBody String data) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		// for get data from json string
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
		// get a String from the JSON object
		String email = (String) jsonObject.get("email");
		String password = (String) jsonObject.get("password");
		if (userServices.userLogin(email, password) != null) {
			map.put("STATUS", HttpStatus.FOUND.value());
			map.put("MESSAGE", "LOGIN SUCCESS");
			map.put("DATA",userServices.userLogin(email, password));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("STATUS", HttpStatus.NOT_FOUND.value());
		map.put("MESSAGE", "LOGIN FAILD");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	//register proccess
	@RequestMapping(value="/",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addUser(@RequestBody User user) {
		
		Map<String,Object> map=new HashMap<String, Object>();
		if( userServices.userRegister(user)== 0){
		    map.put("STATUS", HttpStatus.NOT_FOUND.value());
		    map.put("MESSAGE","FAILD TO INSET USERT");
		    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE","SUCCESS TO INSET USERT");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	//User Status process
	@RequestMapping(value="toggle/{id}",method=RequestMethod.PATCH)
	public ResponseEntity<Map<String,Object>> toggleStatus(@PathVariable("id") int id) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(userServices.enableUser(id)==0){
		    map.put("STATUS", HttpStatus.NOT_FOUND.value());
		    map.put("MESSAGE","FAILD TO UPDTE USERT STATUS");
		    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
	
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE","SUCCESS TO UPDTE USERT STATUS");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
    
	//change password process
	@RequestMapping(value="/changepwd",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> changePassword(@RequestBody String data) throws ParseException {
		Map<String,Object> map=new HashMap<String, Object>();

		// for get data from json string
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
		// get a String from the JSON object
		String oldpass = (String) jsonObject.get("oldpass");
		String newpass = (String) jsonObject.get("newpass");
		String uid=(String) jsonObject.get("id");
		int id=Integer.parseInt(uid);
		
		
		if(userServices.changePassword(oldpass, newpass, id)==0){
		    map.put("STATUS", HttpStatus.NOT_FOUND.value());
		    map.put("MESSAGE","FAILD TO CHANGE PASSWORD");
		    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
	
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE","PASSWORD HAVE BEEN CHANGED");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	//Update user infor process
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public ResponseEntity<Map<String,Object>> updateUser(@RequestBody User user) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(userServices.updateUser(user)==0){
		    map.put("STATUS", HttpStatus.NOT_FOUND.value());
		    map.put("MESSAGE","FAILD TO UPDTE USERT INFORMATION");
		    return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}
	
		map.put("STATUS", HttpStatus.FOUND.value());
		map.put("MESSAGE","SUCCESS TO UPDTE USERT INFORMATION");
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
	
	//upload image
	@RequestMapping(value="/upload", method= RequestMethod.POST )
	public ResponseEntity<Map<String,Object>> uploadImage( @RequestParam("file") MultipartFile file, HttpServletRequest request){
		Map<String, Object> map  = new HashMap<String, Object>();
		if(!file.isEmpty()){
			try{
				UUID uuid = UUID.randomUUID();
	            String originalFilename = file.getOriginalFilename(); 
	            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
	            String randomUUIDFileName = uuid.toString();
	            
	            String filename = originalFilename;
				byte[] bytes = file.getBytes();

				// creating the directory to store file
				String savePath = request.getSession().getServletContext().getRealPath("/resources/images/");
				System.out.println(savePath);
				File path = new File(savePath);
				if(!path.exists()){
					path.mkdir();
				}
				
				// creating the file on server
				File serverFile = new File(savePath + File.separator + filename );
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				System.out.println(serverFile.getAbsolutePath());
				System.out.println("You are successfully uploaded file " + filename);
				map.put("MESSAGE","UPLOAD IMAGE SUCCESS");
				map.put("STATUS", HttpStatus.OK.value());
				map.put("IMAGE", request.getContextPath() + "/images/" + filename);
				return new ResponseEntity<Map<String,Object>>
									(map, HttpStatus.OK);
			}catch(Exception e){
				System.out.println("You are failed to upload  => " + e.getMessage());
			}
		}else{
			System.err.println("File not found");
		}
		return null;
	}
}
