package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 评论表业务处理
 **/
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 新增
     */
    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);

        Integer id = comment.getId();
        //更新root_id
        if (ObjectUtil.isEmpty(comment.getPid())){
            //一级评论
            comment.setRootId(id);
        }else {
            //二级评论(回复)
            Comment parentComment = commentMapper.selectById(comment.getPid());
            comment.setRootId(parentComment.getRootId());
        }
        commentMapper.updateById(comment);
    }

    /**
     * 递归删除评论及其子评论
     */
    public void deleteDeep(int id) {
        // 根据父评论ID查询所有子评论
        List<Comment> childrenList = commentMapper.selectByPid(id);
        // 删除父评论
        commentMapper.deleteById(id);
        // 遍历所有子评论，并递归删除它们
        for (Comment children : childrenList) {
            deleteDeep(children.getId());
        }
    }


    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    /**
     * 前台查询
     */
    public PageInfo<Comment> selectTree(Integer fid, String module, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> rootList = commentMapper.selectRoot(fid, module);
        if (ObjectUtil.isNotEmpty(rootList)){
            for (Comment root : rootList) {
               List<Comment> childrenList = commentMapper.selectByRootId(root.getRootId());
               root.setChildren(childrenList);
            }
         }
        return PageInfo.of(rootList);
    }

    public Integer selectCount(Integer fid, String module) {
        Integer commentCount = commentMapper.selectCount(fid, module);
        return commentCount;
    }

}