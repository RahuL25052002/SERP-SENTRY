package com.cdac.dao;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//org.junit.jupiter.api. - package
//Assertions - class
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserDaoTest {
//depency- dao layer
	@Autowired
	private UserDao userDao;
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
