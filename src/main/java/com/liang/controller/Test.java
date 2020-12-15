package com.liang.controller;

/**
 * @author LiangYonghui
 * @date 2020/10/10 14:06
 * @description
 */
public class Test {
    int i = 1;

    public Test() {
        this.i = 2;
    }

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.i);
    }
}
