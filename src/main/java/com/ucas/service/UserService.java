package com.ucas.service;

import com.ucas.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-11-01
 */
public interface UserService extends IService<User> {
    String CheckMailAndPass(String mail, String password);
}
