package com.sichuan.sichuanproject.mapper;

import com.sichuan.sichuanproject.domain.WarningModelRule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author
 */

@Mapper
@Repository
public interface WarningModelRuleMapper {

    /**
     * 新增预警模型规则
     *
     * @param warningModelRule
     * @return
     */
    @Insert("insert into warning_model_rule(warning_model_id, domain_id, key_word, start_time, high_risk_value, middle_high_risk_value, middle_risk_value, low_risk_value, high_risk_value_increment, middle_high_risk_value_increment, middle_risk_value_increment, low_risk_value_increment) values(#{warningModelId},#{domainId},#{keyWord},#{startTime},#{highRiskValue},#{middleHighRiskValue},#{middleRiskValue},#{lowRiskValue},#{highRiskValueIncrement},#{middleHighRiskValueIncrement},#{middleRiskValueIncrement},#{lowRiskValueIncrement})")
    int insertWarningModelRule(WarningModelRule warningModelRule);

    /**
     * 获取预警模型规则
     *
     * @param warningModelId
     * @return
     */
    @Select("select * from warning_model_rule where warning_model_id =#{warningModelId}")
    WarningModelRule getWarningModelRuleById(@Param("warningModelId") Long warningModelId);
}
