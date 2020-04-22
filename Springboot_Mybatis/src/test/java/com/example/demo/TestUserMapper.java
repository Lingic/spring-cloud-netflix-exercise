package com.example.demo;

import com.example.DemoApplication;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestUserMapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        User user = userMapper.Sel(1);
        System.out.println(user.toString());
    }
}
