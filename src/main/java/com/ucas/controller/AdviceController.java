package com.ucas.controller;


import com.ucas.entity.Advice;
import com.ucas.mapper.AdviceMapper;
import com.ucas.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-11-06
 */

// mybatis-plus 自动生成的是
@RestController
@RequestMapping("/advice")
public class AdviceController {

    @Resource
    private AdviceMapper adviceMapper;

    @Autowired
    private MailService mailService;

    @RequestMapping("/add")
    public String AddAdvice(HttpServletRequest request){
        String content = request.getParameter("content");

        Advice advice = new Advice();
        advice.setContent(content);
        Integer r = adviceMapper.insert(advice);

        if(r == 1){
            mailService.send("924761163@qq.com", "新的建议！", content);
        }
        return r.toString();
    }
}
