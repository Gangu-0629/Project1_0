package com.springBoot.Project.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springBoot.Project.Customs.CustomUserdetails;
import com.springBoot.Project.Entity.Customuser;
import com.springBoot.Project.Entity.RegisterUser;
import com.springBoot.Project.Repository.Userrepo;

import jakarta.mail.internet.MimeMessage;

@Service
public class Userservice implements UserDetailsService {
	@Autowired
	private Userrepo userrepo;
	@Bean
	public PasswordEncoder passwordEncoder()
	{
	    return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private JavaMailSender mailsender;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		
	    Customuser user=userrepo.findByEmail(username);
	    if(user==null) {
	    	throw new UsernameNotFoundException("UserNotfound");
	    }else {
	    CustomUserdetails cud=new CustomUserdetails(user);
		return cud;
	    }
	}
	
	public Customuser saveuser(RegisterUser user,String url) {
		String password = passwordEncoder().encode(user.getPassword());
	   
		Customuser us=Customuser.builder().role("ROLE_USER").email(user.getEmail()).enable(false).password(password).username(user.getUsername()).veriycode(UUID.randomUUID().toString()).build();
		System.out.println(us.getUsername());
		Customuser newuser= userrepo.save(us);
		System.out.println(newuser.getUsername());
		if(newuser!=null) {
			sendEmail(newuser, url);
		}
		return newuser;
		 
	}
	
	
	
	public void sendEmail(Customuser user, String url) {

		String from = "agangu@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verfication";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Becoder";
		System.out.println(content);

		try {

			MimeMessage message = mailsender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			System.out.println("Inside");
     
			helper.setFrom(from, "Atagara Gangadhar");
			helper.setTo(to);
			helper.setSubject(subject);
           
			
			System.out.println(user.getUsername()+" "+user.getVeriycode());
			content = content.replace("[[name]]", user.getUsername());
			System.out.println("Inside");
			String siteUrl =  "http://localhost:8080/verify?code=" + user.getVeriycode();


			content = content.replace("[[URL]]", siteUrl);

			helper.setText(content, true);

			mailsender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public boolean verifyAccount(String verificationCode) {

		Customuser user = userrepo.findByVeriycode(verificationCode);

		if (user == null) {
			return false;
		} else {

			user.setEnable(true);
			user.setVeriycode("");
			userrepo.save(user);
			return true;
		}

	}


}
