package com.sichuan.sichuanproject.controller;

import com.sichuan.sichuanproject.domain.WarningModelRule;
import com.sichuan.sichuanproject.service.WarningModelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author
 */

@CrossOrigin
@RestController
public class WarningModelRuleController {

    @Autowired
    private WarningModelRuleService warningModelRuleService;

    @RequestMapping(value = "/risk-warning/warning-model/rule/set", method = RequestMethod.POST)
    public Integer setWarningModelRule(@RequestBody @Valid WarningModelRule warningModelRule) {
        return warningModelRuleService.setWarningModelRule(warningModelRule);
    }
}
