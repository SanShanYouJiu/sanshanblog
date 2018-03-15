package xyz.sanshan.main.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = "ueditor_id_file_map")
public class UeditorIdFileMapDO extends BaseDO implements Serializable{


    private static final long serialVersionUID = -2310558671488507061L;
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blod_id;

    private String filename;

    private String extra;

    public UeditorIdFileMapDO() {
    }

    public UeditorIdFileMapDO(Long blod_id, String filename, Date created, Date updated) {
        this.blod_id = blod_id;
        this.filename = filename;
        this.setCreated(created);
        this.setUpdated(updated);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlod_id() {
        return blod_id;
    }

    public void setBlod_id(Long blod_id) {
        this.blod_id = blod_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UeditorIdFileMapDO)) return false;
        if (!super.equals(o)) return false;

        UeditorIdFileMapDO that = (UeditorIdFileMapDO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getBlod_id() != null ? !getBlod_id().equals(that.getBlod_id()) : that.getBlod_id() != null) return false;
        if (getFilename() != null ? !getFilename().equals(that.getFilename()) : that.getFilename() != null)
            return false;
        return getExtra() != null ? getExtra().equals(that.getExtra()) : that.getExtra() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getBlod_id() != null ? getBlod_id().hashCode() : 0);
        result = 31 * result + (getFilename() != null ? getFilename().hashCode() : 0);
        result = 31 * result + (getExtra() != null ? getExtra().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UeditorIdFileMapDO{" +
                "id=" + id +
                ", blod_id=" + blod_id +
                ", filename='" + filename + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }
}