package com.example.mapper;

import com.example.entity.Likes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface LikesMapper {

    @Insert("insert into likes (goods_id, user_id, module) VALUES (#{goodsId}, #{userId}, #{module})")
    void insert(Likes likes);

    @Delete("delete from likes where id = #{id}")
    void delete(Integer id);

    @Select("select * from likes where goods_id = #{goodsId} and user_id = #{userId}")
    Likes selectByGoodsIdAndUserId(Integer goodsId, Integer userId);

    @Select("select count(*) from likes where goods_id = #{goodsId}")
    int selectLikesCount(Integer goodsId);
}
