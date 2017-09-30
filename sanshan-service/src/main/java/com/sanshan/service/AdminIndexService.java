package com.sanshan.service;

import com.mongodb.WriteResult;
import com.sanshan.dao.MarkDownBlogMapper;
import com.sanshan.dao.UEditorBlogMapper;
import com.sanshan.dao.mongo.UserRepository;
import com.sanshan.pojo.dto.UserDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.pojo.entity.UEditorBlogDO;
import com.sanshan.pojo.entity.UserDO;
import com.sanshan.service.convent.BlogConvert;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.convent.UeditorEditorConvert;
import com.sanshan.service.convent.UserConvert;
import com.sanshan.service.editor.MarkDownBlogService;
import com.sanshan.service.editor.UeditorBlogService;
import com.sanshan.service.vo.BlogVO;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.service.vo.ResponseMsgVO;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import com.sanshan.util.info.PosCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminIndexService {

    @Autowired
    private MarkDownBlogMapper markDownBlogMapper;

    @Autowired
    private UEditorBlogMapper uEditorBlogMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private MarkDownBlogService markDownBlogService;

    @Autowired
    private UeditorBlogService ueditorBlogService;


    private List cacheList;

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
        Collections.sort(list,(o1,o2)->{
            if (o1.getId()>o2.getId())return -1;
            else if (o1.getId().equals(o2.getId())) return 0;
            else return 1;
        });
        cacheList=list;
        return list;
    }


    public List<BlogVO> queryMarkdownBlogAll() {
        List<BlogVO> list;
       if ((list=cacheList)!=null){
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
        if ((list=cacheList)!=null){
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
        String blogLink = mapList.get("blogLink");
        boolean avatarFlag=stringIsNotNull(avatar);
        boolean blogLinFlag=stringIsNotNull(blogLink);
           if (avatarFlag){
               userDO.setAvatar(avatar);
           }
           if (blogLinFlag){
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
            case MarkDown_EDITOR:
                markDownBlogService.updateSelectiveDO(id, content, title, tag);
                responseMsgVO.buildOK();
                return;
            case Void_Id:
                responseMsgVO.buildWithMsgAndStatus(PosCodeEnum.INTER_ERROR, "更新错误 更新节点已被删除");
                throw new NullPointerException("该节点已被删除");
        }
    }

    private  boolean stringIsNotNull(String s){
        if (s!=null&&s!="")
            return true;
        return false;
    }


}
