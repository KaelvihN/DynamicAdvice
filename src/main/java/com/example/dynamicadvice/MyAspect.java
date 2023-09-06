package com.example.dynamicadvice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author : KaelvihN
 * @date : 2023/9/5 16:45
 */

@Aspect
public class MyAspect {
    /**
     * 静态通知调用，无需参数绑定，性能高
     * 执行无需切点信息
     */
    @Before("execution(* foo(..))")
    public void before0() {
        System.out.println("Before0");
    }

    /**
     * 动态通知调用，需要参数绑定，性能低
     * 执行需要切点信息
     */
    @Before("execution(* foo(..)) && args(x)")
    public void before1(int x) {
        System.out.println("Before1 >>> " + x);
    }
}
