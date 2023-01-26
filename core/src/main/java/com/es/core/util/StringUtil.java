package com.es.core.util;

public class StringUtil {
    public static boolean isBlank(String str){
        if(str==null)
            return true;
        if(str.trim().isEmpty())
            return true;

        return false;
    }
}
