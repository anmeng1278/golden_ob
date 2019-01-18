package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.entity.Product;

/**
 *   @description : Product 服务接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-21
 */
public interface ProductService extends IService<Product> {

    Integer getMaxSort(int typeId);
}
