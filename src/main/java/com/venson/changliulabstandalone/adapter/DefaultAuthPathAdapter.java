package com.venson.changliulabstandalone.adapter;

import com.venson.changliulabstandalone.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
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

}
