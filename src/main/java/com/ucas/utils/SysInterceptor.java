package com.ucas.utils;

import com.ucas.entity.LibLectureAdmin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SysInterceptor implements HandlerInterceptor {
    /**
     * 进入拦截器后首先进入的方法
     * 返回false则不再继续执行
     * 返回true则继续执行
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)throws Exception
    {
        HttpSession session=request.getSession();
        LibLectureAdmin userInfo = (LibLectureAdmin) session.getAttribute("userInfo");
        if(userInfo==null)
        {
            response.sendRedirect(request.getContextPath()+"/lib-lecture-admin/login");
            return false;
        }
        return  true;
    }
    /**
     * 生成视图时执行，可以用来处理异常，并记录在日志中
     */
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object arg2, Exception exception){
        //-----------------//
    }

    /** -
     * 生成视图之前执行，可以修改ModelAndView
     */
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object arg2, ModelAndView arg3)
            throws Exception{
        //----------------------------//
    }
}