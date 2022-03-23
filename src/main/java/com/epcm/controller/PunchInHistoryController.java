package com.epcm.controller;

import com.epcm.common.HttpUtil;
import com.epcm.common.ReturnCodeBuilder;
import com.epcm.entity.PunchInHistory;
import com.epcm.entity.UserInfo;
import com.epcm.entity.builder.PunchInHistoryBuilder;
import com.epcm.entity.builder.UserInfoBuilder;
import com.epcm.service.PunchInHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/history")
@Api(tags = {"打卡记录管理"})
@RestController
public class PunchInHistoryController {
    @Autowired
    PunchInHistoryService punchInHistoryService;
    @Autowired
    HttpUtil httpUtil;

    @ApiOperation(
            value = "打卡上传",
            notes = "打卡上传"
    )
    @RequestMapping(
            value = "/punchIn",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> PunchInUpload (@RequestParam(value = "health", required = true) Integer health,
                                                @RequestParam(value = "degree", required = true) String degree,
                                                @RequestParam(value = "vaccine", required = true) Integer vaccine,
                                                @RequestParam(value = "location", required = true) String location,
                                                HttpServletRequest request){
        PunchInHistory punchInHistory = PunchInHistoryBuilder.aPunchInHistory()
                .withHealth(health)
                .withDegree(degree)
                .withVaccine(vaccine)
                .withLocation(location)
                .build();
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(punchInHistoryService.post(punchInHistory,httpUtil.getUidByToken(httpUtil.getToken(request))))
                .buildMap();
    }

    @ApiOperation(
            value = "获取用户打卡记录",
            notes = "获取用户打卡记录"
    )
    @RequestMapping(
            value = "/historyUser",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getUserHistoryListByModifyTimeDESC (@RequestParam(value = "page", required = true) Long page,
                                                                   @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                                   HttpServletRequest httpServletRequest){
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
        List<Map<String, Object>> mapList = punchInHistoryService.getUserHistoryListByModifyTimeDESC(uid,page,pageSize);

        return ReturnCodeBuilder.successBuilder()
                .addDataValue(mapList)
                .addDataCount(punchInHistoryService.getCountByUid(uid))
                .buildMap();
    }

    @ApiOperation(
            value = "获取用户打卡记录总数",
            notes = "获取用户打卡记录总数"
    )
    @RequestMapping(
            value = "/count",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getUserHistoryCount (HttpServletRequest httpServletRequest){
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
        long count = punchInHistoryService.getCountByUid(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);

        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
    }

    @ApiOperation(
            value = "获取所有用户打卡记录",
            notes = "获取所有用户打卡记录"
    )
    @RequestMapping(
            value = "/all/history",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getAllHistoryList (@RequestParam(value = "page", required = true) Long page,
                                                  @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                  HttpServletRequest httpServletRequest){
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
        List<Map<String, Object>> mapList = punchInHistoryService.getAllHistory(uid,page,pageSize);

        return ReturnCodeBuilder.successBuilder()
                .addDataValue(mapList)
                .addDataCount(punchInHistoryService.getAllCount(uid))
                .buildMap();
    }

    @ApiOperation(
            value = "获取所有打卡记录总数",
            notes = "获取所有打卡记录总数"
    )
    @RequestMapping(
            value = "/all/count",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getAllHistoryCount (HttpServletRequest httpServletRequest){
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
        long count = punchInHistoryService.getAllCount(uid);
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);

        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
    }

    @ApiOperation(
            value = "删除户打卡记录",
            notes = "删除打卡记录"
    )
    @RequestMapping(
            value = "/historyUser",
            method = RequestMethod.DELETE
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> deleteUserHistory (@RequestParam(value = "id", required = true) Long id,
                                                  HttpServletRequest httpServletRequest){
        Long uid = httpUtil.getUidByToken(httpUtil.getToken(httpServletRequest));
        punchInHistoryService.deleteById(id, uid);

        return ReturnCodeBuilder.successBuilder().buildMap();
    }
}


