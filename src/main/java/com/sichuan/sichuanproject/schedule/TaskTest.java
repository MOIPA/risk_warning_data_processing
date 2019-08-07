package com.sichuan.sichuanproject.schedule;

import com.sichuan.sichuanproject.domain.*;
import com.sichuan.sichuanproject.mapper.PostMapper;
import com.sichuan.sichuanproject.mapper.WarningModelRuleMapper;
import com.sichuan.sichuanproject.mapper.WarningSignalMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author
 */

@Component
@Data
public class TaskTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private WarningModelRuleMapper warningModelRuleMapper;

    @Autowired
    private WarningSignalMapper warningSignalMapper;

    @Autowired
    private RestTemplate restTemplate;

    public void run1(String postTableName, String commentTableName, Long warningModelId){

        //获取今天的日期的字符串，转换为yyyy-MM-dd格式
        Date date = new Date(System.currentTimeMillis());

        produceRiskResult(postTableName, commentTableName, warningModelId, date);

//        RegressionLine line = new RegressionLine();

//        line.getA0();
//        line.getA1();
//        line.getR();

        produceRiskSignal(warningModelId, date);

    }

    private void produceRiskResult(String postTableName, String commentTableName, Long warningModelId, Date date) {
        Float riskValue = 0f;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date);

        List<Post> postList = postMapper.getPostByModelId(postTableName, dateString);
        List<Comment> commentList = postMapper.getCommentByModelId(commentTableName, dateString);
        for (Post post : postList) {
            Float riskValueOfPost = (post.getAttitudesCount() + post.getRepostsCount()) * post.getSentiment();
            riskValue += riskValueOfPost;
        }

        for (Comment comment : commentList) {
            Float riskValueOfComment = (comment.getLikeCount() + comment.getFollowersCount()) * comment.getSentiment();
            riskValue += riskValueOfComment;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String timeString = timeFormat.format(date);

        RiskResult riskResult = new RiskResult(timeString, riskValue, warningModelId);
        int out = postMapper.insertRiskResult(riskResult);
        System.out.println(riskResult);
    }

    private void produceRiskSignal(Long warningModelId, Date date) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String timeString = timeFormat.format(date);
        Date timeOneHourBefore = new Date(date.getTime() - 1000*60*60);
        String timeOneHourBeforeString = timeFormat.format(timeOneHourBefore);

        RiskResult riskResultNow = postMapper.getRiskResultByDate(warningModelId, timeString);
        RiskResult riskResultOneHourBefore = postMapper.getRiskResultByDate(warningModelId, timeOneHourBeforeString);
        WarningModelRule warningModelRule = warningModelRuleMapper.getWarningModelRuleById(warningModelId);

        String fxyjLevel = null;
        Integer level = 4;
        Integer levelForValue = 4;
        Integer levelForIncrement = 4;

        if (riskResultNow.getRiskValue() <= -warningModelRule.getHighRiskValue()) {
            levelForValue = 0;
        }else if (riskResultNow.getRiskValue() <= -warningModelRule.getMiddleHighRiskValue()) {
            levelForValue = 1;
        }else if (riskResultNow.getRiskValue() <= -warningModelRule.getMiddleRiskValue()) {
            levelForValue = 2;
        }else if (riskResultNow.getRiskValue() <= -warningModelRule.getLowRiskValue()) {
            levelForValue = 3;
        }
        if (riskResultOneHourBefore != null) {
            //前一个小时有风险值
            Float increment = riskResultNow.getRiskValue() - riskResultOneHourBefore.getRiskValue();
            if (increment <= -warningModelRule.getHighRiskValueIncrement()) {
                levelForIncrement = 0;
            }else if (increment <= -warningModelRule.getMiddleHighRiskValueIncrement()) {
                levelForIncrement = 1;
            }else if (increment <= -warningModelRule.getMiddleRiskValueIncrement()) {
                levelForIncrement = 2;
            }else if (increment <= -warningModelRule.getLowRiskValueIncrement()) {
                levelForIncrement = 3;
            }
        }

        if (levelForValue <= levelForIncrement) {
            level = levelForValue;
        }else {
            level = levelForIncrement;
        }

        if (level.equals(0)) {
            fxyjLevel = "P0";
        }else if (level.equals(1)) {
            fxyjLevel = "P1";
        }else if (level.equals(2)) {
            fxyjLevel = "P2";
        }else if (level.equals(3)) {
            fxyjLevel = "P3";
        }

        if (fxyjLevel != null) {
            WarningSignal warningSignal = new WarningSignal();
            String fxyjId = "FXYJ_TX_" + date.getTime()/10;
            String fxyjTitle = warningModelRule.getKeyWord() + "风险预警";

            warningSignal.setFxyjId(fxyjId);
            warningSignal.setFxyjDomainId(warningModelRule.getDomainId());
            warningSignal.setFxyjAreaNumber("510000");
            warningSignal.setFxyjTitle(fxyjTitle);
            warningSignal.setFxyjDesc(null);
            warningSignal.setFxyjLevel(fxyjLevel);
            warningSignal.setFxyjTime(new Timestamp(date.getTime()));
            warningSignal.setFxyjModelId(warningModelId.toString());
            warningSignal.setFxyjDetailUrl(null);

            warningSignalMapper.addWarningSignal(warningSignal);
            restTemplate.postForObject("http://59.225.206.13:8751//risk-warning/signal/add", warningSignal, Integer.class);

        }

    }

    private void produceHlwyq(Long warningModelId, Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String timeString = timeFormat.format(date);
        Date timeOneHourBefore = new Date(date.getTime() - 1000*60*60);
        String timeOneHourBeforeString = timeFormat.format(timeOneHourBefore);

        RiskResult riskResultNow = postMapper.getRiskResultByDate(warningModelId, timeString);
        RiskResult riskResultOneHourBefore = postMapper.getRiskResultByDate(warningModelId, timeOneHourBeforeString);
        WarningModelRule warningModelRule = warningModelRuleMapper.getWarningModelRuleById(warningModelId);

        Float riskValueNowAbs = Math.abs(riskResultNow.getRiskValue());
        Float riskValueOneHourBeforeAbs = Math.abs(riskResultOneHourBefore.getRiskValue());

        String eventLevel = null;
        String eventStatus = null;

        //判断是否存在舆情，以及舆情的传播等级
        if (riskValueNowAbs > warningModelRule.getHighRiskValue()) {
            eventLevel = "重度";
        }else if (riskValueNowAbs > warningModelRule.getMiddleRiskValue()) {
            eventLevel = "中度";
        }else if (riskValueNowAbs > warningModelRule.getLowRiskValue()) {
            eventLevel = "轻度";
        }

        if (eventLevel != null) {
            //存在舆情

        }
    }
}
