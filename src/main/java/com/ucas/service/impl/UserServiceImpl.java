package com.ucas.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ucas.entity.User;
import com.ucas.mapper.UserMapper;
import com.ucas.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String GetScores(String mail, String password){
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

        try{
            Request request = new Request.Builder().get().url("http://onestop.ucas.ac.cn/home/index").build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

            response.close();

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
                String msg = jsonObject.getString("msg");
                // System.out.println(msg);
                // System.out.println(okHttpClient.cookieJar().toString());

                request = new Request.Builder().get().url(msg).build();

                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    // System.out.println(response.body().string());
                    // System.out.println(okHttpClient.cookieJar().toString());
                } else {
                    response.close();
                    throw new IOException("Unexpected code " + response);
                }

                response.close();

                request = new Request.Builder().get().url("http://sep.ucas.ac.cn/portal/site/226/821").build();

                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    // System.out.println(response.body().string());
                    // System.out.println(okHttpClient.cookieJar().toString());
                } else {
                    response.close();
                    throw new IOException("Unexpected code " + response);
                }

                Pattern pattern = Pattern.compile("http://jwxk.ucas.ac.cn/login\\?Identity=(.*)&");
                Matcher m = pattern.matcher(response.body().string());
                if (m.find( )) {
                    // System.out.println(m.group(1));

                } else {
                    System.out.println("NO MATCH");
                }

                String code = m.group(1);

//                System.out.println(code);

                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/login?Identity=" + code)
                        .addHeader("Origin", "http://onestop.ucas.ac.cn")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("Referer", "http://onestop.ucas.ac.cn/")
                        .addHeader("Host", "jwxk.ucas.ac.cn")
                        .build();
                response = okHttpClient.newCall(request).execute();

//                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/subject/humanityLecture")
//                        .addHeader("Origin", "http://onestop.ucas.ac.cn")
//                        .addHeader("X-Requested-With", "XMLHttpRequest")
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
//                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .addHeader("Referer", "http://onestop.ucas.ac.cn/")
//                        .addHeader("Host", "jwxk.ucas.ac.cn")
//                        .build();
//                response = okHttpClient.newCall(request).execute();
                response.close();
                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn//score/yjs/all")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                        .addHeader("Referer", "http://jwxk.ucas.ac.cn/courseManage/main")
                        .addHeader("Host", "jwxk.ucas.ac.cn")
                        .build();
                response = okHttpClient.newCall(request).execute();


                String body = response.body().string();
//                System.out.println(body);
                Document doc = Jsoup.parse(body);
                Elements elements = doc.getElementsByTag("td");
//                System.out.println(elements.toString());

                JSONObject obj = new JSONObject();
                JSONObject obj1 = new JSONObject();
                obj1.put("stu_name", elements.get(0).text());
                obj1.put("stu_department", elements.get(1).text());
                obj1.put("stu_no", elements.get(2).text());
                obj1.put("stu_major", elements.get(3).text());

                obj.put("stu_info",  obj1);

                Integer i = 0;

                JSONArray jsonArray = new JSONArray();

                for(i = 4; i < elements.size(); i=i+7){
                    JSONObject obj2 = new JSONObject();
                    obj2.put("course_name", elements.get(i).text());
                    obj2.put("course_name_en", elements.get(i+1).text());
                    obj2.put("course_score", elements.get(i+2).text());
                    obj2.put("course_credit", elements.get(i+3).text());
                    obj2.put("course_xuewei", elements.get(i+4).text());
                    obj2.put("course_term", elements.get(i+5).text());
                    obj2.put("course_status", elements.get(i+6).text());
                    jsonArray.add(obj2);
                }

                obj.put("score", jsonArray);

//                System.out.println(obj);
                return obj.toJSONString();
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
