package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        /**
         * 处理用户对商品的收藏操作。首先检查该商品是否已经被当前用户收藏，如果是，则删除之前的收藏记录；如果不是，则插入新的收藏记录。
         */
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前用户
        Collect dbCollect = collectMapper.selectByGoodsIdAndUserId(collect.getGoodsId(), currentUser.getId()); // 根据商品ID和用户ID查询收藏信息
        if (ObjectUtil.isNotEmpty(dbCollect)){
            collectMapper.delete(dbCollect.getId()); // 如果存在该收藏记录，则删除
        }else {
            collect.setUserId(currentUser.getId()); // 如果不存在，则设置用户ID
            collect.setCreateTime(DateUtil.now()); // 如果不存在，则设置收藏时间
            collectMapper.insert(collect); // 插入新的收藏记录
        }
    }

    public void deleteById(Integer id) {
        collectMapper.delete(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.delete(id);
        }
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }
}
