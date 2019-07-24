package com.sichuan.sichuanproject.service;

import com.sichuan.sichuanproject.domain.WarningSignal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 */

@Service
public interface ApiService {

    /**
     * 获取风险预警信号
     *
     * @return
     */
    List<WarningSignal> getWarningSignal();
}
