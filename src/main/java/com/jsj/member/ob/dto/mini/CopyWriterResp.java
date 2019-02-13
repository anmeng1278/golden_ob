package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.entity.Copywriter;
import io.swagger.annotations.ApiModelProperty;

public class CopyWriterResp {

    @ApiModelProperty(value = "分享文案", required = true)
    private Copywriter copywriter;

    public Copywriter getCopywriter() {
        return copywriter;
    }

    public void setCopywriter(Copywriter copywriter) {
        this.copywriter = copywriter;
    }
}
