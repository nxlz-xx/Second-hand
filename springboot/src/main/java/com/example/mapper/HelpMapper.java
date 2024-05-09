package com.example.mapper;

import com.example.entity.Help;
import java.util.List;

/**
 * 操作help相关数据接口
 */
public interface HelpMapper {

    /**
     * 新增
     */
    int insert(Help help);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Help help);

    /**
     * 根据ID查询
     */
    Help selectById(Integer id);

    /**
     * 查询所有
     */
    List<Help> selectAll(Help help);

    List<Help> selectFrontAll(Help help);
}