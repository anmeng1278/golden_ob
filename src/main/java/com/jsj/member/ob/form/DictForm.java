package com.jsj.member.ob.form;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@Data
public class DictForm {
    /**
     * 主键
     */
    private Integer dictId;
    /**
     * 名称
     */
    private String dictName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 类型
     */
    private String dictType;
    /**
     * 所属类型
     */
    private Integer parentDictId;
    /**
     * 排序
     */
    private Integer sort;


}
