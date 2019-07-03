package com.sichuan.sichuanproject.service;

import com.sichuan.sichuanproject.domain.WarningModelRule;
import org.springframework.stereotype.Service;

/**
 * @author
 */

@Service
public interface WarningModelRuleService {


    /**
     * 新增预警模型规则
     *
     * @param warningModelRule
     * @return
     */
    Integer setWarningModelRule(WarningModelRule warningModelRule);
}
