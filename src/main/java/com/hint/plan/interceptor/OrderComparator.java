package com.hint.plan.interceptor;

import java.util.Comparator;

public class OrderComparator implements Comparator<PlanInterceptor> {
    @Override
    public int compare(PlanInterceptor o1, PlanInterceptor o2) {
        if (o1.getOrder() > o2.getOrder()) {
            return 1;
        }
        return 0;
    }
}
