package com.sanshan.service;

import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFSDBFile;
import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UEditorBlogMapper;
import com.sanshan.dao.mongo.FileOperation;
import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.convent.BlogConvert;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.convent.UserConvert;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminIndexService {

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UEditorBlogMapper uEditorBlogMapper;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;



    private final String cacheBlogName = "tempBlogList";


    public List<BlogVO> queryAllBlog() {
        List<BlogVO> list = new LinkedList<>();
        MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
        UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        markDownBlogDO.setUser(jwtUser.getUsername());
        uEditorBlogDO.setUser(jwtUser.getUsername());

        List<MarkDownBlogDO> markDownBlogDOList = markDownBlogMapper.queryByUser(markDownBlogDO);
        List<UEditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.queryByUser(uEditorBlogDO);

        list.addAll(BlogConvert.MarkdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogDOList)));
        list.addAll(BlogConvert.UeditorDoToDtoList(UeditorEditorConvert.doToDtoList(uEditorBlogDOS)));

        //加入缓存
        redisTemplate.opsForValue().set(cacheBlogName, list, 3, TimeUnit.MINUTES);
        return list;
    }


    public List<BlogVO> queryMarkdownBlogAll() {
        List<BlogVO> list;
        if ((list = (List<BlogVO>) redisTemplate.opsForValue().get(cacheBlogName)) != null) {
            return list.stream().filter((blogVO) -> blogVO.getType() == 1).collect(Collectors.toList());
        } else {
            list = new LinkedList<>();
            MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            markDownBlogDO.setUser(jwtUser.getUsername());
            List<MarkDownBlogDO> markDownBlogDOList = markDownBlogMapper.queryByUser(markDownBlogDO);
            list.addAll(BlogConvert.MarkdownDoToDtoList(MarkDownEditorConvert.doToDtoList(markDownBlogDOList)));
            return list;
        }
    }


    public List<BlogVO> queryUEditorBlogAll() {
        List<BlogVO> list;
        if ((list = (List<BlogVO>) redisTemplate.opsForValue().get(cacheBlogName)) != null) {
            return list.stream().filter((blogVO) -> blogVO.getType() == 0).collect(Collectors.toList());
        } else {
            list = new LinkedList<>();
            UEditorBlogDO uEditorBlogDO = new UEditorBlogDO();
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            uEditorBlogDO.setUser(jwtUser.getUsername());
            List<UEditorBlogDO> uEditorBlogDOS = uEditorBlogMapper.queryByUser(uEditorBlogDO);
            list.addAll(BlogConvert.UeditorDoToDtoList(UeditorEditorConvert.doToDtoList(uEditorBlogDOS)));
            return list;
        }
    }


    public UserDTO getUserInfo() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDO userDO = userRepository.findByUsername(jwtUser.getUsername());
        UserDTO userDTO = UserConvert.doToDto(userDO);
        return userDTO;
    }

    public Boolean changeUserInfo(String username,Map<String,String> mapList ){
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        String avatar = mapList.get("avatar");
        String email = mapList.get("email");
        String blogLink = mapList.get("blogLink");
        boolean avatarFlag=stringIsNotNull(avatar);
        boolean emailFlag= stringIsNotNull(email);
        boolean blogLinFlag=stringIsNotNull(blogLink);
           if (avatarFlag){
               userDO.setAvatar(avatar);
           }
           if (emailFlag){
               userDO.setEmail(email);
           }
           if (blogLinFlag){
               userDO.setBlogLink(blogLink);
           }
       WriteResult result= userRepository.changeUserInfo(userDO);
        //这里暂时将修改为0的作为更新失败
        return  result.getN()!=0;
    }

    private  boolean stringIsNotNull(String s){
        if (s!=null&&s!="")
            return true;
        return false;
    }


}
