package com.hss.controller;

import com.hss.pojo.Notice;
import com.hss.pojo.PageBean;
import com.hss.pojo.Result;
import com.hss.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
@Validated
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页获取所有公告（管理员用）
     */
    @GetMapping("/list")
    public Result<PageBean<Notice>> list(@RequestParam Integer pageNo,
                                         @RequestParam Integer pageSize) {
        PageBean<Notice> pageBean = noticeService.list(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 用户按公告类型分页获取公告（只返回已发布的且未过期的公告，普通用户可访问）
     */
    @GetMapping("/user/list")
    public Result<PageBean<Notice>> listByType(@RequestParam String type,
                                               @RequestParam Integer pageNo,
                                               @RequestParam Integer pageSize) {
        PageBean<Notice> pageBean = noticeService.listByType(type, pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 获取公告详情（用户访问时增加浏览次数）
     */
    @GetMapping("/{id}")
    public Result<Notice> getById(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id, true);
        return Result.success(notice);
    }

    /**
     * 创建公告（默认草稿或已发布由前端传 status 决定，发布人由后端根据当前管理员设置）
     * 仅管理员可调用
     */
    @PostMapping
    public Result create(@RequestBody Notice notice) {
        noticeService.create(notice);
        return Result.success();
    }

    /**
     * 更新公告内容（标题/内容/类型/优先级等），仅管理员
     */
    @PutMapping
    public Result update(@RequestBody Notice notice) {
        noticeService.update(notice);
        return Result.success();
    }

    /**
     * 发布公告：状态置为 1，仅管理员
     */
    @PatchMapping("/{id}/publish")
    public Result publish(@PathVariable Integer id) {
        noticeService.publish(id);
        return Result.success();
    }

    /**
     * 下线公告：状态置为 2，仅管理员
     */
    @PatchMapping("/{id}/offline")
    public Result offline(@PathVariable Integer id) {
        noticeService.offline(id);
        return Result.success();
    }

    /**
     * 删除公告，仅管理员
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.delete(id);
        return Result.success();
    }
}

