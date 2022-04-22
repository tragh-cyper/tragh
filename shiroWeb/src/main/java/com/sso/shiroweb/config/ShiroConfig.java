package com.sso.shiroweb.config;

import com.sso.shiroweb.entity.MyRealm;
import com.sso.shiroweb.filter.JWTFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private MyRealm myRealm;

//    //Realm 系统资源
//    @Bean
//    public Realm myRealm(){
//          return new MyRealm();
//    }

    //SecurityManager
    @Bean
    public DefaultWebSecurityManager getSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // 设置自定义 realm.
        securityManager.setRealm(myRealm);
        return securityManager;
    }

    //ShiroFilterFactoryBean 请求过滤器
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager mySecurityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(mySecurityManager);

        Map<String, Filter> m = new HashMap<>();
        m.put("jwt",new JWTFilter());
        factoryBean.setFilters(m);
        
        //配置路径过滤器
        Map<String,String> filterMap = new HashMap<>();

        //key是ant路径，支持**代表多级路径，*代表单级路径，？代表一个字符
        //value配置shiro的默认过滤器
        //shiro的默认过滤器，配置DefaultFilter中的Key
        //auth,authc,perms,role
        filterMap.put("/common/**","anon");
        filterMap.put("/mobile/**","authc");
        filterMap.put("/salary/**","jwt,authc");

        factoryBean.setFilterChainDefinitionMap(filterMap);

        return factoryBean;
    }
}
