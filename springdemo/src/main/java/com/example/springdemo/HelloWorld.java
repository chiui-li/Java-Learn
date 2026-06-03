package com.example.springdemo;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.entity.User;
import com.example.springdemo.mapper.UserMapper;

@RestController
public class HelloWorld {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redis;

    public HelloWorld() {
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/users")
    public List<User> getUser() {
        // redis.opsForValue()
        // .set("name", "Tom");
        Object value = redis.opsForValue()
                .get("name");
        System.out.println("------->" + value);
        return userMapper.selectUserList();
    }

    @PostMapping("/user")
    // @Transactional
    public HashMap<String, Object> createUser(@RequestBody User newUser) {
        var h = new HashMap<String, Object>();
        h.put("result", userMapper.addUser(newUser));
        if ((Boolean) h.get("result") == true) {
            throw new Error("mmmmm");
        }
        return h;
    }

    @PostMapping("/del/user/{id}")
    public HashMap<String, Object> delUser(@PathVariable Long id) {
        var h = new HashMap<String, Object>();
        h.put("result", userMapper.delUser(id));
        return h;
    }
}
