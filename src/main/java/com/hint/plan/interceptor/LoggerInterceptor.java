package com.hint.plan.interceptor;

import com.hint.plan.annotations.Order;
import org.jcp.xml.dsig.internal.SignerOutputStream;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 */
@Order(2)
public class LoggerInterceptor extends PlanInterceptor {
    @Override
    protected boolean before(String reqStr) {
        System.out.println("--------->日志拦截器before");
        return super.before(reqStr);
    }

    @Override
    protected void after(String reqStr) {
        System.out.println("--------->日志拦截器after");
//        super.after(reqStr);
        System.out.println("------------>日志拦截器：reqStr=" + reqStr);
    }
}
