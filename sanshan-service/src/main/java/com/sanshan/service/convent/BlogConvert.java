package com.sanshan.service.convent;

import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.dto.UEditorBlogDTO;
import com.sanshan.service.vo.BlogVO;

import java.util.LinkedList;
import java.util.List;

public class BlogConvert {


    /**
     * 将markDownBlogDO List转换为BlogVO List
     * @param markDownBlogDOS
     * @return
     */
    public static List<BlogVO> MarkdownDoToDtoList(List<MarkDownBlogDTO> markDownBlogDOS) {
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
    public static List<BlogVO> UeditorDoToDtoList(List<UEditorBlogDTO> uEditorBlogDOS) {
        List<BlogVO> blogVOS = new LinkedList<>();
        for (int i = 0; i <uEditorBlogDOS.size() ; i++) {
            blogVOS.add(new BlogVO(uEditorBlogDOS.get(i)));
        }
        return blogVOS;
    }


}
