package com.ucas.controller;

import com.ucas.entity.LibraryUser;
import com.ucas.entity.User;
import com.ucas.mapper.LibraryUserMapper;
import com.ucas.mapper.UserMapper;
import com.ucas.service.LessonService;
import com.ucas.utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private LessonService lessonService;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String GetLessons(HttpServletRequest request){
        String openid = request.getParameter("openid");

        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);

        List<User> users = userMapper.selectByMap(map);
        try{
            byte[] password = AES.decrypt(Base64.getDecoder().decode(users.get(0).getPassword()), AES.GetKey(users.get(0).getUsername()),  AES.iv);
            return lessonService.GetLessons(users.get(0).getUsername(), new String(password, StandardCharsets.UTF_8));

        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
