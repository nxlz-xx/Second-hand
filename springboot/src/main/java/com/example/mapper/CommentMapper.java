package com.example.mapper;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作comment相关数据接口
 */
public interface CommentMapper {

    /**
     * 新增
     */
    int insert(Comment comment);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Comment comment);

    /**
     * 根据ID查询
     */
    Comment selectById(Integer id);

    /**
     * 查询所有
     */
    List<Comment> selectAll(Comment comment);

    List<Comment> selectRoot(Integer fid, String module);

    List<Comment> selectByRootId(Integer rootId);

    Integer selectCount(Integer fid, String module);

    @Select("select * from comment where pid = #{pid}")
    List<Comment> selectByPid(Integer pid);
}