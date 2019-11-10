package com.ucas.controller;



import com.ucas.entity.Liblecture;
import com.ucas.mapper.LiblectureMapper;
import com.ucas.service.LiblectureService;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-11-09
 */
@RestController
@RequestMapping("/liblecture")
public class LiblectureController {

    @Resource
    private LiblectureMapper liblectureMapper;

    @RequestMapping(value = "/getLectures", method = RequestMethod.POST)
    public String GetLectures(HttpServletRequest request){
        String status = request.getParameter("status");
        if(status == "null"){
            // this would never happen now
            return status;
        }else{
            Map<String, Object> m = new HashMap<>();
            m.put("status", status);

            List<Liblecture> liblectures = liblectureMapper.selectByMap(m);
            return liblectures.toString();
        }
    }
}
