package xyz.sanshan.main.service.convent;

import xyz.sanshan.main.pojo.dto.MarkDownBlogDTO;
import xyz.sanshan.main.pojo.dto.UeditorBlogDTO;
import xyz.sanshan.main.service.vo.BlogVO;

import java.util.LinkedList;
import java.util.List;

public class BlogConvert {


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


}
