package com.ucas.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucas.entity.LibLectureAdmin;
import com.ucas.entity.Liblecture;
import com.ucas.entity.Liblecturerecord;
import com.ucas.entity.LibraryUser;
import com.ucas.mapper.LiblectureMapper;
import com.ucas.mapper.LiblecturerecordMapper;
import com.ucas.mapper.LibraryUserMapper;
import com.ucas.service.LibraryUserService;
import com.ucas.utils.AES;
import com.ucas.utils.PositionComp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    @Resource
    private LibraryUserMapper libraryUserMapper;

    @Autowired
    private LibraryUserService libraryUserService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String Register(HttpServletRequest request){
        // check status of lecture
        Integer liblectureid = Integer.valueOf(request.getParameter("liblectureid"));

        Map<String, Object> map = new HashMap<>();
        map.put("id", liblectureid);

        List<Liblecture> liblectures = liblectureMapper.selectByMap(map);
        if(liblectures.size() != 1){
            return "error";
        }
        // check time
        System.out.println(liblectures.get(0));
        if(!liblectures.get(0).getStatus().equals("进行中")){
            return "不在规定签到时间内！";
        }

        // check distance
        Double lat = Double.valueOf(request.getParameter("latitude"));
        Double lon = Double.valueOf(request.getParameter("longitude"));

        if(liblectures.get(0).getPosition().equals("") || liblectures.get(0).getPosition().equals(null)){
            return "讲座位置还未确定！";
        }

        String[] temp = liblectures.get(0).getPosition().split(" ");
        Double lat1 = Double.valueOf(temp[0]);
        Double lon1 = Double.valueOf(temp[1]);

        if(PositionComp.GetPos(lat, lon, lat1, lon1).equals("sign success!")){
            String openid = request.getParameter("openid");

            map = new HashMap<>();
            map.put("openid", openid);

            List<LibraryUser> libraryUsers = libraryUserMapper.selectByMap(map);
//            System.out.println(libraryUsers);
//            System.out.println(libraryUsers.size());
            if(libraryUsers.size() < 1){
                return "用户信息出错！";
            }

            map = new HashMap<>();
            map.put("liblecture_id", liblectureid);
            map.put("stulib_id", libraryUsers.get(0).getUsername());

            List<Liblecturerecord> liblecturerecords = liblecturerecordMapper.selectByMap(map);
            if(liblecturerecords.size() > 0){
                return "重复签到！";
            }

            Liblecturerecord record = new Liblecturerecord();
            record.setLiblectureId(liblectureid);
            record.setStulibId(libraryUsers.get(0).getUsername());

            Integer res = liblecturerecordMapper.insert(record);
            if(res == 1){
                return "签到成功！";
            }else{
                return "签到失败！请重试！";
            }
        }else{
            return "无效距离！";
        }
    }




    @RequestMapping(value = "/getLectures", method = RequestMethod.POST)
    public List<Liblecture> GetLectures(HttpServletRequest request){
        String status = request.getParameter("status");
        if(status.equals("null")){
            // this would never happen now
            return null;
        }else if(!status.equals("已结束")){
            Map<String, Object> m = new HashMap<>();
            m.put("status", status);

            List<Liblecture> liblectures = liblectureMapper.selectByMap(m);
            return liblectures;
        }else if(status.equals("已结束")){

            String openid = request.getParameter("openid");

            Map<String, Object> map = new HashMap<>();
            map.put("openid", openid);

            List<LibraryUser> libraryUsers = libraryUserMapper.selectByMap(map);


            Map<String, Object> m = new HashMap<>();
            m.put("stulib_id", libraryUsers.get(0).getUsername());

            List<Liblecturerecord> liblecturerecords = liblecturerecordMapper.selectByMap(m);

            List<Integer> idList = new ArrayList<>();

            for(Integer i = 0; i < liblecturerecords.size(); i++){
                idList.add(liblecturerecords.get(i).getLiblectureId());
            }

            List<Liblecture> liblectures = liblectureMapper.selectBatchIds(idList);

            return liblectures;
        }else{
            return null;
        }
    }
}
