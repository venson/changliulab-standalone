package com.venson.changliulabstandalone.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectCategory {

    @ExcelProperty(index = 0)
    private String topSubject;
    @ExcelProperty(index = 1)
    private String levelISubject;
}
