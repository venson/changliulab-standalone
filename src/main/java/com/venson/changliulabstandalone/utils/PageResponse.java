package com.venson.changliulabstandalone.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 29392383L;
    private List<T> records;
    private List<T> data;
    private Integer pages;
    private Integer current;
    private Integer size;
    private Integer total;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private PageInfo pageInfo;
    private Integer newCount;

    {
        this.pageInfo = new PageInfo();
    }
    @Setter
    @Getter
    @NoArgsConstructor
    class PageInfo{
        Boolean hasPreviousPage;
        Boolean hasNextPage;
    }
}
