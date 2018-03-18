package xyz.sanshan.main.service.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.pojo.dto.UserDTO;

@FeignClient(name = "sanshan-search", fallback = SearchFallback.class)
public interface SearchClient {

    @PostMapping("/user-info")
    public ResponseMsgVO userAdd(UserDTO userDTO);

    @PostMapping("/markdown-info")
    public ResponseMsgVO markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO);

    @PostMapping("/ueditor-info")
    public ResponseMsgVO ueditorBlogAdd(UeditorBlogDTO ueditorBlogDTO);

    @DeleteMapping("/user-info/{id}")
    public ResponseMsgVO userDelete(@PathVariable(name = "id")String id);

    @DeleteMapping("/markdown-info/{id}")
    public ResponseMsgVO markdownBlogDelete(@PathVariable(name = "id")Long id);

    @DeleteMapping("/ueditor-info/{id}")
    public ResponseMsgVO ueditorBlogDelete(@PathVariable(name = "id")Long id);

}
