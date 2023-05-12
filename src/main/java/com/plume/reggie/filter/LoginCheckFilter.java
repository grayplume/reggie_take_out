package com.plume.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.plume.reggie.common.BaseContext;
import com.plume.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 检查用户是否登录
* */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1.获取本次请求URL
        String requestURL = request.getRequestURI();
        log.info("拦截到请求:{}",requestURL);
        // 定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg",
                "/doc.html",
                "/web.jars/**",
                "/swagger-resources",
                "v2/api-docs"
        };
        // 2.判断请求是否需要处理
        boolean check = check(urls, requestURL);
        // 3.如果不需要处理,直接放行
        if (check){
            log.info("本次请求不需要处理",requestURL);
            filterChain.doFilter(request,response);
            return;
        }
        // 4-1.判断后台登陆状态,如果已登录,放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登陆:{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        // 4-2.判断后台登陆状态,如果已登录,放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登陆:{}",request.getSession().getAttribute("user"));

            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
        // 5.如果未登录,通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配,检查本次请求是否需要放行
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls,String requestURL){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match){
                return true;
            }
        }
        return false;
    }

}
