package com.ucas.utils;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ucas.entity.Liblecture;
import com.ucas.entity.User;
import com.ucas.mapper.LiblectureMapper;
import com.ucas.mapper.UserMapper;
import com.ucas.service.LiblectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Status transition
 */
@Component
public class CheckLibLectureStatus {

    @Autowired
    private LiblectureService liblectureService;

    @Resource
    private LiblectureMapper liblectureMapper;

    @Resource
    private UserMapper userMapper;

    @Scheduled(cron="* */1 * * * ?")
    private void check(){
        /**
         * 条件构造器
         */
        QueryWrapper<Liblecture> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("status", Arrays.asList("未开始"));
        List<Liblecture> libNotStart = liblectureMapper.selectList(queryWrapper1);

        QueryWrapper<Liblecture> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("status", Arrays.asList("进行中"));
        List<Liblecture> libStarted = liblectureMapper.selectList(queryWrapper2);
//        liblectures.forEach(System.out::println);

        /**
         * 进行中 ---> 已结束
         */
        Integer i;
        for(i = 0; i < libStarted.size(); ++i){
            String endtime = libStarted.get(i).getEndTime();
            Integer diff = TimeComp.GetTime(endtime);
            if(diff < 0){
                Liblecture liblecture = new Liblecture();
                liblecture.setId(libStarted.get(i).getId());
                liblecture.setStatus("已结束");
                liblectureMapper.updateById(liblecture);
            } else {
                continue;
            }
//            libStarted.forEach(System.out::println);
        }

        /**
         * 未开始 ---> 进行中
         */
        for(i = 0; i < libNotStart.size(); ++i){
            String begintime = libNotStart.get(i).getStartTime();
            Integer diff = TimeComp.GetTime(begintime);
            if(diff < 0){
                Liblecture liblecture = new Liblecture();
                liblecture.setId(libNotStart.get(i).getId());
                liblecture.setStatus("进行中");
                liblectureMapper.updateById(liblecture);
            } else {
                continue;
            }
//            System.out.println(diff);
        }
    }
}
