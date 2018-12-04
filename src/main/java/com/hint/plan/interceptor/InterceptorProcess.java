package com.hint.plan.interceptor;

import com.hint.plan.util.ClassScanner;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 拦截器链
 */
public class InterceptorProcess {
    // TODO 先不考虑并发情况
    private static InterceptorProcess process;
    private static List<PlanInterceptor> interceptors;


    /**
     * get single Instance
     *
     * @return
     */
    public static InterceptorProcess getInstance() {
        if (process == null) {
            process = new InterceptorProcess();
        }
        return process;
    }

    /**
     * 加载所有拦截器
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void loadInterceptors() throws IllegalAccessException, InstantiationException {
        if (interceptors != null) {
            return;
        }
        interceptors = new ArrayList<>(10);
        // TODO 应该从配置文件中对应的实体类中获取包路径，这里直接写死了
        String rootPackageName = "com.hint.plan";
        Map<Class<?>, Integer> planInterceptor = ClassScanner.getPlanInterceptor(rootPackageName);

        Set<Map.Entry<Class<?>, Integer>> entries = planInterceptor.entrySet();
        for (Map.Entry<Class<?>, Integer> entry : entries) {
            Class<?> clazz = entry.getKey();
            PlanInterceptor interceptor = (PlanInterceptor) clazz.newInstance();
            Integer order = entry.getValue();
            interceptor.setOrder(order);
            interceptors.add(interceptor);
        }
        // 对拦截器进行排序
        Collections.sort(interceptors, new OrderComparator());
    }

    /**
     * 拦截前的入口
     *
     * @param reqStr
     * @return
     */
    public boolean processBefore(String reqStr) {
        for (PlanInterceptor interceptor : interceptors) {
            boolean access = interceptor.before(reqStr);
            if (!access) {
                return access;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 拦截和的入口
     *
     * @param reqStr
     */
    public void processAfter(String reqStr) {
        for (PlanInterceptor interceptor : interceptors) {
            interceptor.after(reqStr);
        }
    }
}
