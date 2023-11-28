package com.springBoot.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springBoot.Project.Entity.Customuser;
import java.util.List;


@Repository
public interface Userrepo extends JpaRepository<Customuser,Long> 
{
	Customuser  findByEmail(String email);
	
	Customuser  findByVeriycode(String veriycode);

}
