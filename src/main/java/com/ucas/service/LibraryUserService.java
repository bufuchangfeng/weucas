package com.ucas.service;

import com.ucas.entity.LibraryUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuchen
 * @since 2019-10-31
 */
@Service
public interface LibraryUserService extends IService<LibraryUser> {
    String LoginLibrary(String username, String password);
    String QueryBorrowedBooks(String username, String password);
}
