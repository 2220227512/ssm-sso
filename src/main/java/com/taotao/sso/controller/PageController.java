package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 到注册或登录页面
* <p>Title: PageController</p>  
* <p>Description: </p>  
* @author 唯  
* @date 2018-1-17
 */
@Controller
@RequestMapping("/page")
public class PageController {
	
	@RequestMapping("/register")
	public String register(){
		
		return "register";
	}
	
	@RequestMapping("/login")
	public String login(String redirect,Model model){
		model.addAttribute("redirect", redirect);
		return "login";
	}

}
