package com.sichuan.sichuanproject.domain;

import lombok.Data;

/**
 * @author
 */

@Data
public class RiskResult {
    private Integer id;
    private String createdAt;
    private Float riskValue;
    private Long warningModelId;

    public RiskResult() {
        //do nothing;
    }

    public RiskResult(String createdAt, Float riskValue, Long warningModelId) {
        this.createdAt = createdAt;
        this.riskValue = riskValue;
        this.warningModelId = warningModelId;
    }
}
