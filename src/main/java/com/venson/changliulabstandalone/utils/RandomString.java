package com.venson.changliulabstandalone.utils;


import java.util.Random;

public abstract class RandomString {
    public static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUM = "1234567890";
    public static final String ALPHA_NUM= ALPHA + NUM;
    private static final Random random = new Random();

    public static String randomInt(int length){
        return random(length,NUM);
    }
    public static String randomInt(){
        return random(6,NUM);
    }
    public static String randomString(int length){
        return random(length,ALPHA);
    }

    public static String randomCode(int length){
        return random(length,ALPHA_NUM);
    }
    public static String randomCode(){
        return random(6,ALPHA_NUM);
    }
    private static String random(int length, String baseString){
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            Character temp = baseString.charAt(random.nextInt(baseString.length()));
            result.append(temp);
        }
        return result.toString();
    }
}
