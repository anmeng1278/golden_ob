package com.jsj.member.ob.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jsj.member.ob.dao.CartMapper;
import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *   @description : Cart 服务实现类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Override
    public List<CartProduct> getCartProducts(String unionId) {
        return baseMapper.getCartProducts(unionId);
    }

}
