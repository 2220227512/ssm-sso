package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.po.TbUser;


/**
 * 用户服务端Service接口
 * 完成sso单点登录
* <p>Title: UserService</p>  
* <p>Description: </p>  
* @author 唯  
* @date 2018-1-17
 */
public interface UserService {
	/**
	 * 用户注册信息校验
	 * <p>Title: checkUser</p>  
	 * <p>Description: </p>  
	 * @param content
	 * @param type
	 * @return
	 */
	TaotaoResult checkUser(String content,Integer type);
	/**
	 * 用户注册
	 * <p>Title: createUser</p>  
	 * <p>Description: </p>  
	 * @param user
	 * @return
	 */
	TaotaoResult createUser(TbUser user);
	
	/**
	 * 用户登录
	 * <p>Title: userLogin</p>  
	 * <p>Description: </p>  
	 * @param username
	 * @param password
	 * @return
	 */
	TaotaoResult userLogin(String username,String password,HttpServletRequest request,HttpServletResponse response);
	/**
	 * 根据token获得用户信息
	 * <p>Title: getUserByToken</p>  
	 * <p>Description: </p>  
	 * @param token
	 * @return
	 */
	TaotaoResult getUserByToken(String token);

}

