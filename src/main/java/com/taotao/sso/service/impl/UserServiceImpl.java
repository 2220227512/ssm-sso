package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.po.TbUser;
import com.taotao.po.TbUserExample;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	//用户存redisd的session信息
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	
	//#用户存redis的sessio过期时间
	@Value("${REDIS_SESSION_PS}")
	private Integer REDIS_SESSION_PS;
	
	@Autowired
	private JedisClient jedisClient;

	@Override
	public TaotaoResult checkUser(String content, Integer type) {
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		// 对数据进行校验：1、2、3分别代表username、phone、email
		// 用户名校验
		if (1 == type) {
			criteria.andUsernameEqualTo(content);
			// 电话校验
		} else if (2 == type) {
			criteria.andPhoneEqualTo(content);
			// email校验
		} else {
			criteria.andEmailEqualTo(content);
		}
		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	@Override
	public TaotaoResult createUser(TbUser user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword()
				.getBytes()));
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult userLogin(String username, String password,HttpServletRequest request,HttpServletResponse response) {
		TbUserExample example = new TbUserExample();
		TbUserExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> lists = userMapper.selectByExample(example);
		if (lists.size() == 0 || lists == null) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = lists.get(0);
		if (!user.getPassword()
				.equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {

			return TaotaoResult.build(400, "用户名或密码错误");
		}
		//密码清除
		user.setPassword(null);
		//token
		String token =UUID.randomUUID().toString();
		jedisClient.set(REDIS_USER_SESSION_KEY+":"+token, JsonUtils.objectToJson(user));
	    jedisClient.expire(REDIS_USER_SESSION_KEY+":"+token, REDIS_SESSION_PS);
	    
	    //写cookie
	    CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(REDIS_USER_SESSION_KEY+":"+token);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "session已经过期,请从新登录");
		}
		 jedisClient.expire(REDIS_USER_SESSION_KEY+":"+token, REDIS_SESSION_PS);
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}

}
