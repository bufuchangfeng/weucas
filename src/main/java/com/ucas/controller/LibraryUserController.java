package com.ucas.controller;

import com.ucas.entity.LibraryUser;
import com.ucas.mapper.LibraryUserMapper;
import com.ucas.service.LibraryUserService;

import com.ucas.utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

            try{
                byte[] text = password.getBytes(StandardCharsets.UTF_8);
                byte[] cipherText = AES.encrypt(text, AES.GetKey(username), AES.iv);
                String base64String = Base64.getEncoder().encodeToString(cipherText);
                libraryUser.setPassword(base64String);
                libraryUser.setOpenid(openid);

                libraryUserService.save(libraryUser);

            }catch (Exception e){

            }

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

        try{
            byte[] password = AES.decrypt(Base64.getDecoder().decode(libraryUsers.get(0).getPassword()), AES.GetKey(libraryUsers.get(0).getUsername()),  AES.iv);
            return libraryUserService.QueryBorrowedBooks(libraryUsers.get(0).getUsername(), new String(password, StandardCharsets.UTF_8));

        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
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
