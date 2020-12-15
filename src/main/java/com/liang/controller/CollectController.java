package com.liang.controller;

import com.liang.common.lang.Result;
import com.liang.dto.CollectDto;

import com.liang.service.CollectService;
import com.liang.utils.BlogConstant;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author LiangYonghui
 * @date 2020/10/31 14:04
 * @description 收藏文章
 */
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController implements BlogConstant{
    @Autowired
    private HttpServletRequest rep;

    @Autowired
    private CollectService collectService;



    @PostMapping("/add")
    public Result collect(@RequestBody CollectDto collectDto) {
        System.out.println("========================");
        System.out.println(collectDto);
        Long userId = getUserId(req);
        collectDto.setUserId(userId);



        return collectService.collect(collectDto);
    }


    @PostMapping("/is_collect")
    public Result isCollect(@RequestBody CollectDto collectDto) {
        System.out.println("is_collect");
        Long userId = getUserId(req);
        collectDto.setUserId(userId);
        Result collect = collectService.isCollect(collectDto);
        Object data = collect.getData();
        System.out.println(data);
        return collectService.isCollect(collectDto);
    }

    @GetMapping("/get_collect_count/{id}")
    public Result getCollectCount(@PathVariable String id) {
        System.out.println(id);
        return collectService.getCollectCount(BlogConstant.ENTITY_TYPE_ARTICLE, Long.parseLong(id));
    }
}
