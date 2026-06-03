package com.example.test;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.Date;

import com.example.Student;
import com.example.User;

public class SpringTest {
    @Test
    public void testBean01() {
        // try (ApplicationContext ctx = new
        // ClassPathXmlApplicationContext("beans.xml")) {
        // User bean = (User) ctx.getBean("userBean");
        // System.out.println("------> \n" + bean);
        // }
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        User bean = (User) ctx.getBean("userBean");
        User bean2 = (User) ctx.getBean("userBean");
        System.out.println("------> \n" + bean);
        System.out.println("------> \n" + (bean == bean2));
    }

    @Test
    public void testBean02() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "beans.xml");
        User bean = (User) ctx.getBean("userBean");
        User bean2 = (User) ctx.getBean("userBean2");
        System.out.println("2------> \n" + bean);
        System.out.println("2------> \n" + bean2);
    }

    @Test
    public void testBean03() {

        // 创建支持无参数构造器的 class
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Date d = ctx.getBean("nowBean", java.util.Date.class);
        System.out.println("构造支持无参数的class \n" + d);
    }

    @Test
    public void testBean04() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        try {
            Student student = (Student) ctx.getBean("studentBean", Class.forName("com.example.Student"));
            student.print();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBean05() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        System.out.println(ctx.getBean("myJdbcs", com.example.MyJDBC.class).getPassword());
    }
}
