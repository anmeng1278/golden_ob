package com.jsj.member.ob.dao;

import com.jsj.member.ob.entity.Stock;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 *   @description : Stock Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-30
 */
@Repository
public interface StockMapper extends BaseMapper<Stock> {

}