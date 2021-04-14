package com.binance.web.controller;

import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @Author wangtianren
 * @Date 2021/4/14 11:18 上午
 * @Description
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/binance/fee")
public class FeiQueryController {
    @Resource
    private RateFeeInfoQuery rateFeeInfoQuery;

    @GetMapping("/test")
    public void test(){
        ArrayList<Object> objects = Lists.newArrayList();
        System.out.println("test");
    }

    @GetMapping("/rate/info")
    public void queryAll(){
        rateFeeInfoQuery.printAllSymbolsFundingRate();
    }

    @GetMapping("/position/info")
    public void queryPositionAll(){
        rateFeeInfoQuery.printPositionAndRateFeeInfo();
    }
}
