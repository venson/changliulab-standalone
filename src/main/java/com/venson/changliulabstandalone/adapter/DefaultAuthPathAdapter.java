package com.venson.changliulabstandalone.adapter;

import com.venson.changliulabstandalone.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DefaultAuthPathAdapter implements AuthPathAdapter{
    private final PathPatternParser parser = PathPatternParser.defaultInstance;
    private final List<String> pathWhiteList = new ArrayList<>();
    private final List<PathPattern> patternWhiteList = new ArrayList<>();

    private final List<PathPattern> patternDocList = new ArrayList<>();
    {
        log.info("init login path");
        pathWhiteList.add(AuthConstants.LOGIN_PATH_ADMIN);
        pathWhiteList.add(AuthConstants.LOGIN_PATH_USER);
        patternWhiteList.add(parser.parse(AuthConstants.FRONT_PATTERN));
        Arrays.stream(AuthConstants.DOC_PATTERNS).forEach(o->patternDocList.add(parser.parse(o)));
    }


    @Override
    public List<String> pathWhiteList() {
        return pathWhiteList;
    }

    @Override
    public List<PathPattern> patternWhiteList() {
        return patternWhiteList;
    }

    @Override
    public List<PathPattern> patternDocList() {
        return patternDocList;
    }

    @Override
    public boolean checkWhiteList(String url) {
        if(pathWhiteList.size() ==0){
            throw new RuntimeException("pathWhite list is empty");
        }
        for (String path : pathWhiteList) {
            if( path.equals(url)) return true;
        }
        return false;
    }

    @Override
    public boolean checkPatternList(String url) {
        if(patternWhiteList.size() ==0 )
            throw new RuntimeException("Path pattern white list is empty");
        PathContainer path = PathContainer.parsePath(url);
        for (PathPattern pathPattern : patternWhiteList) {
            if(pathPattern.matches(path)) return true;
        }
        return false;
    }

}
