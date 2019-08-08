package com.sichuan.sichuanproject.mapper;

import com.sichuan.sichuanproject.domain.Comment;
import com.sichuan.sichuanproject.domain.Post;
import com.sichuan.sichuanproject.domain.RiskResult;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 */

@Mapper
@Repository
public interface PostMapper {

    /**
     * 获取当日博文
     *
     * @param postTableName
     * @param createdAt
     * @return
     */
    @SelectProvider(type = PostProvider.class, method = "getPostFromTableName")
    List<Post> getPostByModelId(@Param("postTableName") String postTableName, @Param("createdAt") String createdAt);

    /**
     * 获取最近几日博文
     *
     * @param postTableName
     * @param createdAt
     * @return
     */
    @SelectProvider(type = PostProvider.class, method = "getSeveralPostFromTableName")
    List<Post> getRecentPostByModelId(@Param("postTableName") String postTableName, @Param("createdAt") String createdAt);

    /**
     * 获取评论
     *
     * @param commentTableName
     * @param createdAt
     * @return
     */
    @SelectProvider(type = PostProvider.class, method = "getCommentFromTableName")
    List<Comment> getCommentByModelId(@Param("commentTableName") String commentTableName, @Param("createdAt") String createdAt);

    /**
     * 获取最近几日评论
     *
     * @param commentTableName
     * @param createdAt
     * @return
     */
    @SelectProvider(type = PostProvider.class, method = "getSeveralCommentFromTableName")
    List<Comment> getRecentCommentByModelId(@Param("commentTableName") String commentTableName, @Param("createdAt") String createdAt);

    /**
     * 插入风险分析结果
     *
     * @param riskResult
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into risk_result_1(created_at,risk_value,warning_model_id) values(#{createdAt},#{riskValue},#{warningModelId})")
    int insertRiskResult(RiskResult riskResult);

    /**
     * 查询结果
     *
     * @param warningModelId
     * @param createdAt
     * @return
     */
    //TODO 异常：产生太多risk_result导致，添加 limit 1
//    @Select("select * from risk_result_1 where warning_model_id = #{warningModelId} and created_at = #{createdAt} limit 1")
    @Select("select * from risk_result_1 where warning_model_id = #{warningModelId} and created_at = #{createdAt}")
    RiskResult getRiskResultByDate(@Param("warningModelId") Long warningModelId, @Param("createdAt") String createdAt);

    class PostProvider {
        public String getPostFromTableName(@Param("postTableName") String postTableName, @Param("createdAt") String createdAt) {
            String sql = "select * from ${postTableName} p where p.created_at = #{createdAt}";

            return sql;
        }

        public String getCommentFromTableName(@Param("commentTableName") String commentTableName, @Param("createdAt") String createdAt) {
            String sql = "select c.comment_id, c.post_id, c.text, c.created_at, c.like_count, c.user_id, c.screen_name, c.profile_url, c.description, c.gender, c.followers_count, c.sentiment" +
                    " from ${commentTableName} c where c.created_at = #{createdAt}";
            System.out.println(sql);
            return sql;
        }

        public String getSeveralPostFromTableName(@Param("commentTableName") String commentTableName, @Param("createdAt") String createdAt) {
            String sql = "select c.comment_id, c.post_id, c.text, c.created_at, c.like_count, c.user_id, c.screen_name, c.profile_url, c.description, c.gender, c.followers_count, c.sentiment" +
                    " from ${commentTableName} c where c.created_at >= #{createdAt}";
            System.out.println(sql);
            return sql;
        }
        public String getSeveralCommentFromTableName(@Param("commentTableName") String commentTableName, @Param("createdAt") String createdAt) {
            String sql = "select c.comment_id, c.post_id, c.text, c.created_at, c.like_count, c.user_id, c.screen_name, c.profile_url, c.description, c.gender, c.followers_count, c.sentiment" +
                    " from ${commentTableName} c where c.created_at >= #{createdAt}";
            System.out.println(sql);
            return sql;
        }
    }
}
