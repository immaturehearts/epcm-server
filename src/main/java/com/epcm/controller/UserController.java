package com.epcm.controller;

import com.epcm.common.HttpUtil;
import com.epcm.common.ReturnCodeBuilder;
import com.epcm.entity.User;
import com.epcm.entity.UserInfo;
import com.epcm.entity.builder.UserBuilder;
import com.epcm.entity.builder.UserInfoBuilder;
import com.epcm.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@Api(tags = {"用户管理"})
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    HttpUtil httpUtil;

    @ApiOperation(
            value = "用户注册",
            notes = "用户注册"
    )
    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> Register (@RequestParam(value = "phone", required = true) String phone,
                                              @RequestParam(value = "password", required = true) String password,
                                              @RequestParam(value = "userName", required = true) String userName,
                                         @RequestParam(value = "verifyCode", required = true) String verifyCode){
        User user = UserBuilder.anUser()
                .withPhone(phone)
                .withPassword(password)
                .withUserName(userName)
                .build();
        Map<String, Object> map = userService.register(user,verifyCode);
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
    }

    @ApiOperation(
            value = "手机密码登录",
            notes = "手机密码登录"
    )
    @RequestMapping(
            value = "login",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> LoginByTelephoneAndPassword (@RequestParam(value = "phone", required = true) String phone,
                                                            @RequestParam(value = "password", required = true) String password){
        String token = userService.loginByTelephoneAndPassword(phone,password);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(map)
                .buildMap();
        ///TODO：抛出异常时如何返回错误码
        ///TODO: 登录后插入数据至登录表
    }

    @ApiOperation(
            value = "用户基本信息上传",
            notes = "用户基本信息上传"
    )
    @RequestMapping(
            value = "/basicInfo",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> BasicInfoUpload (@RequestParam(value = "gender", required = true) Integer gender,
                                         @RequestParam(value = "id_card", required = true) String id_card_number,
                                         @RequestParam(value = "email", required = true) String email,
                                                @RequestParam(value = "true_name", required = true) String true_name,
                                                HttpServletRequest request){
        UserInfo userInfo = UserInfoBuilder.anUserInfo()
                .withGender(gender)
                .withIdCardNumber(id_card_number)
                .withEmailAddr(email)
                .withTrueName(true_name)
                .build();
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(userService.basicInfoUpload(userInfo,httpUtil.getUidByToken(httpUtil.getToken(request))))
                .buildMap();
        ///TODO: 重复上传问题
    }

    @ApiOperation(
            value = "获得个人信息",
            notes = "获得个人信息"
    )
    @RequestMapping(
            value = "/getInfo",
            method = RequestMethod.GET
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(userService.getUserInfo(httpUtil.getUidByToken(httpUtil.getToken(request))))
                .buildMap();
    }

    @ApiOperation(
            value = "修改个人信息",
            notes = "修改个人信息"
    )
    @RequestMapping(
            value = "/modifyInfo",
            method = RequestMethod.POST
    )
    @Transactional(
            rollbackFor = Exception.class
    )
    public Map<String, Object> modifyUserInfo(@RequestParam(value = "gender", required = true) Integer gender,
                                              @RequestParam(value = "id_card", required = true) String id_card_number,
                                              @RequestParam(value = "email", required = true) String email,
                                              @RequestParam(value = "true_name", required = true) String true_name,
                                              HttpServletRequest request) {
        UserInfo userInfo = UserInfoBuilder.anUserInfo()
                .withGender(gender)
                .withIdCardNumber(id_card_number)
                .withEmailAddr(email)
                .withTrueName(true_name)
                .build();
        return ReturnCodeBuilder.successBuilder()
                .addDataValue(userService.modifyUserInfo(userInfo,httpUtil.getUidByToken(httpUtil.getToken(request))))
                .buildMap();
    }
}
