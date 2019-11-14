package com.ucas.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucas.entity.Liblecture;
import com.ucas.entity.Liblecturerecord;
import com.ucas.mapper.LiblectureMapper;
import com.ucas.mapper.LiblecturerecordMapper;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
@RestController
@RequestMapping("/liblecture")
public class LiblectureController {

    @Resource
    private LiblectureMapper liblectureMapper;

    @Resource
    private LiblecturerecordMapper liblecturerecordMapper;

    @RequestMapping(value = "/getLectures", method = RequestMethod.POST)
    public List<Liblecture> GetLectures(HttpServletRequest request){
        String status = request.getParameter("status");
        if(status == "null"){
            // this would never happen now
            return null;
        }else if(status != "已结束"){
            Map<String, Object> m = new HashMap<>();
            m.put("status", status);

            List<Liblecture> liblectures = liblectureMapper.selectByMap(m);
            return liblectures;
        }else{
            Integer stulibid = Integer.valueOf(request.getParameter("stulibid"));
            Map<String, Object> m = new HashMap<>();
            m.put("stulib_id", stulibid);

            List<Liblecturerecord> liblecturerecords = liblecturerecordMapper.selectByMap(m);

            List<Integer> idList = new ArrayList<>();

            for(Integer i = 0; i < liblecturerecords.size(); i++){
                idList.add(liblecturerecords.get(i).getLiblectureId());
            }

            List<Liblecture> liblectures = liblectureMapper.selectBatchIds(idList);

            return liblectures;
        }
    }
}
