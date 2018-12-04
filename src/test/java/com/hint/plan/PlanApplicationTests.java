package com.hint.plan;

import com.alibaba.fastjson.JSON;
import com.hint.plan.Controller.GameController;
import com.hint.plan.interceptor.InterceptorProcess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * A 计划
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanApplicationTests {

    private final InterceptorProcess interceptorProcess = InterceptorProcess.getInstance();

    /**
     * 自定义反射
     * Spring MVC / Spring boot 接受请求的原理。
     * 扫描所有使用 @RestController 注解的类。
     * 扫描所有使用 @RequestMapping 注解的方法。
     * 将他们的映射关系存入 Map 中。
     * ------------代码如下，将url作为key，把方法名作为method放入map中。。。
     * private static Map<String,Method> routes = new HashMap<>();
     * Set<Class<?>> classes = ClassScanner.getClasses(packageName) ;
     * for (Class<?> aClass : classes) {
     * Method[] declaredMethods = aClass.getMethods();
     * for (Method method : declaredMethods) {
     * CicadaRoute annotation = method.getAnnotation(CicadaRoute.class) ;
     * if (annotation == null){
     * continue;
     * }
     * CicadaAction cicadaAction = aClass.getAnnotation(CicadaAction.class);
     * routes.put(appConfig.getRootPath() + "/" + cicadaAction.value() + "/" + annotation.value(),method) ;
     * }
     * }
     * 请求时根据 URL 去 Map 中查找这个关系。
     * 反射构建参数及方法调用。
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void testRef() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        // 自定义map，模拟扫描文件放入map之后的数据
        // 这里的key，就不模拟URL了
        Map<String, Method> routes = new HashMap<>();
        Method[] methods = GameController.class.getMethods();
        for (Method method : methods) {
            routes.put("game/" + method.getName(), method);
        }
        System.out.println(JSON.toJSONString(routes));
        String url1 = "game/print";
        String url2 = "game/getStr";

        Method method = routes.get(url1);
        // Object obj, Object... args
        GameController obj = new GameController();
        method.invoke(obj, new Object[] {"123"});

        Method method1 = routes.get(url2);
        Object invoke = method1.invoke(obj, new Object[]{"236"});
        System.out.println("-------->invoke--" + invoke);
    }


    /**
     * 自定义拦截器链
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void testInterceptroProcess() throws InstantiationException, IllegalAccessException {

        interceptorProcess.loadInterceptors();

        String str = "abc123f";
        boolean access = interceptorProcess.processBefore(str);
        if (!access) {
            // 如果有问题则不执行
            return;
        }
        System.out.println("do something----------");
        interceptorProcess.processAfter(str);
    }

}
