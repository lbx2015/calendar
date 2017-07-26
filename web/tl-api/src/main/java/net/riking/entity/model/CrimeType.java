package net.riking.entity.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by bing.xun on 2017/5/24.
 */
@Entity
@Table(name = "T_AML_CRIME_TYPE")
public class CrimeType {
    @Id
    @GeneratedValue
    private Long id;

    private String szlx;
    
    private String szkyjyxw;

    private String szkyjyxwdm;

    private Integer fzfs;

    private String type;

    public String getSzlx() {
        return szlx;
    }

    public void setSzlx(String szlx) {
        this.szlx = szlx;
    }

    public String getSzkyjyxw() {
        return szkyjyxw;
    }

    public void setSzkyjyxw(String szkyjyxw) {
        this.szkyjyxw = szkyjyxw;
    }

    public String getSzkyjyxwdm() {
        return szkyjyxwdm;
    }

    public void setSzkyjyxwdm(String szkyjyxwdm) {
        this.szkyjyxwdm = szkyjyxwdm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFzfs() {
        return fzfs;
    }

    public void setFzfs(Integer fzfs) {
        this.fzfs = fzfs;
    }

}
