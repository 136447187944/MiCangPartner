package com.mic.zl.micangpartner.util;


import java.util.regex.Pattern;

public class FormatTool {
    private static final String CHEINESE_WORDS="^[\u4e00-\u9fa5]{0,10}";//最多允许10个汉子
    private static final String TEST_PASSWORD="/^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*? ]).*$/;";//验证密码,字母开头,由数字、英文、字符组成
    //身份证验证
    private static final String ID_CARD="^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$";

    private static final String TEST_PHONE="^(13[0-9]|14[0-9]|15[0-9]|166|17[0-9]|18[0-9]|19[8|9])\\d{8}$";
    public static boolean isChinese(String name){
        if (Pattern.matches(CHEINESE_WORDS,name)){
            return true;
        }
        return false;
    }

    public static boolean testPwd(String pwd){
        if (Pattern.matches(TEST_PASSWORD,pwd)){
            return true;
        }
        return false;
    }

    public static  boolean id_Card(String id_Card){
        if (Pattern.matches(ID_CARD,id_Card)){
            return true;
        }
        return false;
    }

    public static  boolean testPhone(String phone){
        if (phone.matches(TEST_PHONE)){
            return true;
        }
        return false;
    }
}
