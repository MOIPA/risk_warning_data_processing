package com.sichuan.sichuanproject.service.impl;

import com.sichuan.sichuanproject.domain.WarningModelRule;
import com.sichuan.sichuanproject.mapper.WarningModelRuleMapper;
import com.sichuan.sichuanproject.service.WarningModelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */

@Component
public class WarningModelRuleServiceImpl implements WarningModelRuleService {

    @Autowired
    private WarningModelRuleMapper warningModelRuleMapper;

    @Override
    public Integer setWarningModelRule(WarningModelRule warningModelRule) {
        return warningModelRuleMapper.insertWarningModelRule(warningModelRule);
    }
}
