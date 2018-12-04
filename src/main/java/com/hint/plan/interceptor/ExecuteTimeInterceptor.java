package com.hint.plan.interceptor;

import com.hint.plan.annotations.Order;

@Order(3)
public class ExecuteTimeInterceptor extends PlanInterceptor {
    private long start;
    private long end;
    @Override
    protected boolean before(String reqStr) {
        long l = System.currentTimeMillis();
        System.out.println("--------->执行时间拦截器before");
        return super.before(reqStr);
    }

    @Override
    protected void after(String reqStr) {
        System.out.println("--------->执行时间拦截器after");
        System.out.println("--------->执行时间time=" + (end-start));
    }
}
