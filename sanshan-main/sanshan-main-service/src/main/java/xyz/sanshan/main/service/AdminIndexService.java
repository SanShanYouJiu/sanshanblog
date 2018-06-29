package xyz.sanshan.main.service;

import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.sanshan.common.UserContextHandler;
import xyz.sanshan.common.exception.NotFoundBlogException;
import xyz.sanshan.common.info.EditorTypeEnum;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.dao.mongo.UserRepository;
import xyz.sanshan.main.pojo.dto.UserDTO;
import xyz.sanshan.main.pojo.entity.UserDO;
import xyz.sanshan.main.service.convent.UserConvert;
import xyz.sanshan.main.service.editor.BlogIdGenerate;
import xyz.sanshan.main.service.editor.MarkDownBlogService;
import xyz.sanshan.main.service.editor.UEditorBlogService;
import xyz.sanshan.main.service.user.info.UserInfoService;
import xyz.sanshan.main.service.vo.BlogVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
/**
 */
public class AdminIndexService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogIdGenerate blogIdGenerate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UEditorBlogService UEditorBlogService;

    @Autowired
    private MarkDownBlogService markDownBlogService;

    public List<BlogVO> queryAllBlog() {
        String username= UserContextHandler.getUsername();
        List<BlogVO> list = userInfoService.getUserBlogs(username);
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
        return list;
    }


    public List<BlogVO> queryMarkdownBlogAll() {
        List<BlogVO> list;
         String username=  UserContextHandler.getUsername();
            list = userInfoService.getUserBlogs(username);
           return list.stream().filter((blogVO) -> blogVO.getType() == 1).collect(Collectors.toList());
    }


    public List<BlogVO> queryUEditorBlogAll() {
        List<BlogVO> list;
        String username=  UserContextHandler.getUsername();
            list = userInfoService.getUserBlogs(username);
            return list.stream().filter((blogVO) -> blogVO.getType() == 0).collect(Collectors.toList());
    }


    public UserDTO getUserInfo() {
        String username=  UserContextHandler.getUsername();
        UserDO userDO = userRepository.findByUsername(username);
        UserDTO userDTO = UserConvert.doToDto(userDO);
        return userDTO;
    }

    public Boolean changeUserInfo(Map<String,String> mapList ){
        UserDO userDO ;
        String username=  UserContextHandler.getUsername();
        userDO = userRepository.findByUsername(username);
        String avatar = mapList.get("avatar");
        String blogLink = mapList.get("blogLink");
        boolean avatarFound=stringIsNotNull(avatar);
        boolean blogLinkFound=stringIsNotNull(blogLink);
           if (avatarFound){
               userDO.setAvatar(avatar);
               log.info("用户{}更改自己的头像为{}",username,avatar);
           }
           if (blogLinkFound){
                String httpPrefix="http://";
                String httpsPrefix="https://";
                if (blogLink.contains(httpPrefix)||blogLink.contains(httpsPrefix)){
                } else {
                    //猜测是http开头
                    blogLink = httpPrefix + blogLink;
                }
               userDO.setBlogLink(blogLink);
               log.info("用户{}更改自己的博客链接为{}",username,blogLink);
           }
       return changeUserInfo(userDO);
    }

    private Boolean changeUserInfo(UserDO userDO){
        WriteResult result= userRepository.changeUserInfo(userDO);
        return true;
    }

    public void updateBlogById(Long id,String title, String tag, String content, ResponseMsgVO responseMsgVO) {
        EditorTypeEnum type = blogIdGenerate.getType(id);
        switch (type){
            case UEDITOR_EDITOR:
               UEditorBlogService.updateSelectiveDO(id, content, title, tag);
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
