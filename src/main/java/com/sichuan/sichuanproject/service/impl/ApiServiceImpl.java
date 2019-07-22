package com.sichuan.sichuanproject.service.impl;

import com.sichuan.sichuanproject.domain.WarningSignal;
import com.sichuan.sichuanproject.mapper.WarningSignalMapper;
import com.sichuan.sichuanproject.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 */

@Component
public class ApiServiceImpl implements ApiService {

    @Autowired
    private WarningSignalMapper warningSignalMapper;

    @Override
    public List<WarningSignal> getWarningSignal() {
        return warningSignalMapper.getWarningSignal();
    }
}
