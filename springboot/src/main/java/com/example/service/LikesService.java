package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Likes;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    @Autowired
    private LikesMapper likesMapper;

    public void add(Likes likes) {
        /**
         * 处理用户对商品的点赞操作。如果用户已经点赞，则取消点赞；如果未点赞，则进行点赞。
         *
         * @param likes 包含商品ID和点赞信息的对象
         */
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前登录用户
        Likes dbLikes = likesMapper.selectByGoodsIdAndUserId(likes.getGoodsId(), currentUser.getId()); // 查询用户是否已经点赞
        if (ObjectUtil.isNotEmpty(dbLikes)){
            likesMapper.delete(dbLikes.getId()); // 如果已点赞，则删除这条点赞记录
        }else {
            likes.setUserId(currentUser.getId()); // 如果未点赞，则设置用户的ID
            likesMapper.insert(likes); // 插入新的点赞记录
        }
    }

}
