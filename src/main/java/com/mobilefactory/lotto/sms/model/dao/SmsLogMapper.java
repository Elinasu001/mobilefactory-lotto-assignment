package com.mobilefactory.lotto.sms.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mobilefactory.lotto.sms.model.vo.SmsLog;

@Mapper
public interface SmsLogMapper {
    int insertSmsLog(SmsLog smsLog);
}
