package xyz.sanshan.main.service.convent;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import xyz.sanshan.main.pojo.dto.CommonBlogDTO;
import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.service.vo.BlogVO;

import java.util.LinkedList;
import java.util.List;

public class BlogConvert {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    /**
     * 将markDownBlogDO List转换为BlogVO List
     * @param markDownBlogDOS
     * @return
     */
    public static List<BlogVO> markdownDoToDtoList(List<MarkDownBlogDTO> markDownBlogDOS) {
        List<BlogVO> blogVOS = new LinkedList<>();
        for (int i = 0; i <markDownBlogDOS.size() ; i++) {
             blogVOS.add(new BlogVO(markDownBlogDOS.get(i)));
        }
        return  blogVOS;
    }

    public static BlogVO markdownDTOConvertBlogVO(MarkDownBlogDTO markDownBlogDTO) {
        return MODEL_MAPPER.map(markDownBlogDTO, BlogVO.class);
    }


    /**
     * 将UeditorBlogDto List转换为BlogVO List
      * @param uEditorBlogDOS
     * @return
     */
    public static List<BlogVO> ueditorDoToDtoList(List<UeditorBlogDTO> uEditorBlogDOS) {
        List<BlogVO> blogVOS = new LinkedList<>();
        for (int i = 0; i <uEditorBlogDOS.size() ; i++) {
            blogVOS.add(new BlogVO(uEditorBlogDOS.get(i)));
        }
        return blogVOS;
    }


    public static BlogVO ueditorDTOConvertBlogVO(UeditorBlogDTO ueditorBlogDTO) {
        return MODEL_MAPPER.map(ueditorBlogDTO, BlogVO.class);
    }

    /**
     *将ueditor类型的博客转换为CommonBlogDTO
     * @param ueditorBlogDTOS
     * @return
     */
    public static List<CommonBlogDTO> ueditorConvertcommonBlogDTO(List<UeditorBlogDTO> ueditorBlogDTOS) {
        return MODEL_MAPPER.map(ueditorBlogDTOS, new TypeToken<List<CommonBlogDTO>>() {
        }.getType());
    }

    /**
     *将ueditor类型的博客转换为CommonBlogDTO
     * @param ueditorBlogDTO
     * @return
     */
    public static CommonBlogDTO ueditorConvertcommonBlogDTO(UeditorBlogDTO ueditorBlogDTO) {
        return MODEL_MAPPER.map(ueditorBlogDTO, CommonBlogDTO.class);
    }

    /**
     * 将markdown类型的博客转换为CommonBlogDTO
     * @param markDownBlogDTOS
     * @return
     */
    public static List<CommonBlogDTO> markdownConvertCommonBlogDTO(List<MarkDownBlogDTO> markDownBlogDTOS) {
        return MODEL_MAPPER.map(markDownBlogDTOS,new TypeToken<List<CommonBlogDTO>>(){}
        .getType());
    }

    /**
     *将ueditor类型的博客转换为CommonBlogDTO
     * @param markDownBlogDTO
     * @return
     */
    public static CommonBlogDTO markdownConvertCommonBlogDTO(MarkDownBlogDTO markDownBlogDTO) {
        return MODEL_MAPPER.map(markDownBlogDTO, CommonBlogDTO.class);
    }


}
