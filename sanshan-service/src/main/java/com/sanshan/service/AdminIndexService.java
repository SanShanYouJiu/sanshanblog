package com.sanshan.service;

import com.mongodb.WriteResult;
import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.convent.UserConvert;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.user.info.UserInfoService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.exception.NotFoundBlogException;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminIndexService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UeditorBlogService ueditorBlogService;

    @Autowired
    private MarkDownBlogService markDownBlogService;

    private ThreadLocal<List> cacheList = new ThreadLocal<>();

    public List<BlogVO> queryAllBlog() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<BlogVO> list = userInfoService.getUserBlogs(jwtUser.getUsername());
        Collections.sort(list,(o1,o2)->{
            if (o1.getId()>o2.getId()){
                return -1;
            }
            else if (o1.getId().equals(o2.getId())) {
                return 0;
            }
            else{
                return 1;
            }
        });
        cacheList.set(list);
        return list;
    }


    public List<BlogVO> queryMarkdownBlogAll() {
        List<BlogVO> list;
       if ((list=cacheList.get())!=null){
            return list.stream().filter((blogVO) -> blogVO.getType() == 1).collect(Collectors.toList());
        } else {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            list = userInfoService.getUserBlogs(jwtUser.getUsername());
           return list.stream().filter((blogVO) -> blogVO.getType() == 1).collect(Collectors.toList());
        }
    }


    public List<BlogVO> queryUEditorBlogAll() {
        List<BlogVO> list;
        if ((list=cacheList.get())!=null){
            return list.stream().filter((blogVO) -> blogVO.getType() == 0).collect(Collectors.toList());
        } else {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            list = userInfoService.getUserBlogs(jwtUser.getUsername());
            return list.stream().filter((blogVO) -> blogVO.getType() == 0).collect(Collectors.toList());
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
        String blogLink = mapList.get("blogLink");
        boolean avatarFound=stringIsNotNull(avatar);
        boolean blogLinkFound=stringIsNotNull(blogLink);
           if (avatarFound){
               userDO.setAvatar(avatar);
           }
           if (blogLinkFound){
               userDO.setBlogLink(blogLink);
           }
       WriteResult result= userRepository.changeUserInfo(userDO);
        //这里暂时将修改为0的作为更新失败
        return  result.getN()!=0;
    }

    public void updateBlogById(Long id,String title, String tag, String content, ResponseMsgVO responseMsgVO) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        switch (type){
            case UEDITOR_EDITOR:
               ueditorBlogService.updateSelectiveDO(id, content, title, tag);
                responseMsgVO.buildOK();
                return;
            case MARKDOWN_EDITOR:
                markDownBlogService.updateSelectiveDO(id, content, title, tag);
                responseMsgVO.buildOK();
                return;
            case VOID_ID:
                responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "更新错误 更新节点已被删除");
                throw new NotFoundBlogException("该节点已被删除");
            default:
                throw new NotFoundBlogException("更新对应的博客类型不存在");
        }
    }

    private  boolean stringIsNotNull(String s){
        if (s!=null&&s!=""){
            return true;
        }
        return false;
    }


}
