package com.example.controller;

import com.example.common.Result;
import com.example.entity.Likes;
import com.example.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @RequestMapping("/add")
    public Result add(@RequestBody Likes likes) {
        likesService.add(likes);
        return Result.success();
    }

}
