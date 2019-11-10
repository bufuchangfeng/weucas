package com.ucas.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ucas.entity.User;
import com.ucas.mapper.UserMapper;
import com.ucas.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-11-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public String CheckMailAndPass(String mail, String password){
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
            Request request = new Request.Builder().get().url("http://onestop.ucas.ac.cn/home/index").build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                response.close();
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());

            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

            FormBody formBody = new FormBody.Builder()
                    .add("username", mail)
                    .add("password", password)
                    .add("remember", "checked")
                    .build();
            request = new Request.Builder().post(formBody).url("http://onestop.ucas.ac.cn/Ajax/login/0")
                    .addHeader("Origin", "http://onestop.ucas.ac.cn")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("Referer", "http://onestop.ucas.ac.cn/")
                    .build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String f = jsonObject.getString("f");
                return f;
            }else{
                response.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "error";
    }
}
