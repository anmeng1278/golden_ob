package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.airport.AirportDto;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.entity.Airport;
import com.jsj.member.ob.enums.AirportType;
import com.jsj.member.ob.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AirportLogic extends BaseLogic {

    private static AirportLogic airportLogic;

    @PostConstruct
    public void init() {
        airportLogic = this;
    }

    @Autowired
    AirportService airportService;

    /**
     * 实体转换
     *
     * @param entity
     * @return
     */
    public static AirportDto ToDto(Airport entity) {

        AirportDto dto = new AirportDto();

        dto.setAirportCode(entity.getAirportCode());
        dto.setCityId(entity.getCityId());
        dto.setCityName(entity.getCityName());
        dto.setInitials(entity.getInitials());
        dto.setAirportId(entity.getAirportId());

        dto.setAirportName(entity.getAirportName());
        dto.setAirportType(AirportType.valueOf(entity.getTypeId()));
        dto.setIfhot(entity.getIfhot());

        return dto;
    }

    /**
     * 获取贵宾厅列表，按字母排序
     *
     * @return
     */
    public static List<JsAirportDto> GetAirportDtos(AirportType airportType) {

        List<JsAirportDto> dtos = new ArrayList<>();

        Wrapper<Airport> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (airportType != null) {
            wrapper.where("type_id = {0}", airportType.getValue());
        }

        wrapper.orderBy("type_id asc, initials asc, sort asc");

        List<Airport> viphalls = airportLogic.airportService.selectList(wrapper);
        List<AirportDto> vipHallDtos = new ArrayList<>();

        viphalls.forEach(v -> {
            vipHallDtos.add(ToDto(v));
        });

        //字母
        Set<String> chars = vipHallDtos.stream().map(x -> x.getInitials()).collect(Collectors.toSet());

        for (String c : chars) {

            JsAirportDto jsAirportDto = new JsAirportDto();
            jsAirportDto.setInitials(c);

            List<AirportDto> collect = vipHallDtos.stream().filter(v -> v.getInitials().equals(c)).collect(Collectors.toList());
            jsAirportDto.setAirportDtos(collect);

            dtos.add(jsAirportDto);
        }

        return dtos;
    }
}
