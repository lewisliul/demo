package com.moretv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2016/9/24.
 */
public class Bean {
    private String name = "liuhao";
    private  int age = 10;
    private List lista ;
    private subBean subBean;
    private Enum anEnum;
    private Map mapa;
    private subBean subBean2;

    public Bean(){
        lista = new ArrayList();
        lista.add(1);
        lista.add(2);
        subBean = new subBean();
        anEnum = MainActivity.Weather.cloudy;
        mapa = new HashMap();
        mapa.put("name","feifei");
        mapa.put("name2","feifei2");
        subBean2 = null;
    }


    @Override
    public String toString() {
        return "name : " + name +
                ", age : " + age +
                ", lista : " + lista +
                ", subBean : " + subBean +
                ", subBean2 : " + subBean2 +
                ", mapa : " + mapa +
                ", anEnum : " + anEnum;
    }
}
