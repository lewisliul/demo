package com.typeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hc on 2016/9/24.
 */
public class Bean {
    private List lista ;
    private String string = "string";
    private subBean subBean;
    private Enum anEnum;
    private Map mapa;
    private subBean subBean2;
    private Sub subBean3;

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
        subBean3 = new Sub();
    }


    @Override
    public String toString() {
        return
                ", lista : " + lista +
                ", subBean : " + subBean +
                ", subBean2 : " + subBean2 +
                ", mapa : " + mapa +
                ", anEnum : " + anEnum+
                 ", subBean3 : " + subBean3 +
				", string : " + string;
    }

    static class Sub{
        private String name = "subBean-sub";
        private String name2 = "";
    }
}
