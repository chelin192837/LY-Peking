package com.zscat.mallplus.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.sms.entity.SmsGroup;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface SmsGroupMapper extends BaseMapper<SmsGroup> {
    SmsGroup getByGoodsId(Long goodsId);
}
