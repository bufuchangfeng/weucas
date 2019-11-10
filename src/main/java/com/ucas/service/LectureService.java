package com.ucas.service;

import com.alibaba.fastjson.JSONArray;
import com.ucas.entity.Lecture;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-11-06
 */
public interface LectureService extends IService<Lecture> {
    JSONArray GetLectures();
}
