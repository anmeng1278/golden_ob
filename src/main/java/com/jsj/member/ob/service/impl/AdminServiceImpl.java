package com.jsj.member.ob.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jsj.member.ob.dao.AdminMapper;
import com.jsj.member.ob.entity.Admin;
import com.jsj.member.ob.service.AdminService;
import org.springframework.stereotype.Service;

/**
 *   @description : Admin 服务实现类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-07-17
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
	
}
