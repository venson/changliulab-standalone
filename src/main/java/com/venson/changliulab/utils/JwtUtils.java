package com.venson.changliulab.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private JwtUtils(){}
    public static final long EXPIRE= 1000* 60 * 60 *24;
    public static String APP_SECRET;
    private static Key key;

    public static void setJwtKey(String stringKey){
        if(key ==null){
            APP_SECRET=stringKey;
            byte[] decode = Decoders.BASE64.decode(APP_SECRET);
            key = Keys.hmacShaKeyFor(decode);
        }
    }
    public static String getJwtToken(Long id, String nickName){
        Assert.notNull(key);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS512")
                .setSubject("ChangLiuLab")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id",id)
                .claim("nickName",nickName)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    public static String getJwtToken(Long id, String nickName, String signKey, Long expire){
        Assert.notNull(key);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS512")
                .setSubject("ChangLiuLab")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .claim("id",id)
                .claim("nickName",nickName)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }
    public static boolean checkToken(String jwtToken){
        Assert.notNull(key);
        if(ObjectUtils.isEmpty(jwtToken)){
            return false;
        }
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
//            Jwts.parserBuilder().setSigningKey(sign).build().parseClaimsJws(jwtToken);
//            Jwts.parser().setSigningKey(sign).parseClaimsJws(jwtToken);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getTokenSubject(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

}
