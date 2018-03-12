package xyz.sanshan.pojo.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = "ueditor_file_quote")
public class UeditorFileQuoteDO extends BaseDO implements Serializable{


    private static final long serialVersionUID = 8290646201408849806L;
    @Id
    private Long id;

    private String filename;

    private Integer quote;

    private String extra;

    public UeditorFileQuoteDO() {
    }

    public UeditorFileQuoteDO(String filename, Integer quote, Date created, Date updated){
        this.filename=filename;
        this.quote=quote;
        this.setCreated(created);
        this.setUpdated(updated);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getQuote() {
        return quote;
    }

    public void setQuote(Integer quote) {
        this.quote = quote;
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
        if (!(o instanceof UeditorFileQuoteDO)) return false;
        if (!super.equals(o)) return false;

        UeditorFileQuoteDO that = (UeditorFileQuoteDO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getFilename() != null ? !getFilename().equals(that.getFilename()) : that.getFilename() != null)
            return false;
        if (getQuote() != null ? !getQuote().equals(that.getQuote()) : that.getQuote() != null) return false;
        return getExtra() != null ? getExtra().equals(that.getExtra()) : that.getExtra() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getFilename() != null ? getFilename().hashCode() : 0);
        result = 31 * result + (getQuote() != null ? getQuote().hashCode() : 0);
        result = 31 * result + (getExtra() != null ? getExtra().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UeditorFileQuoteDO{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", quote=" + quote +
                ", extra='" + extra + '\'' +
                '}';
    }
}
