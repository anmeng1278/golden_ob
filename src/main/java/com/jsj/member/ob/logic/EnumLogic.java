package com.jsj.member.ob.logic;

import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.WechatType;
import org.springframework.stereotype.Component;

@Component
public class EnumLogic {

    /**
     * 获取所属类型
     *
     * @param value
     * @return
     */
    public static DictType GetDictType(Integer value) {
        return DictType.valueOf(value);
    }

    /**
     * 获取用户注册来源
     * @param value
     * @return
     */
    public static WechatType GetWechatType(Integer value) {
        return WechatType.valueOf(value);
    }


}
