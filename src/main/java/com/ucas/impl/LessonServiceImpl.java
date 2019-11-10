package com.ucas.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ucas.service.LessonService;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LessonServiceImpl implements LessonService {
    public String GetLessons(String username, String password){
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
                    .add("username", username)
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

//                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/subject/humanityLecture")
//                        .addHeader("Origin", "http://onestop.ucas.ac.cn")
//                        .addHeader("X-Requested-With", "XMLHttpRequest")
//                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
//                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                        .addHeader("Referer", "http://onestop.ucas.ac.cn/")
//                        .addHeader("Host", "jwxk.ucas.ac.cn")
//                        .build();
//                response = okHttpClient.newCall(request).execute();

                request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/course/personSchedule")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                        .addHeader("Referer", "http://jwxk.ucas.ac.cn/courseManage/main")
                        .addHeader("Host", "jwxk.ucas.ac.cn")
                        .build();
                response = okHttpClient.newCall(request).execute();


                String body = response.body().string();
//                System.out.println(body);
                Document doc = Jsoup.parse(body);
                Elements elements = doc.getElementsByTag("td");
                //            System.out.println(elements.toString());
                Integer i = 0;

                JSONArray ja = new JSONArray();
                JSONObject jb = new JSONObject();

                List<String> strings = new ArrayList<String>();
                for (i = 0; i < elements.size(); i++) {
                    String href = elements.get(i).select("a").attr("href");
//                    System.out.println(href);
//                    System.out.println(elements.get(i).text());
                    if(strings.contains(elements.get(i).text())){
                        continue;
                    }
                    if(href.equals(""))
                    {
                        continue;
                    }
                    strings.add(elements.get(i).text());
                    request = new Request.Builder().get().url("http://jwxk.ucas.ac.cn/" + href)
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                            .addHeader("Host", "jwxk.ucas.ac.cn")
                            .build();
                    response = okHttpClient.newCall(request).execute();
                    body = response.body().string();
                    doc = Jsoup.parse(body);
                    Elements e = doc.getElementsByTag("tr");
                    ja = new JSONArray();
                    for(Integer l = 0; l < e.size(); l = l + 3){
                        jsonObject = new JSONObject();
                        jsonObject.put(e.get(l).select("th").text(), e.get(l).select("td").text());
                        jsonObject.put(e.get(l+1).select("th").text(), e.get(l+1).select("td").text());
                        jsonObject.put(e.get(l+2).select("th").text(), e.get(l+2).select("td").text());

                        ja.add(jsonObject);

//                        System.out.println(jsonObject.toString());
//                        System.out.println(i.toString() + "" + elements.get(i).text());
                    }
                    jb.put(elements.get(i).text(), ja.toString());

//                    System.out.println(i.toString() + " " + elements.get(i).text());
                }

//                System.out.println(jb);

                JSONArray jsonArray = new JSONArray();
                JSONObject object = null;

                List<Integer> integers = new ArrayList<Integer>();

                for(i = 0; i < elements.size(); i++){
                    integers.add(0);
                }
                int f = 0;
                Integer j = 0;
                int cnt = 0;
                for(j = 0; j < elements.size(); j++){
                    if(integers.get(j) == 1){
                        continue;
                    }
                    integers.set(j, 1);
                    if(elements.get(j).text().equals("")){
                        continue;
                    }
                    cnt = 1;
                    int k = j + 7;
                    f = 0;
                    while(k < elements.size()){
                        if(elements.get(j).text().equals(elements.get(k).text())){
                            cnt++;
                            integers.set(k, 1);
                            k += 7;

                        }
                        else{
                            object = new JSONObject();
                            object.put("kcmc", elements.get(j).text());
                            object.put("xqj", j%7+1);
                            object.put("skjc", j/7 + 1);
                            object.put("skcd", cnt);
                            object.put("time", jb.getJSONArray(elements.get(j).text()));
//                            System.out.println(object.toJSONString());

                            jsonArray.add(object);
                            f = 1;
                            break;
                        }
                    }
                    if(f == 0){
                        if(cnt != 1){
                            object = new JSONObject();
                            object.put("kcmc", elements.get(j).text());
                            object.put("xqj", j%7+1);
                            object.put("skjc", j/7 + 1);
                            object.put("skcd", cnt);
                            object.put("time", jb.getJSONArray(elements.get(j).text()));



                            jsonArray.add(object);

//                            System.out.println(object.toJSONString());
                        }
                    }
                }
//                System.out.println(jsonArray);
                return jsonArray.toJSONString();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
