package com.venson.changliulabstandalone.utils;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data

public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 29392383L;
    private List<T> records;
    private Integer pages;
    private Integer current;
    private Integer size;
    private Integer total;
    private Boolean hasNext;
    private Boolean hasPrevious;
//        map.put("records", page.getRecords());
//        map.put("pages", Math.toIntExact(page.getPages()));
//        map.put("current", Math.toIntExact(page.getCurrent()));
//        map.put("size",Math.toIntExact(page.getSize()));
//        map.put("total", Math.toIntExact(page.getTotal()));
//        map.put("hasNext",page.hasNext());
//        map.put("hasPrevious",page.hasPrevious());
}
