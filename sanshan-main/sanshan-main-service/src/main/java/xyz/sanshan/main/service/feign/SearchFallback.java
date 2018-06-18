package xyz.sanshan.main.service.feign;

import org.springframework.stereotype.Component;
import xyz.sanshan.common.info.PosCodeEnum;
import xyz.sanshan.common.vo.ResponseMsgVO;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UEditorBlogDTO;
import xyz.sanshan.main.pojo.dto.UserDTO;

@Component
public class SearchFallback implements SearchClient {

    @Override
    public ResponseMsgVO userAdd(UserDTO userDTO) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }

    @Override
    public ResponseMsgVO markdownBlogAdd(MarkDownBlogDTO markDownBlogDTO) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }

    @Override
    public ResponseMsgVO ueditorBlogAdd(UEditorBlogDTO UEditorBlogDTO) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }

    @Override
    public ResponseMsgVO userDelete(String id) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }

    @Override
    public ResponseMsgVO markdownBlogDelete(Long id) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }

    @Override
    public ResponseMsgVO ueditorBlogDelete(Long id) {
        return new ResponseMsgVO().buildWithPosCode(PosCodeEnum.FREQUENTLY_REQUEST);
    }


}
