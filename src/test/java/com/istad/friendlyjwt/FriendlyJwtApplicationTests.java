package com.istad.friendlyjwt;

import com.istad.friendlyjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FriendlyJwtApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testingGettingUser(){
		System.out.println("hello world ");
		var user = userRepository.loadUserByUsername("visal");
		System.out.println("Here is the information of the user : ");
		System.out.println(user);
	}

}
