package com.ucas.controller;


import com.ucas.entity.Liblecturerecord;
import com.ucas.mapper.LiblecturerecordMapper;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

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
 * @since 2019-11-14
 */
@Controller
@RequestMapping("/liblecturerecord")
public class LiblecturerecordController {
    @Resource
    private LiblecturerecordMapper liblecturerecordMapper;

    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    public List<Liblecturerecord> Get(HttpServletRequest request){

        Integer libecture_id = Integer.valueOf(request.getParameter("id"));

        Map<String, Object> map = new HashMap<>();
        map.put("liblecture_id", libecture_id);

        return liblecturerecordMapper.selectByMap(map);
    }
}
