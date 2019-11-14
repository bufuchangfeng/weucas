package com.ucas.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ucas.entity.LibLectureAdmin;

import com.ucas.entity.Liblecture;
import com.ucas.mapper.LibLectureAdminMapper;
import com.ucas.mapper.LiblectureMapper;
import com.ucas.service.LibLectureAdminService;
import com.ucas.service.LiblectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuchen
 * @since 2019-11-09
 */
@Controller
@RequestMapping("/lib-lecture-admin")
public class LibLectureAdminController {

    @Autowired
    private LibLectureAdminService libLectureAdminService;

    @Autowired
    private LiblectureService liblectureService;

    @Resource
    private LiblectureMapper liblectureMapper;

    @Resource
    private LibLectureAdminMapper mapper;

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/login.html");
        return mv;
    }

    @GetMapping("/lecturelist")
    public ModelAndView LectureList()
    {
        ModelAndView mv = new ModelAndView();

        List<Liblecture> liblectures = liblectureMapper.selectList(null);

//        System.out.println(liblectures.size());

        mv.addObject("liblectures", liblectures);
        mv.setViewName("/admin/lecturelist.html");
        return mv;
    }

    @RequestMapping(value = "/addLecture", method = RequestMethod.POST)
    public String AddLecture(HttpServletRequest request, Model model){
        String name = request.getParameter("name");
        String place = request.getParameter("place");
        String start_time = request.getParameter("start_time");
        String end_time = request.getParameter("end_time");
        String content = request.getParameter("content");

        LibLectureAdmin admin = (LibLectureAdmin)request.getSession().getAttribute("userInfo");

       Liblecture liblecture = new Liblecture();
       liblecture.setName(name);
       liblecture.setPlace(place);
       liblecture.setStatus("未开始");
       liblecture.setWho(admin.getUsername());
       liblecture.setStartTime(start_time);
       liblecture.setEndTime(end_time);
       liblecture.setPosition("");
       liblecture.setContent(content);

       Integer r = liblectureMapper.insert(liblecture);
        if(1 == r){
            return "redirect:/lib-lecture-admin/lecturelist";
        }else if(0 == r){
            return "redirect:/lib-lecture-admin/createLecture";
        }else{
            // it should never happen here
            return null;
        }
    }

    @RequestMapping(value = "/prize", method = RequestMethod.GET)
    public String Prize(HttpServletRequest request){
        return "/admin/prize.html";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String Logout(HttpServletRequest request){
        request.getSession().removeAttribute("userInfo");
        return "/admin/login.html";
    }

    @RequestMapping(value = "/createLecture", method = RequestMethod.GET)
    public String Create(){
        return "/admin/createlecture.html";
    }

    @RequestMapping(value = "/setLectureLocation", method = RequestMethod.POST)
    @ResponseBody
    public String SetLectureLocation(HttpServletRequest request){
        Integer id = Integer.valueOf(request.getParameter("id"));
        String lat = request.getParameter("lat");
        String lng = request.getParameter("lng");

        UpdateWrapper<Liblecture> wrapper = new UpdateWrapper<>();

        wrapper.eq("id", id);

        Liblecture liblecture = new Liblecture();
        liblecture.setPosition(lat + " " + lng);
        int rows= liblectureMapper.update(liblecture, wrapper);
        if(rows == 1){
            return "success";
        }else{
            return "error";
        }
    }

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String dologin(Model model, HttpServletRequest request){

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        List<LibLectureAdmin> libLectureAdmins = mapper.selectByMap(map);

//        System.out.println(libLectureAdmins.size());

        if(libLectureAdmins.size() == 1){
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", libLectureAdmins.get(0));
            return "redirect:/lib-lecture-admin/lecturelist";
        }
        else {
            model.addAttribute("errmsg", "yes");
            return "/admin/login.html";
        }
    }
}
