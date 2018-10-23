package com.jsj.member.ob.dto.api.dict;

import java.util.ArrayList;
import java.util.List;

public class GetAreasResp {

    public GetAreasResp() {
        this.areas = new ArrayList<>();
    }

    private List<DictDto> areas;

    public List<DictDto> getAreas() {
        return areas;
    }

    public void setAreas(List<DictDto> areas) {
        this.areas = areas;
    }
}
