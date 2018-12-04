package com.hint.plan.interceptor;

import com.hint.plan.annotations.Order;
import org.springframework.util.StringUtils;

/**
 * 大小写拦截器
 */
@Order(1)
public class LowerInterceptor extends PlanInterceptor {
    @Override
    protected boolean before(String reqStr) {
        System.out.println("--------->大小写拦截器before, reqStr=" + reqStr);
        return super.before(reqStr.toUpperCase());
    }

    @Override
    protected void after(String reqStr) {
        System.out.println("--------->大小写拦截器after, reqStr=" + reqStr);
        super.after(reqStr);
    }
}
