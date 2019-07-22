package com.sichuan.sichuanproject.controller;

import com.sichuan.sichuanproject.domain.WarningSignal;
import com.sichuan.sichuanproject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author
 */

@CrossOrigin
@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @RequestMapping(value = "/risk-warning/warning-signal/get", method = RequestMethod.GET)
    public List<WarningSignal> getWarningSignal() {
        return apiService.getWarningSignal();
    }
}
