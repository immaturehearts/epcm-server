package com.epcm.controller;

import com.epcm.common.HttpUtil;
import com.epcm.common.ReturnCodeBuilder;
import com.epcm.entity.PunchInHistory;
import com.epcm.entity.UserPosition;
import com.epcm.entity.builder.PunchInHistoryBuilder;
import com.epcm.entity.builder.UserPositionBuilder;
import com.epcm.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/position")
@Api(tags = {"定位管理"})
@RestController
public class PositionController {
    @Autowired
    PositionService positionService;
    @Autowired
    HttpUtil httpUtil;

    @ApiOperation(
            value = "定位上传",
            notes = "定位上传"
    )
    @RequestMapping(
            value = "/upload",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> PositionUpload (@RequestParam(value = "city", required = true) String city,
                                               @RequestParam(value = "location", required = true) String location,
                                               @RequestParam(value = "longitude", required = true) BigDecimal longitude,
                                               @RequestParam(value = "latitude", required = true) BigDecimal latitude,
                                               HttpServletRequest request){
        UserPosition userPosition = UserPositionBuilder.anUserPosition()
                .withCity(city)
                .withLocation(location)
                .withLongitude(longitude)
                .withLatitude(latitude).build();
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(request));
        Map<String, Object> map = new HashMap<>();
        String hasNearBy = positionService.positionPost(userPosition, uid);
        map.put("nearby", hasNearBy);
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
    }


//    @ApiOperation(
//            value = "查看附近的人",
//            notes = "查看附近的人"
//    )
//    @RequestMapping(
//            value = "/nearby",
//            method = RequestMethod.GET
//    )
//    @Transactional(
//            rollbackFor = Exception.class
//    )
//    public Map<String, Object> getNearBy (@RequestParam(value = "radius", required = true) Integer radius,
//                                          HttpServletRequest httpServletRequest){
//        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
//        Map<String, Object> map = new HashMap<>();
//        String hasNearBy = positionService.getNearBy(radius, uid);
//        map.put("nearby", hasNearBy);
//
//        return ReturnCodeBuilder.successBuilder()
//                .addDataValue(map)
//                .buildMap();
//    }

    @ApiOperation(
            value = "查看附近的人",
            notes = "查看附近的人"
    )
    @RequestMapping(
            value = "/test",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getNearByTest (@RequestParam(value = "longitude", required = true) Double longitude,
                                              @RequestParam(value = "latitude", required = true) Double latitude,
                                              @RequestParam(value = "city", required = true) String city,
                                              @RequestParam(value = "uid", required = true) Long uid,
                                          HttpServletRequest httpServletRequest){
        Map<String, Object> map = new HashMap<>();
        String hasNearBy = positionService.testGetNearBy(longitude, latitude, city, uid);
        map.put("nearby", hasNearBy);

        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
    }
}
