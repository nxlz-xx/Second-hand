package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.mapper.AddressMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 新增订单
     */
    public void add(Orders orders) {
        // 获取当前登录的用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        // 根据订单中的商品ID查询商品信息
        Goods goods = goodsMapper.selectById(orders.getGoodsId());
        // 根据订单中的地址ID查询地址信息
        Address address = addressMapper.selectById(orders.getAddressId());

        // 构建一个新的订单对象，包含完整的订单信息，准备插入数据库
        Orders order = Orders.builder()
                .goodsId(goods.getId())
                .goodsName(goods.getName()) // 商品名称
                .goodsImg(goods.getImg()) // 商品图片
                .saleId(goods.getUserId()) // 商品销售者的用户ID
                .total(goods.getPrice()) // 商品总价
                .address(address.getAddress()) // 收货地址
                .userName(address.getName()) // 收货人姓名
                .phone(address.getPhone()) // 收货人电话
                .userId(currentUser.getId()) // 当前登录用户的ID
                .status(OrderStatusEnum.UNPAID.value) // 订单状态，未支付
                .orderNo(System.currentTimeMillis() + RandomUtil.randomNumbers(7)) // 生成订单编号
                .time(DateUtil.now()) // 订单创建时间
                .build();

        // 插入构建好的订单到数据库
        ordersMapper.insert(order);

        //发送延迟消息到延迟交换机，订单超时自动取消订单
        /*rabbitTemplate.convertAndSend("orderDelay.direct","orderDelay",
                order.getId(), message -> {
                    // 设置延迟时间5分钟
                    message.getMessageProperties().setHeader("x-delay", 1000*60*5);
                    return message;
                });*/
    }


    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        if (RoleEnum.USER.name().equals(TokenUtils.getCurrentUser().getRole())){
            orders.setUserId(TokenUtils.getCurrentUser().getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

    public Orders selectByOrderNo(String orderNo) {
        return ordersMapper.selectByOrderNo(orderNo);
    }

    public PageInfo<Orders> selectSalePage(Orders orders, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            orders.setSaleId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }
}