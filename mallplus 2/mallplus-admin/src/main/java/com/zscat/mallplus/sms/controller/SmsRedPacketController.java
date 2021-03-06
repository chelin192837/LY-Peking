package com.zscat.mallplus.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sms.entity.SmsRedPacket;
import com.zscat.mallplus.sms.service.ISmsRedPacketService;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 红包
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "SmsRedPacketController", description = "红包管理")
@RequestMapping("/sms/SmsRedPacket")
public class SmsRedPacketController {
    @Resource
    private ISmsRedPacketService ISmsRedPacketService;

    @SysLog(MODULE = "sms", REMARK = "根据条件查询所有红包列表")
    @ApiOperation("根据条件查询所有红包列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:read')")
    public Object getSmsRedPacketByPage(SmsRedPacket entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsRedPacketService.page(new Page<SmsRedPacket>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有红包列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "保存红包")
    @ApiOperation("保存红包")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:create')")
    public Object saveSmsRedPacket(@RequestBody SmsRedPacket entity) {
        try {
            if (ISmsRedPacketService.createRedPacket(entity) > 0) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存红包：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "更新红包")
    @ApiOperation("更新红包")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:update')")
    public Object updateSmsRedPacket(@RequestBody SmsRedPacket entity) {
        try {
            if (ISmsRedPacketService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新红包：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "删除红包")
    @ApiOperation("删除红包")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:delete')")
    public Object deleteSmsRedPacket(@ApiParam("红包id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("红包id");
            }
            if (ISmsRedPacketService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除红包：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sms", REMARK = "给红包分配红包")
    @ApiOperation("查询红包明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:read')")
    public Object getSmsRedPacketById(@ApiParam("红包id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("红包id");
            }
            SmsRedPacket coupon = ISmsRedPacketService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询红包明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除红包")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除红包")
    @PreAuthorize("hasAuthority('sms:SmsRedPacket:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ISmsRedPacketService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "sms", REMARK = "领取红包")
    @ApiOperation(value = "领取红包")
    @RequestMapping(value = "/accept/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object accept(@PathVariable("id") Integer id) {
        int count = ISmsRedPacketService.acceptRedPacket(id);
        if (count == 1) {
            return new CommonResult().success("领取成功");
        } else {
            return new CommonResult().failed("你已经领取此红包");
        }
    }
}
