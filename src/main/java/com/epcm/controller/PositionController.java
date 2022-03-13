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
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(positionService.positionPost(userPosition ,httpUtil.getUidByToken(httpUtil.getToken(request))))
                .buildMap();
    }
}
