package com.venson.changliulabstandalone.filter;

import com.venson.changliulabstandalone.adapter.AuthPathAdapter;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.TokenManager;
import com.venson.changliulabstandalone.utils.ResponseUtil;
import com.venson.changliulabstandalone.utils.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter implements Ordered {
    private final TokenManager tokenManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthPathAdapter pathAdapter;

    public AuthTokenFilter(TokenManager tokenManager, RedisTemplate<String, Object> redisTemplate, AuthPathAdapter pathAdapter) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.pathAdapter = pathAdapter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain) throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        if(HttpMethod.OPTIONS.matches(req.getMethod())){
            chain.doFilter(req,res);
            return;
        }
        String token = getBearerToken(req);
        List<String> pathWhiteList = pathAdapter.pathWhiteList();
        List<PathPattern> patternWhiteList = pathAdapter.patternWhiteList();
//        List<PathPattern> patternDocList = pathAdapter.patternDocList();
        UsernamePasswordAuthenticationToken authentication;

        //1.request math white list
        // login url
        if (checkPathWhiteList(requestURI, pathWhiteList)) {
            chain.doFilter(req, res);
            return;
        }
        //        /*/front/**
        // 2. request match pattern
        if (checkPathPatternWhiteList(requestURI, patternWhiteList)) {
            if (token != null && !"undefined".equals(token)) {
                try {
                    authentication = getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(req, res);
                    return;
                } catch (Exception ignored) {}
                finally {
                    SecurityContextHolder.clearContext();

                }
            }
            chain.doFilter(req, res);
            return;
        }
        // if got token, check auth
        try {
            authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
            return;
        } catch (Exception e) {
//            e.printStackTrace();
            log.info(e.toString());
            log.info("auth Failed:");
            log.info(req.getRemoteAddr());
            log.info(req.getRequestURI());
            ResponseUtil.out(res, Result.tokenExpire());
        }finally {
            SecurityContextHolder.clearContext();

        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        // token置于header里
        UserContextInfoBO userContextInfoBO = getRedisUserByRequest(token);
        List<String> permissionValueList = userContextInfoBO.getPermissionValueList();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String permissionValue : permissionValueList) {
            if (ObjectUtils.isEmpty(permissionValue)) continue;
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
            authorities.add(authority);
        }
        return new UsernamePasswordAuthenticationToken(userContextInfoBO, userContextInfoBO.getToken(), authorities);
    }

    /**
     * get Bearer type token from request
     * @param request http servlet request
     * @return token String
     */
    private String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.contains("Bearer")) {
            return token.split(" ")[1];
        }
        return null;

    }

    /**
     * get UserContextInfoBo from redis by token
     * @param token token String
     * @return userContextInfoBO with user info
     */
    private UserContextInfoBO getRedisUserByRequest(String token) {
        // token置于header里
        if (StringUtils.hasText(token)) {
            String redisKey = tokenManager.getRedisKeyFromToken(token);
            Object o = redisTemplate.opsForValue().get(redisKey);
            if (o instanceof UserContextInfoBO) {
                return (UserContextInfoBO) o;
            }
        }
        log.info("redis user auth down");
        throw new CustomizedException(20001, "Token error");
    }

    /**
     * check whether given path match whiteList pattern
     * @param path the path get from request
     * @param pathPatternWhiteList List<PathPattern> pathPattern list
     * @return return true if path match one of pattern in pathPatternWhiteList
     */
    private boolean checkPathPatternWhiteList(String path, List<PathPattern> pathPatternWhiteList) {
        for (PathPattern pattern : pathPatternWhiteList) {
            if (pattern.matches(PathContainer.parsePath(path))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * check whether the given path equals whiteList path
     * @param path the path get from request
     * @param pathWhiteList List<String> path whitelist List
     * @return return true if path equals one of the path in pathWhiteList
     */
    private boolean checkPathWhiteList(String path, List<String> pathWhiteList) {
        for (String white : pathWhiteList) {
            if (white.equals(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
