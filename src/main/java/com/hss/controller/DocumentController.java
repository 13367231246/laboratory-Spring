package com.hss.controller;

import com.hss.pojo.Document;
import com.hss.pojo.PageBean;
import com.hss.pojo.Result;
import com.hss.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@Validated
public class DocumentController {

    
    @Autowired
    private DocumentService documentService;

      /**
     * 分页获取所有文档（管理员用）
     */
      @GetMapping("/list")
      public Result<PageBean<Document>> list(@RequestParam Integer pageNo,
                                             @RequestParam Integer pageSize) {
          PageBean<Document> pageBean = documentService.list(pageNo, pageSize);
          return Result.success(pageBean);
      }

    /**
     * 用户按文档类型分页获取文档（只返回已发布的文档，普通用户可访问）
     */
    @GetMapping("/user/list")
    public Result<PageBean<Document>> listByDocType(@RequestParam String docType,
                                                     @RequestParam Integer pageNo,
                                                     @RequestParam Integer pageSize) {
        PageBean<Document> pageBean = documentService.listByDocType(docType, pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 获取文档详情
     */
    @GetMapping("/{id}")
    public Result<Document> getById(@PathVariable Integer id) {
        Document document = documentService.getById(id);
        return Result.success(document);
    }

    /**
     * 创建文档（默认草稿或已发布由前端传 status 决定，发布人由后端根据当前管理员设置）
     * 仅管理员可调用
     */
    @PostMapping
    public Result create(@RequestBody Document document) {
        documentService.create(document);
        return Result.success();
    }

    /**
     * 更新文档内容（标题/内容/类型/版本等），仅管理员
     */
    @PutMapping
    public Result update(@RequestBody Document document) {
        documentService.update(document);
        return Result.success();
    }

    /**
     * 发布文档：状态置为 1，仅管理员
     */
    @PatchMapping("/{id}/publish")
    public Result publish(@PathVariable Integer id) {
        documentService.publish(id);
        return Result.success();
    }

    /**
     * 下线文档：状态置为 0，仅管理员
     */
    @PatchMapping("/{id}/offline")
    public Result offline(@PathVariable Integer id) {
        documentService.offline(id);
        return Result.success();
    }

    /**
     * 删除文档，仅管理员
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        documentService.delete(id);
        return Result.success();
    }
}


