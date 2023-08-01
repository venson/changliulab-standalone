package com.venson.changliulab.constant;

public final class AuthConstants {
    private AuthConstants(){}
    public static final String ADMIN_PREFIX = "ADMIN";
    public static final String USER_PREFIX = "USER";
    public static final Long EXPIRE_24H_MS = (long) (24 * 60 * 60 * 1000);
    public static final Long EXPIRE_2DAY_MS = (long) (48 * 60 * 60 * 1000);
    public static final Long EXPIRE_24H_S = (long) (24 * 60 * 60);
    public static final Long EXPIRE_2DAY_S = (long) (48 * 60 * 60);
    public static final String LOGIN_PATH_ADMIN = "/auth/admin/login";
    public static final String LOGIN_PATH_USER = "/auth/front/login";
    public static final String FRONT_PATTERN = "/*/front/**";
    public static final String ADMIN_PATTERN = "/*/admin/**";
    public static final String[] DOC_PATTERNS = {"/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html"};

    public static final String TOKEN_KEY = "Authorization";
    public static final String BEARER = "Bearer";
}
