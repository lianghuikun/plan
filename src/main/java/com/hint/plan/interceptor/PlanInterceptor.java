package com.hint.plan.interceptor;

/**
 * 拦截器抽象类--责任链模式
 */
public abstract class PlanInterceptor {
    private int order;

    /**
     * 如果是true才可以被下一个拦截器处理，或者才可以处理自己的逻辑
     * @param reqStr
     * @return
     */
    protected boolean before(String reqStr) {
        return Boolean.TRUE;
    }

    protected void after(String reqStr) {

    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
