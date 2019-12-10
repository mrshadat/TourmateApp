package com.mrshadat.tourmateapp.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventUtils {
    public static final String WEATHER_CONDITION_ICON_PREFIX = "https://openweathermap.org/img/wn/";

    public static String getCurrentDateTime(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date());
    }

    public static String getFormattedDate(long dt){
        Date date = new Date(dt * 1000);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(date);
    }
}
