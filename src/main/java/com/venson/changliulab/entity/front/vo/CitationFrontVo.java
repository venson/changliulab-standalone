package com.venson.changliulab.entity.front.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitationFrontVo {
//    the x-axis of citation bar graph, year
    private List<Integer> dataX;
    //    the x-axis of citation bar graph, citation
    private List<Integer> dataY;
}
