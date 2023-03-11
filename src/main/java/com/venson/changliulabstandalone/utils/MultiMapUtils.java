package com.venson.changliulabstandalone.utils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MultiMapUtils {
    private MultiMapUtils(){}

    /**
     * add an element to multi value map
     *
     * @param key the key of
     * @param setElement  the set element to add
     * @param map the map use set as values
     * @param <K>  type of key
     * @param <T> type of the element of set
     */
    public static <K,T> void put(K key,T setElement, Map<K, LinkedHashSet<T>> map){
        if(map.containsKey(key)){
            LinkedHashSet<T> set = map.get(key);
            if(!set.contains(setElement)){
                set.add(setElement);
                map.put(key,set);
            }
        }else{
            LinkedHashSet<T> set = new LinkedHashSet<>();
            set.add(setElement);
            map.put(key,set);
        }
    }
    public static <K,T> void put(K key,Set<T> addSet, Map<K, LinkedHashSet<T>> map){
        if(map.containsKey(key)){
            LinkedHashSet<T> set = map.get(key);
                set.addAll(addSet);
                map.put(key,set);
        }else{
            LinkedHashSet<T> set = new LinkedHashSet<>(addSet);
            map.put(key,set);
        }
    }

    public static <K,T> LinkedHashSet<T> get(K key, Map<K,LinkedHashSet<T>> map){
        return map.get(key);
    }

    public static <K,T> void remove(K key, Map<K, LinkedHashSet<T>> map){
        map.remove(key);
    }
    public static <K,T> void remove(K key,T setElement, Map<K, LinkedHashSet<T>> map){
        if(map.containsKey(key)){
            LinkedHashSet<T> set = map.get(key);
            if(set.contains(setElement)){
                set.remove(setElement);
                map.put(key, set);
            }
        }
    }

}
