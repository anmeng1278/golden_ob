package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *   @description : Product Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-21
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

   Integer getMaxSort(@Param("typeId") int typeId);
}