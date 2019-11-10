package com.ucas.controller;

import com.ucas.entity.LibraryUser;
import com.ucas.mapper.LibraryUserMapper;
import com.ucas.service.LibraryUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-10-31
 */

@RestController
@RequestMapping("/library-user")
public class LibraryUserController {

    @Resource
    private LibraryUserMapper libraryUserMapper;

    @Autowired
    private LibraryUserService libraryUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String LoginLibrary(HttpServletRequest request){

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String openid = request.getParameter("openid");

        String res = libraryUserService.LoginLibrary(username, password);

        if(res == "success"){

            LibraryUser libraryUser = new LibraryUser();
            libraryUser.setUsername(username);
            libraryUser.setPassword(password);
            libraryUser.setOpenid(openid);

            libraryUserService.save(libraryUser);

            return "success";
        }else if(res == "error"){
            return "error";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "/query_borrowed_books", method = RequestMethod.POST)
    public String QueryBorrowedBooks(HttpServletRequest request){

        String openid = request.getParameter("openid");

        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);

        List<LibraryUser> libraryUsers = libraryUserMapper.selectByMap(map);
        return libraryUserService.QueryBorrowedBooks(libraryUsers.get(0).getUsername(), libraryUsers.get(0).getPassword());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String LogoutLibrary(HttpServletRequest request){

        String openid = request.getParameter("openid");

        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);

        Integer i = libraryUserMapper.deleteByMap(map);

        return i.toString();
    }
}
