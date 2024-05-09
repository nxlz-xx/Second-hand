package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CollectMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.LikesMapper;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二手商品业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 新增
     */
    public void add(Goods goods) {
        goods.setDate(DateUtil.today());
        goods.setStatus(StatusEnum.STATUS_WAIT.status);
        goods.setUserId(TokenUtils.getCurrentUser().getId());
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        //订单中存在该商品且订单没完成或没取消就不能删除该商品
        Orders orders = ordersMapper.selectByGoodsId(id);
        if (ObjectUtil.isNotEmpty(orders)){
            if (!OrderStatusEnum.RECEIVED.value.equals(orders.getStatus()) && !OrderStatusEnum.CANCELLED.value.equals(orders.getStatus())){
                throw new CustomException(ResultCodeEnum.GOODS_ORDER_ERROR);
            }
        }
        goodsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            goodsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            goods.setStatus(StatusEnum.STATUS_WAIT.status);
        }
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        Account currentUser = TokenUtils.getCurrentUser();
        Likes likes = likesMapper.selectByGoodsIdAndUserId(id, currentUser.getId());
        if (ObjectUtil.isNotEmpty(likes)){
            goods.setUserLikes(true);
        }
        int likesCount = likesMapper.selectLikesCount(id, currentUser.getId());
        goods.setLikesCount(likesCount);

        Collect collect = collectMapper.selectByGoodsIdAndUserId(id, currentUser.getId());
        if (ObjectUtil.isNotEmpty(collect)){
            goods.setUserCollect(true);
        }
        int collectCount = collectMapper.selectLikesCount(id, currentUser.getId());
        goods.setCollectCount(collectCount);

        goodsMapper.updateById(goods);

        return goods;

    }
    
    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    /**
     * 前台分页查询
     */
    public PageInfo<Goods> selectFrontPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectFrontAll(goods);
        return PageInfo.of(list);
    }

    /**
     * 修改浏览量
     */
    public void updateReadCountById(Integer id) {
        Goods goods = goodsMapper.selectById(id);
        goods.setReadCount(goods.getReadCount() + 1);
        goodsMapper.updateById(goods);
    }

}