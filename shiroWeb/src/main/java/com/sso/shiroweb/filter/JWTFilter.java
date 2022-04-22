package com.sso.shiroweb.filter;

import com.sso.shiroweb.entity.JWTToken;
import com.sso.shiroweb.utils.JWTUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *   preHandle-->isAccessAllowed(true/false)-->executeLogin(是否有异常)-->LoginRealm
 *      登录成功则访问资源
 *      登录失败则onAccessDenied(这个方法返回了一个401)
 *
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

     private Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    /**
     * 1，这个方法一般用于处理跨域
     * 2.更新令牌
     * 3.发送令牌给客户端
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "token");

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        //得到客户端传过来的令牌
        String token = ((HttpServletRequest) request).getHeader("token");
        logger.info("Filter.token=="+token);

        //更新令牌,更新失败返回"0"
        String newToken =   JWTUtil.updateToken(token);
        logger.info("new.token=="+newToken);

        if ("0".equals(newToken) == false) {
            httpServletResponse.addHeader("token", newToken);
        }

        //返回true则通过过滤器
        logger.info("super.preHandle(request, response)=="+super.preHandle(request, response));
        return super.preHandle(request, response);
    }


    /**
     * 如果方法返回true,则登录成功,且有授权
     * 返回false则执行下面onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            //登录
            executeLogin(request, response);
            // 检查权限
            String url = ((HttpServletRequest) request).getRequestURI();
            getSubject(request, response).checkPermission(url);
        } catch (Exception e) {
            //e.printStackTrace();
            return false; //onAccessDenied
        }
        return true;
    }

    /**
     *登录
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("token");
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse r = (HttpServletResponse) response;
        r.sendError(401, "HttpServletResponse.SC_UNAUTHORIZED");
        return false;
    }
}
