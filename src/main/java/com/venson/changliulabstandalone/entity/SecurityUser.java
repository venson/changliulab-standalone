package com.venson.changliulabstandalone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 安全认证用户详情信息
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Data
@Slf4j
public class SecurityUser implements UserDetails {

    //当前登录用户
    private transient UserInfoBO currentUserInfo;

    //当前权限
    private List<String> permissionValueList;

    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities= null;

    public SecurityUser(UserInfoBO user) {
        if (user != null) {
            this.currentUserInfo = user;
        }
    }

    public UserInfoBO getUser(){
        return currentUserInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(null ==authorities){
            authorities = permissionValueList.parallelStream()
                    .filter(StringUtils::hasText)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
