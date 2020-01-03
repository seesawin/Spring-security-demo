package com.example.demo.service;

import com.example.demo.dao.PermissionDao;
import com.example.demo.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private PermissionDao permissionDao;

    private HashMap<String, Collection<ConfigAttribute>> resourceDefineMap = null;

    /**
     * 載入許可權表中所有許可權
     */
    public void loadResourceDefine() {
        resourceDefineMap = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<Permission> permissions = permissionDao.findAll();
        for (Permission permission : permissions) {
            array = new ArrayList<>();
            cfg = new SecurityConfig(permission.getName());
            //此處只添加了使用者的名字，其實還可以新增更多許可權的資訊，例如請求方法到ConfigAttribute的集合中去。此處新增的資訊將會作為MyAccessDecisionManager類的decide的第三個引數。
            array.add(cfg);
            //用許可權的getUrl() 作為map的key，用ConfigAttribute的集合作為 value，
            resourceDefineMap.put(permission.getUrl(), array);
        }

    }

    /**
     * 此方法是為了判定使用者請求的url 是否在許可權表中，如果在許可權表中，則返回給 decide 方法，
     * 用來判定使用者是否有此許可權。如果不在許可權表中則放行
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(resourceDefineMap)) {
            loadResourceDefine();
        }
        //object 中包含使用者請求的request 資訊
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        for (Iterator<String> iter = resourceDefineMap.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if (matcher.matches(request)) {
                return resourceDefineMap.get(resUrl);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
