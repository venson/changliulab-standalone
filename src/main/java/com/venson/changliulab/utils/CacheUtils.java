package com.venson.changliulab.utils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

public abstract class CacheUtils {
//    public static String keyPrefix(String prefix){
//        return String.format("'%s'", prefix);
//    }

    public static String getPrefix(String str){
        return str.substring(1,str.length()-1);
    }

    public static void evict(CacheManager manager, String value){
        Cache cache = manager.getCache(value);
        if(cache!=null){
            cache.clear();
        }

    }
    public static void evict(CacheManager manager, String prefix, Collection<Long> ids ){
        if(ids.isEmpty()) return;
        ids.forEach(id->{
            Cache cache = manager.getCache(prefix + id);
            if(cache!=null){
                cache.clear();
            }
        });

    }
    public static void evict(CacheManager manager, String value, String... keys){
        Cache cache = manager.getCache(value);
        if(cache!=null){
            for (String key : keys) {
                cache.evict(key);
            }
            cache.clear();
        }

    }

}
