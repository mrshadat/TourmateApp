package com.mrshadat.tourmateapp.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventUtils {
    public static String getCurrentDateTime(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new Date());
    }
}
