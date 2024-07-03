package com.example.controller;

import com.example.common.Result;
import com.example.entity.Goods;
import com.example.service.GoodsService;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 二手商品前端操作接口
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 新增
     */
    @PostMapping("/add")
    @CacheEvict(cacheNames = "goodsCache", allEntries = true)
    public Result add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @CacheEvict(cacheNames = "goodsCache", allEntries = true)
    public Result deleteById(@PathVariable Integer id) {
        goodsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    @CacheEvict(cacheNames = "goodsCache", allEntries = true)
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        goodsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @CacheEvict(cacheNames = "goodsCache", allEntries = true)
    public Result updateById(@RequestBody Goods goods) {
        goodsService.updateById(goods);
        return Result.success();
    }

    /**
     * 修改浏览量
     */
    @PutMapping("/updateReadCount/{id}")
    @CacheEvict(cacheNames = "goodsCache", allEntries = true)
    public Result updateReadCountById(@PathVariable Integer id) {
        goodsService.updateReadCountById(id);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Goods goods = goodsService.selectById(id);
        return Result.success(goods);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Goods goods) {
        List<Goods> list = goodsService.selectAll(goods);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = goodsService.selectPage(goods, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 前台分页查询
     *
     * @param goods 商品对象，包含分类和排序字段。
     * @return 缓存中或通过查询获取的商品信息。
     */
    /**
     * 使用缓存存储商品信息的方法。(sort不为null)
     * 该方法依据商品的分类和排序方式以及根据页码来进行缓存。
     * 缓存的键构造逻辑为：如果分类不为null，则以'category_' + 分类 + '-' + 'sort_' + 排序方式 + ':' + '页码' + 页码为键；
     * 如果分类为null，则以'category_全部' + '-' + 'sort_' + 排序方式 + ':' + '页码' + 页码为键。
     * 如果返回商品为空，则不缓存。
     * 该方法只有在缓存中未找到对应的商品信息或缓存信息已过期时，才会执行实际的数据库查询操作。
     */
    @Cacheable(cacheNames = "goodsCache",
            key = "#goods.category != null ? '!userId_' + #goods.userId + '-' + 'category_' + #goods.category + '-' + 'sort_' + #goods.sort + ':' + '页码' + #pageNum " +
                    ": '!userId_' + #goods.userId + '-' + 'category_全部' + '-' + 'sort_' +  #goods.sort + ':' + '页码' + #pageNum", unless = "#result.getData().getSize() == 0 ")
    @GetMapping("/selectFrontPage")
    public Result selectFrontPage(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = goodsService.selectFrontPage(goods, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result search(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = goodsService.selectFrontPage(goods, pageNum, pageSize);
        return Result.success(page);
    }

}