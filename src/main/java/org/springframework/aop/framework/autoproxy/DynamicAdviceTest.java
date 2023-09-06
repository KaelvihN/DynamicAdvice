package org.springframework.aop.framework.autoproxy;


import com.example.dynamicadvice.MyConfig;
import com.example.dynamicadvice.Target;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author : KaelvihN
 * @date : 2023/9/5 17:06
 */
public class DynamicAdviceTest {
    public static void main(String[] args) throws Throwable {
        test0();
    }

    public static void test0() throws Throwable {
        //创建一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();
        //注册解析@Bean的BeanFactoryPostProcessor
        context.registerBean(ConfigurationClassPostProcessor.class);
        //注册MyConfig
        context.registerBean(MyConfig.class);
        //初始化容器
        context.refresh();

        AnnotationAwareAspectJAutoProxyCreator creator
                = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        //将低级切面封装到集合中(低级切面+高级切面转为的低级切面)
        List<Advisor> advisorList = creator.findEligibleAdvisors(Target.class, "target");

        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        Method method = Target.class.getMethod("foo", int.class);
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisors(advisorList);
        //获取代理
        Object proxy = proxyFactory.getProxy();
        //获取环绕通知集合
        List<Object> interceptorList =
                proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(method, Target.class);
        interceptorList.forEach(System.out::println);

        System.out.println(">>>>>>>>>>");

        for (Object obj : interceptorList) {
            showDetail(obj);
        }
        System.out.println(">>>>>>>>>>");
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                proxy, target, method, new Object[]{11}, Target.class, interceptorList
        ) {
        };
        methodInvocation.proceed();
    }


    public static void showDetail(Object o) {
        try {
            Class<?> clazz = Class.forName("org.springframework.aop.framework.InterceptorAndDynamicMethodMatcher");
            if (clazz.isInstance(o)) {
                Field methodMatcher = clazz.getDeclaredField("methodMatcher");
                methodMatcher.setAccessible(true);
                Field methodInterceptor = clazz.getDeclaredField("interceptor");
                methodInterceptor.setAccessible(true);
                System.out.println("环绕通知和切点：" + o);
                System.out.println("\t切点为：" + methodMatcher.get(o));
                System.out.println("\t通知为：" + methodInterceptor.get(o));
            } else {
                System.out.println("普通环绕通知：" + o);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
