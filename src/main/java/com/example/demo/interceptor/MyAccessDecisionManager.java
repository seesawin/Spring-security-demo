package com.example.demo.interceptor;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * decide 方法是判定是否擁有許可權的決策方法，
     * authentication 是CustomUserService中迴圈新增到 GrantedAuthority 物件中的許可權資訊集合.
     * object 包含客戶端發起的請求的requset資訊，可轉換為 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * configAttributes 為MyInvocationSecurityMetadataSource的getAttributes(Object object)這個方法返回的結果，此方法是為了判定使用者請求的url 是否在許可權表中，如果在許可權表中，則返回給 decide 方法，用來判定使用者是否有此許可權。如果不在許可權表中則放行。
     * */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(null== configAttributes || configAttributes.size() <=0) {
            return;
        }
        ConfigAttribute c;
        String needRole;
        for(Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext(); ) {
            c = iter.next();
            needRole = c.getAttribute();
            for(GrantedAuthority ga : authentication.getAuthorities()) {//authentication 為在註釋1 中迴圈新增到 GrantedAuthority 物件中的許可權資訊集合
                if(needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
