package com.ibm.cn.cdl;

import com.alibaba.fastjson.JSON;
import com.ibm.cn.cdl.annotation.JsonParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by wxy on 2015/5/6.
 */
@RestController
public class TestCtrl {
    @RequestMapping("/test1")
    public List<User> test1(@JsonParam User[] users){
        System.out.println(users.length);
        System.out.println(users.length);
        return Arrays.asList(users);
    }

    @RequestMapping("/test2")
    public Date test2(){
        return new Date();
    }

    public static void main(String[] args) {
        User user = new User();
        user.setAge(1);
        user.setName("wxy");
        String string = JSON.toJSONString(user);
        System.out.println(string);
    }
}
