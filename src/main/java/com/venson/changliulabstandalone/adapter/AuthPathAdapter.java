package com.venson.changliulabstandalone.adapter;

import org.springframework.web.util.pattern.PathPattern;

import java.util.List;

public interface AuthPathAdapter {
     List<String> pathWhiteList();
     List<PathPattern> patternWhiteList();

     List<PathPattern> patternDocList();

     boolean checkWhiteList(String url);
     boolean checkPatternList(String url);
}
