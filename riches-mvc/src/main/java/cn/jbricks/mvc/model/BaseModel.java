package cn.jbricks.mvc.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by haoting.wang on 2017/2/16.
 */
public class BaseModel implements Serializable {

    // 主键
    private Long    id;

    // 是否激活 1.激活，0.未激活
    private Integer dataStatus;

    // 生成时间
    private Date    gmtCreated;

    // 修改时间
    private Date    gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
