package com.hint.plan.Controller;

public class GameController {

    /**
     * 打印
     *
     * @param str
     */
    public void print(String str) {
        System.out.println("----->print--->" + str);
    }

    /**
     * 获取
     *
     * @param str
     * @return
     */
    public String getStr(String str) {
        System.out.println("----->getStr--->" + str);
        if ("A".equals(str)) {
            return "ABC";
        }
        return "DEF";
    }
}
