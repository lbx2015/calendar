package net.riking.entity.model;

import net.riking.core.annos.Comment;

import javax.persistence.*;

/**
 * Created by bing.xun on 2017/7/4.
 */
@Entity
@Table(name = "T_AML_RuleEngine_Score")
public class AmlRuleEngineScore {
    /** 系统ID */
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    private Long ruleEngineId;

    private Long tranFeaturesId;

    //分值
    private Integer score;

    /** 规则名称 */
    @Comment("规则名称")
    @Column(name = "Rule_Name", length = 255)
    private String ruleName;

    /** 规则编号 */
    @Comment("规则编号")
    @Column(name = "Rule_No", length = 100)
    private String ruleNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRuleEngineId() {
        return ruleEngineId;
    }

    public void setRuleEngineId(Long ruleEngineId) {
        this.ruleEngineId = ruleEngineId;
    }

    public Long getTranFeaturesId() {
        return tranFeaturesId;
    }

    public void setTranFeaturesId(Long tranFeaturesId) {
        this.tranFeaturesId = tranFeaturesId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }
}
