package com.example.dynamicadvice;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : KaelvihN
 * @date : 2023/9/5 17:04
 */

@Configuration
public class MyConfig {
    @Bean
    public MyAspect myAspect() {
        return new MyAspect();
    }

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        return new AnnotationAwareAspectJAutoProxyCreator();
    }
}
