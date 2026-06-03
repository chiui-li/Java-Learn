package com.example;

public class Parent {

    public GrandParent parent;

    //
    public String sayhello() {
        this.parent.sayhello();
        return "hello my child";
    }

    Parent() {
    }

    Parent(GrandParent parent) {
        this.parent = parent;
    }
}
