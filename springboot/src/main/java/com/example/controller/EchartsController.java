package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.common.enums.OrderStatusEnum;
import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 查询折线图数据
     * @return
     */
    @GetMapping("/line")
    public Result line(){
        // 初始化一个空的字典列表
        List<Dict> dictList = new ArrayList<>();

        // 查询所有已完成的订单，并过滤出状态为"已完成"的订单列表。
        List<Orders> ordersList = ordersMapper.selectAll(new Orders()).stream().filter(orders ->
                OrderStatusEnum.RECEIVED.value.equals(orders.getStatus())).toList();

        // 获取当前日期
        Date date = new Date();
        // 计算过去一周的开始和结束日期
        DateTime start = DateUtil.offsetDay(date, -7);
        DateTime end = DateUtil.offsetDay(date, -1);
        // 生成过去一周每一天的日期列表
        List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
        // 将日期格式化并按升序排序
        List<String> dateList = dateTimes.stream().map(DateUtil::formatDate).sorted(Comparator.naturalOrder()).toList();
        // 遍历每一天，计算该天订单的总金额，并添加到字典列表中
        for (String day : dateList) {
            // 对于每一天，筛选出订单时间包含该天的订单，计算总金额
            BigDecimal total = ordersList.stream().filter(orders -> orders.getTime().contains(day))
                    .map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            // 将日期和对应的总金额作为键值对添加到字典列表中
            dictList.add(Dict.create().set("name", day).set("value", total));
        }

        return Result.success(dictList);
    }

    /**
     * 查询树状图数据
     * @return
     */
    @GetMapping("/bar")
    public Result bar(){
        // 初始化一个空的字典列表
        List<Dict> dictList = new ArrayList<>();

        // 查询所有已完成的订单，并过滤出状态为"已完成"的订单列表。
        List<Orders> ordersList = ordersMapper.selectAll(new Orders()).stream().filter(orders ->
                OrderStatusEnum.RECEIVED.value.equals(orders.getStatus())).toList();

        Set<String> saleList = ordersList.stream().map(Orders::getSaleName).collect(Collectors.toSet());
        for (String sale : saleList) {
            BigDecimal total = ordersList.stream().filter(orders -> sale.equals(orders.getSaleName()))
                    .map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            dictList.add(Dict.create().set("name", sale).set("value", total));
        }

        return Result.success(dictList);

        /*Map<String, BigDecimal> collect = ordersList.stream().collect(Collectors.groupingBy(Orders::getSaleName,
                Collectors.reducing(BigDecimal.ZERO, Orders::getTotal, BigDecimal::add)));

        List<String> nameList = new ArrayList<>();
        List<BigDecimal> totalList = new ArrayList<>();
        for (String key : collect.keySet()) {
            nameList.add(key);
            totalList.add(collect.get(key));
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", nameList);
        resultMap.put("total", totalList);

        return Result.success(resultMap);*/
    }

}
