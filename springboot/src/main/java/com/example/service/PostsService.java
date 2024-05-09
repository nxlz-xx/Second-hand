package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Posts;
import com.example.mapper.PostsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 帖子业务处理
 **/
@Service
public class PostsService {

    @Resource
    private PostsMapper postsMapper;

    /**
     * 新增
     */
    public void add(Posts posts) {
        posts.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        posts.setUserId(currentUser.getId());
        posts.setStatus(StatusEnum.STATUS_WAIT.status);
        postsMapper.insert(posts);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        postsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            postsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Posts posts) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            posts.setStatus(StatusEnum.STATUS_WAIT.status);
        }
        postsMapper.updateById(posts);
    }

    /**
     * 根据ID查询
     */
    public Posts selectById(Integer id) {
        return postsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Posts> selectAll(Posts posts) {
        return postsMapper.selectAll(posts);
    }

    /**
     * 分页查询
     */
    public PageInfo<Posts> selectPage(Posts posts, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())){
            posts.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selectAll(posts);
        return PageInfo.of(list);
    }

    public PageInfo<Posts> selectFrontPage(Posts posts, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Posts> list = postsMapper.selecFrontAll(posts);
        return PageInfo.of(list);
    }

    public void updateCount(Integer id) {
        Posts posts = postsMapper.selectById(id);
        posts.setReadCount(posts.getReadCount() + 1);
        postsMapper.updateById(posts);
    }
}