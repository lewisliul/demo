package com.moretv;

/**
 * Created by hc on 2016/9/26.
 */
public class subBean {
    private String name = "haozi";
    private String name2 = "haozi2";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Override
    public String toString() {
        return "{name :" + name +"}";
    }
}
