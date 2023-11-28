package com.springBoot.Project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springBoot.Project.Entity.Customuser;
import com.springBoot.Project.Entity.RegisterUser;
import com.springBoot.Project.Service.Userservice;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {
	
	@Autowired
	private Userservice userservice;
	@PostMapping("/register")
	public Customuser registeruser(@RequestBody RegisterUser user,HttpServletRequest request) 
	{
	
	    String url=request.getRequestURI().toString();
	    url = url.replace(request.getServletPath(), "");
		System.out.println(url);
		// //http://localhost:8080/verify?code=3453sdfsdcsadcscd
	
		Customuser us= userservice.saveuser(user,url);
		return us;
	}
	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code) {
		
		boolean verify=userservice.verifyAccount(code);
		if(verify==true) {
			return "True";
		}
		else {
			return "False";
		}
	}
	
	@GetMapping("/about")
	public String about() {
		return "About";
	}
	
	@GetMapping("/home")
	public String home() {
		return "Home";
	}

}
