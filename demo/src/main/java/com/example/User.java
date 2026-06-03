package com.example;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

    private static Logger logger = LoggerFactory.getLogger(User.class);

    private List<String> list;

    private HashMap<String, Integer> map;

    private Set<String> set;

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public void setMap(HashMap<String, Integer> map) {
        this.map = map;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        logger.debug("11111111toString method called");
        System.out.println("注入list------->" + list);
        System.out.println("注入map ----->" + map);
        System.out.println("注入set ----->" + set);
        logger.error("some error");
        return "hello user";
    }

}
