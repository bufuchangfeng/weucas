package com.ucas.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ucas.entity.LibraryUser;
import com.ucas.mapper.LibraryUserMapper;
import com.ucas.service.LibraryUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuchen
 * @since 2019-10-31
 */
@Service
public class LibraryUserServiceImpl extends ServiceImpl<LibraryUserMapper, LibraryUser> implements LibraryUserService {

    public String LoginLibrary(String username, String password){
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
            String url3 = "http://opac.las.ac.cn/F?RN=" + Math.round(Math.random()*1000000000);
//            System.out.println(url3);
            Request request = new Request.Builder().get().url(url3).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

            Pattern pattern = Pattern.compile("http://opac.las.ac.cn:80/F/(.*)\\?func=file&file_name=login-session");
            Matcher m = pattern.matcher(response.body().string());
            if (m.find( )) {
                // System.out.println(m.group(0));

            } else {
                System.out.println("NO MATCH");
                throw new IOException("error");
            }

            String url = m.group(0);

            request = new Request.Builder().get().url(url).build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

//            System.out.println(response.body().string());
            pattern = Pattern.compile("action=\"http://opac.las.ac.cn:80/F/(.*)\"");
            m = pattern.matcher(response.body().string());
            if (m.find()) {
                // System.out.println(m.group(0));
//
            } else {
                System.out.println("NO MATCH");
                throw new IOException("error");
            }
            String url2 = m.group(0).substring(8, m.group(0).length() - 1);
//            System.out.println(url);
//            System.out.println(url2);
            FormBody formBody = new FormBody.Builder()
                    .add("func", "login-session")
                    .add("login_source", "bor-info")
                    .add("bor_id", username)
                    .add("bor_verification", password)
                    .add("bor_library", "CAS50")
                    .build();
            request = new Request.Builder().post(formBody).url(url2)
                    .addHeader("Origin", "http://opac.las.ac.cn")
                    .addHeader("Referer", url)
                    .addHeader("Host", "opac.las.ac.cn")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                    .build();
            response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                // System.out.println(response.body().string());
                String body = response.body().string();

                if(body.contains("证卡条码号")){
//                    System.out.println(body);
                    // logout操作
                    return "success";
                }
                else {
                    return "error";
                }

            }else{
                response.close();
                return "error";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    public  String QueryBorrowedBooks(String username, String password) {
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
            String url3 = "http://opac.las.ac.cn/F?RN=" + Math.round(Math.random() * 1000000000);
//            System.out.println(url3);
            Request request = new Request.Builder().get().url(url3).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

            Pattern pattern = Pattern.compile("http://opac.las.ac.cn:80/F/(.*)\\?func=file&file_name=login-session");
            Matcher m = pattern.matcher(response.body().string());
            if (m.find()) {
                // System.out.println(m.group(0));

            } else {
                System.out.println("NO MATCH");
            }

            String url = m.group(0);

            request = new Request.Builder().get().url(url).build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }

//            System.out.println(response.body().string());
            pattern = Pattern.compile("action=\"http://opac.las.ac.cn:80/F/(.*)\"");
            m = pattern.matcher(response.body().string());
            if (m.find()) {
                // System.out.println(m.group(0));
//
            } else {
                System.out.println("NO MATCH");
            }
            String url2 = m.group(0).substring(8, m.group(0).length() - 1);
//            System.out.println(url);
//            System.out.println(url2);
            FormBody formBody = new FormBody.Builder()
                    .add("func", "login-session")
                    .add("login_source", "bor-info")
                    .add("bor_id", username)
                    .add("bor_verification", password)
                    .add("bor_library", "CAS50")
                    .build();
            request = new Request.Builder().post(formBody).url(url2)
                    .addHeader("Origin", "http://opac.las.ac.cn")
                    .addHeader("Referer", url)
                    .addHeader("Host", "opac.las.ac.cn")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                    .build();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
//                System.out.println(response.body().string());
            }

            response.close();

            request = new Request.Builder().get().url(url2 + "?func=bor-loan&adm_library=CAS50").build();
            response = okHttpClient.newCall(request).execute();
            String body = "";
            if (response.isSuccessful()) {
                body = response.body().string();
                // System.out.println(response.body().string());
                // System.out.println(okHttpClient.cookieJar().toString());
            } else {
                response.close();
                throw new IOException("Unexpected code " + response);
            }
            Document doc = Jsoup.parse(body);
            Elements elements = doc.getElementsByClass("td1");
//            System.out.println(elements.toString());
            Integer i = 0;

            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();

            JSONObject object = new JSONObject();

            pattern = Pattern.compile("\\d+");
            m = pattern.matcher(elements.get(i).text());
            if (m.find()) {
                object.put("book_count", m.group(0));
            } else {

            }

            jsonArray.add(object);

            for (i = 1; i < elements.size(); i = i + 11) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("book_no", elements.get(i).text());
                jsonObject.put("book_name", elements.get(i + 3).text());
                jsonObject.put("book_author", elements.get(i + 2).text());
                jsonObject.put("book_year", elements.get(i + 4).text());
                jsonObject.put("book_time", elements.get(i + 5).text());

                jsonArray2.add(jsonObject);

            }
            jsonArray.add(jsonArray2);

//            System.out.println(jsonArray.toJSONString());
            return jsonArray.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
