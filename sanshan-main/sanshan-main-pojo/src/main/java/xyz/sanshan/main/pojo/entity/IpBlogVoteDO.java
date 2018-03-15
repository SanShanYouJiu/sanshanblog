package xyz.sanshan.main.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = " ip_blog_vote")
public class IpBlogVoteDO extends BaseDO implements Serializable{


    private static final long serialVersionUID = -7890519067032690610L;
    @Id
    private Long id;

    private String ip;

    @Column(name = "blog_id")
    private Long blogId;

    private Boolean favour = false;

    private Boolean tread = false;

    private String extra;

    public IpBlogVoteDO() {
    }

    public IpBlogVoteDO(Date created, Date updated, String ip, Long blogId, Boolean vote) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.ip = ip;
        this.blogId = blogId;
        if (vote){
            favour = true;
            tread=false;
        }
        else{
            tread = true;
            favour=false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Boolean getFavour() {
        return favour;
    }

    public void setFavour(Boolean favour) {
        this.favour = favour;
    }

    public Boolean getTread() {
        return tread;
    }

    public void setTread(Boolean tread) {
        this.tread = tread;
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
        if (!(o instanceof IpBlogVoteDO)) return false;
        if (!super.equals(o)) return false;

        IpBlogVoteDO that = (IpBlogVoteDO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getIp() != null ? !getIp().equals(that.getIp()) : that.getIp() != null) return false;
        if (getBlogId() != null ? !getBlogId().equals(that.getBlogId()) : that.getBlogId() != null) return false;
        if (getFavour() != null ? !getFavour().equals(that.getFavour()) : that.getFavour() != null) return false;
        if (getTread() != null ? !getTread().equals(that.getTread()) : that.getTread() != null) return false;
        return getExtra() != null ? getExtra().equals(that.getExtra()) : that.getExtra() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getIp() != null ? getIp().hashCode() : 0);
        result = 31 * result + (getBlogId() != null ? getBlogId().hashCode() : 0);
        result = 31 * result + (getFavour() != null ? getFavour().hashCode() : 0);
        result = 31 * result + (getTread() != null ? getTread().hashCode() : 0);
        result = 31 * result + (getExtra() != null ? getExtra().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IpBlogVoteDO{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", blogId=" + blogId +
                ", favour=" + favour +
                ", tread=" + tread +
                ", extra='" + extra + '\'' +
                '}';
    }
}
