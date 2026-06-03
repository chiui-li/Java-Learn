package com.example;

public class Student {
    private Parent parent;

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void print() {
        System.out.println("Student's parent: " + parent.sayhello());
    }

}
