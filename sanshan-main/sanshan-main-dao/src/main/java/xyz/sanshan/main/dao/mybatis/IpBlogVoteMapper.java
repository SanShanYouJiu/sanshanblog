package xyz.sanshan.main.dao.mybatis;

import xyz.sanshan.main.pojo.entity.IpBlogVoteDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 */
public interface IpBlogVoteMapper extends Mapper<IpBlogVoteDO> {
    @Select(" SELECT  ip,blog_id,favour,tread FROM ip_blog_vote WHERE  ip=#{ip}")
    IpBlogVoteDO queryByIp(@Param("ip")String ip);

    @Delete(" Delete FROM ip_blog_vote WHERE  ip=#{ip}&&blog_id=#{blogId}&&tread=1")
    int deleteVoteTreadByBlogId(@Param("ip")String ip,@Param("blogId")Long blogId);

    @Delete(" Delete FROM ip_blog_vote WHERE  ip=#{ip}&&blog_id=#{blogId}&&favour=1")
    int deleteVoteFavourByBlogId(@Param("ip") String ip, @Param("blogId") Long blogId);

    @Delete("Delete FROM ip_blog_vote WHERE blog_id=#{blog_id}")
    int deleteByBlogId(@Param("blog_id") Long blogId);
}
