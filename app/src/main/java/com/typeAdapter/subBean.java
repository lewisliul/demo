package com.typeAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hc on 2016/9/26.
 */
public class subBean {
    private double aDouble = 123.123;
    private float aFloat = 123.1f;
    private Character aCharacter = 'a';
    private  int aAge = 10 ;
    private byte aByte = 1;
    private short aShort = 2;
    private boolean aBoolean = true;
    private Calendar aCalendar = Calendar.getInstance();
    private Date aDate = new Date();
    private String aName = "liuhao";
    private Long aLong = 1234677l;

    @Override
    public String toString() {
        return "{" +
				"doubleN :" + aDouble +
                ",floatN : " + aFloat +
                ",calendar : " + aCalendar +
                ",character : " + aCharacter +
                ",age : " + aAge +
                "name : " + aName +
                ",byte : " + aByte +
                ",short : " + aShort +
                ",boolean : " + aBoolean +
                ",date : " + aDate +
                ",long : " + aLong +
                "}";
    }
}
