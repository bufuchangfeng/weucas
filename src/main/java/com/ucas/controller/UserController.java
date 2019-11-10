package com.ucas.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ucas.entity.User;
import com.ucas.mapper.UserMapper;
import com.ucas.service.UserService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
 * @since 2019-11-01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @RequestMapping(value = "/update_lecture_state", method = RequestMethod.POST)
    public String UpdateLectureState(HttpServletRequest httpServletRequest) {
        String openid = httpServletRequest.getParameter("openid");
        String state = httpServletRequest.getParameter("state");

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();

        wrapper.eq("openid", openid);
        User user = new User();
        user.setLecture(state);
        int rows= userMapper.update(user, wrapper);
        if(rows == 1){
            return "success";
        }
        else{
            return "fail";
        }
    }



    @RequestMapping(value = "/quit", method = RequestMethod.POST)
    public String Quit(HttpServletRequest httpServletRequest) {
        String openid = httpServletRequest.getParameter("openid");
        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);

        Integer result = userMapper.deleteByMap(map);
        return result.toString();
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public String Bind(HttpServletRequest httpServletRequest){
        String openid = httpServletRequest.getParameter("openid");
        String mail = httpServletRequest.getParameter("mail");
        String password = httpServletRequest.getParameter("password");

        System.out.println(openid);
        System.out.println(mail);
        System.out.println(password);

        String res = userService.CheckMailAndPass(mail, password);

        System.out.println(res);

        if(res == "false"){
            // we do nothing here
        }else {

            Map<String, Object> m = new HashMap<>();
            m.put("openid", openid);
            m.put("username", mail);

            List<User> users = userMapper.selectByMap(m);
            if(users.size() == 0){
                User user = new User();
                user.setOpenid(openid);
                user.setUsername(mail);
                user.setPassword(password);
                user.setLecture("0");
                userMapper.insert(user);
            }
            else{
                // we do nothing here.
            }
        }
        return res;
    }

    @RequestMapping(value = "/qqlogin", method = RequestMethod.POST)
    public String QQLogin(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");

        System.out.println(code);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        OkHttpClient okHttpClient = builder.build();

        try {
            String url = "https://api.q.qq.com/sns/jscode2session?appid=1110006992&secret=VfILrzd2U8yfvfPc&js_code=" + code + "&grant_type=authorization_code";

            Request request = new Request.Builder().get().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String body =  response.body().string();
                System.out.println(body);
                return body;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
