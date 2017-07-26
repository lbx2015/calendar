package net.riking.entity.model;

import javax.persistence.*;

/**
 * Created by bing.xun on 2017/4/6.
 */
@Entity
@Table(name = "T_AML_SuspiciousPropDict")
public class SuspiciousPropDict {

    @Id
    @GeneratedValue
    private Long id;

    private String coder;

    @Column(name = "prop_type")
    private String propType;

    @Column(name = "Rule_Prop_Code")
    private String rulePropCode;

    @Column(name = "Rule_Prop_Name")
    private String rulePropName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoder() {
        return coder;
    }

    public void setCoder(String coder) {
        this.coder = coder;
    }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }

    public String getRulePropCode() {
        return rulePropCode;
    }

    public void setRulePropCode(String rulePropCode) {
        this.rulePropCode = rulePropCode;
    }

    public String getRulePropName() {
        return rulePropName;
    }

    public void setRulePropName(String rulePropName) {
        this.rulePropName = rulePropName;
    }
}
