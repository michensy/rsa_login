package com.sf.rsa;

import com.sf.rsa.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RsaApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findOneTest() throws Exception{

        // userRepository.save(User.builder().id(1L).username("zhang").password("123").build());
        //
        // List<User> all = userRepository.findAll();
        // log.info(all.toString());
    }



}
