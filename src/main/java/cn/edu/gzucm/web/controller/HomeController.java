package cn.edu.gzucm.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.gzucm.web.service.WeiboService;

@Controller
public class HomeController {

    @Autowired
    WeiboService weiboService;

    @RequestMapping(value = "/")
    public String home(Model model, HttpServletRequest request) {

        String userId = "1827501013";
        //        weiboService.addUser(userId);
        //        weiboService.addUserFriends(userId);
        //        weiboService.addUserFollowers(userId);
        return "index";
    }
}
