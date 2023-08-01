package com.venson.changliulab.entity.vo.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

/**
 * VO for list query params
 * @param page current page number
 * @param perPage items per page
 * @param sort sort info [ field, order]
 * @param filter filter info map<String, String>
 */
public record ListQueryParams(Integer page,
                              Integer perPage,
                              List<String> sort,
                              List<String> filter

                              ) {
    public List<HashMap<String, String>> getFilter(){
        ObjectMapper mapper = new ObjectMapper();
        return filter.stream().map(filterString -> {
            try {
                TypeReference<HashMap<String, String>> typeRef
                        = new TypeReference<HashMap<String, String>>() {};
                return mapper.readValue(filterString, typeRef);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
//        ObjectMapper mapper = new ObjectMapper();
////        TypeReference<HashMap<String,Object>> typeRef
////                = new TypeReference<HashMap<String,Object>>() {};
//        return filter.stream().map(o -> {
//            try {
//                return mapper.readValue(o, Map.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
    }
}
