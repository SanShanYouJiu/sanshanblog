package com.sanshan.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 */
@Table(name = "blog_vote")
public class BlogVoteDO extends BaseDO implements Serializable{


    private static final long serialVersionUID = -8759168618837796213L;
    @Id
    private Long id;

    @Column(name = "blog_id")
    private Long blogId;

    private Integer favours;

    private Integer treads;

    private String extra;

    public BlogVoteDO() {
    }

    public BlogVoteDO(Date created, Date updated, Long blogId, Integer favours, Integer treads) {
        super.setCreated(created);
        super.setUpdated(updated);
        this.blogId = blogId;
        this.favours = favours;
        this.treads = treads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Integer getFavours() {
        return favours;
    }

    public void setFavours(Integer favours) {
        this.favours = favours;
    }

    public Integer getTreads() {
        return treads;
    }

    public void setTreads(Integer treads) {
        this.treads = treads;
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
        if (!(o instanceof BlogVoteDO)) return false;
        if (!super.equals(o)) return false;

        BlogVoteDO that = (BlogVoteDO) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getBlogId() != null ? !getBlogId().equals(that.getBlogId()) : that.getBlogId() != null) return false;
        if (getFavours() != null ? !getFavours().equals(that.getFavours()) : that.getFavours() != null) return false;
        if (getTreads() != null ? !getTreads().equals(that.getTreads()) : that.getTreads() != null) return false;
        return getExtra() != null ? getExtra().equals(that.getExtra()) : that.getExtra() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getBlogId() != null ? getBlogId().hashCode() : 0);
        result = 31 * result + (getFavours() != null ? getFavours().hashCode() : 0);
        result = 31 * result + (getTreads() != null ? getTreads().hashCode() : 0);
        result = 31 * result + (getExtra() != null ? getExtra().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlogVoteDO{" +
                "id=" + id +
                ", blogId=" + blogId +
                ", favours=" + favours +
                ", treads=" + treads +
                ", extra='" + extra + '\'' +
                '}';
    }
}
