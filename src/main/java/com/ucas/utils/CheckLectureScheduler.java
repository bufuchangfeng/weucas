package com.ucas.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ucas.entity.Lecture;
import com.ucas.entity.User;
import com.ucas.mapper.LectureMapper;
import com.ucas.mapper.UserMapper;
import com.ucas.service.LectureService;
import com.ucas.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckLectureScheduler {

    @Autowired
    private MailService mailService;

    @Autowired
    private LectureService lectureService;

    @Resource
    private LectureMapper lectureMapper;

    @Resource
    private UserMapper userMapper;

//    @Scheduled(cron="* */2 * * * ?")
    @Scheduled(cron="* */30 * * * ?")
    private void check(){
        try{
//            System.out.println(lectureService.GetLectures().toJSONString());
            JSONArray jsonArray = lectureService.GetLectures();

//            System.out.println(jsonArray.toJSONString());

            Map<String, Object> map = new HashMap<>();
            map.put("lecture", "1");
            List<User> users = userMapper.selectByMap(map);
            if(users.size() == 0){
                return;
            }else{
                // continue;
            }

            for(Integer i = 0;  i < jsonArray.size(); i++){
                JSONObject object = jsonArray.getJSONObject(i);


                map = new HashMap<>();
                map.put("name", object.getString("讲座名称"));

                List<Lecture> lectures = lectureMapper.selectByMap(map);
                if(lectures.size() == 0){
                    // 发现新讲座
                    Lecture lecture = new Lecture();
                    lecture.setName(object.getString("讲座名称"));
                    lecture.setObjects(object.getString("面向对象"));
                    lecture.setBranch(object.getString("部门"));
                    lecture.setTime(object.getString("讲座时间"));
                    lecture.setWho(object.getString("主讲人"));
                    lecture.setPeriod(object.getString("学时"));
                    Integer r = lectureMapper.insert(lecture);
                    if(1 == r){
                        // success
                        for(Integer j = 0; j < users.size(); j++){
                            mailService.send(users.get(j).getUsername(), "有新的人文讲座发布！", object.toString());
                            // object.toString()可以优化一下。
                        }
                    }else{
                        // fail
                    }
                }else if (lectures.size() == 1){
                    // 讲座已存在，不做任何事情。
                }else{
                    // we do nothing here
                }
            }
            // mailService.send("924761163@qq.com", "hello world!");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
