package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collect;
import com.example.entity.Notice;
import com.example.service.CollectService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @RequestMapping("/add")
    public Result add(@RequestBody Collect collect) {
        collectService.add(collect);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collectService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        collectService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Collect collect,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Collect> page = collectService.selectPage(collect, pageNum, pageSize);
        return Result.success(page);
    }

}
