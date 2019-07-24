package com.sichuan.sichuanproject.mapper;

import com.sichuan.sichuanproject.domain.WarningSignal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 */

@Mapper
@Repository
public interface WarningSignalMapper {

    /**
     * 插入风险预警信号
     *
     * @param warningSignal
     * @return
     */
    @Insert("insert into JG_FXYJ_WARNING_SIGNAL(Fxyj_Id,Fxyj_Domain_Id,Fxyj_Area_Number,Fxyj_Title,Fxyj_Desc,Fxyj_Level,Fxyj_Time,Fxyj_Model_Id,Fxyj_Detail_Url) values(#{fxyjId},#{fxyjDomainId},#{fxyjAreaNumber},#{fxyjTitle},#{fxyjDesc},#{fxyjLevel},#{fxyjTime},#{fxyjModelId},#{fxyjDetailUrl})")
    int addWarningSignal(WarningSignal warningSignal);

    /**
     * 获取风险预警信号
     *
     * @return
     */
    @Select("select * from JG_FXYJ_WARNING_SIGNAL")
    List<WarningSignal> getWarningSignal();
}
