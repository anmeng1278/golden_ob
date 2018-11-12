package com.jsj.member.ob.service.impl;

import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.dao.ProductMapper;
import com.jsj.member.ob.service.ProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *   @description : Product 服务实现类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-12
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
	
}
