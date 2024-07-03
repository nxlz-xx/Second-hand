package com.example.mapper;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CollectMapper {

    @Insert("insert into collect (goods_id, user_id, module, create_time) VALUES (#{goodsId}, #{userId}, #{module}, #{createTime})")
    void insert(Collect likes);

    @Delete("delete from collect where id = #{id}")
    void delete(Integer id);

    @Select("select * from collect where goods_id = #{goodsId} and user_id = #{userId}")
    Collect selectByGoodsIdAndUserId(Integer goodsId, Integer userId);

    @Select("select count(*) from collect where goods_id = #{goodsId}")
    int selectCollectCount(Integer goodsId);

    List<Collect> selectAll(Collect collect);
}
