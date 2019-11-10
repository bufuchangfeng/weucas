package com.ucas.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.ucas.entity.Lecture;
import com.ucas.mapper.LectureMapper;
import com.ucas.service.LectureService;
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
 * @since 2019-11-06
 */
@Service
public class LectureServiceImpl extends ServiceImpl<LectureMapper, Lecture> implements LectureService {
    public JSONArray GetLectures(){
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
                    .add("username", "dongyuchen19@mails.ucas.ac.cn")
                    .add("password", "dongyuchen0801%")
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

                response.close();

                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/subject/humanityLecture")
                        .addHeader("Origin", "http://onestop.ucas.ac.cn")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("Referer", "http://onestop.ucas.ac.cn/")
                        .addHeader("Host", "jwxk.ucas.ac.cn")
                        .build();
                response = okHttpClient.newCall(request).execute();

                String body = response.body().string();
//                System.out.println(response.body().string());

//                System.out.println(body);
                Document document = Jsoup.parse(body);

                Elements elements = document.getElementsByTag("td");
//                System.out.println(elements.toString());

                JSONArray jsonArray = new JSONArray();
                for(Integer i = 0; i < elements.size(); i = i + 7){
                    JSONObject obj = new JSONObject();
                    obj.put("讲座名称", elements.get(i).text());
                    obj.put("学时", elements.get(i+1).text());
                    obj.put("讲座时间", elements.get(i+2).text());
                    obj.put("面向对象", elements.get(i+3).text());
                    obj.put("主讲人", elements.get(i+4).text());
                    obj.put("部门", elements.get(i+5).text());
                    jsonArray.add(obj);
                }
//                System.out.println(jsonArray.toJSONString());
                return jsonArray;
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
