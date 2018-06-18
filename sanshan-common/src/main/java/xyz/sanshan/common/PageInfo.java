package xyz.sanshan.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sanshan
 * www.85432173@qq.com
 * 分页相关信息
 */
@Data
@ToString
public class PageInfo<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    //结果集
    private T completeData;

    //从BlogIdGenerate中取出的映射 会在返回Json时设置为null 在Json中看不到此项
    private Map currentMapData;

    //当前页
    private long pageNum;

    //每页的数量
    private long pageRows;

    //当前页的数量
    private long size;

    //总记录数
    private long total;

    //总页数
    private long pages;

    //前一页
    private long prePage;
    //下一页
    private long nextPage;

    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;

    /**
     * 组装PageInfo
     * @param currentMapData 从BlogIdGenerate中获得的数据
     * @param pageRows  一页的行数
     * @param pageNum   当前页
     * @param total     数据总数
     */
    public PageInfo(Map currentMapData, long pageRows, long pageNum, long total) {
        long preRows = pageRows * (pageNum - 1);
        this.currentMapData = currentMapData;
        this.pageRows = pageRows;
        this.pageNum = pageNum;
        this.total = total;

        computeFiled(pageRows, pageNum, total, preRows);

    }

    /**
     * 组装完成的数据
     * @param completeData
     * @param pageRows
     * @param pageNum
     * @param total
     */
    public PageInfo(T completeData, long pageRows, long pageNum, long total) {
        long preRows = pageRows * (pageNum - 1);
        this.completeData = completeData;
        this.pageRows = pageRows;
        this.pageNum = pageNum;
        this.total = total;

        computeFiled(pageRows, pageNum, total, preRows);
    }

    /**
     * 空
     */
    public PageInfo(){

    }


    private void computeFiled(long pageRows, long pageNum, long total, long preRows) {
        if ((total % pageRows) != 0) {
            //如果余数不为0 代表除不尽 pages+1
            long pages = total / pageRows;
            this.pages = pages + 1;
        } else {
            this.pages = total / pageRows;
        }

        if (total / pageRows < 1) {
            //数据太少 不满足一页的情况
            this.isFirstPage = true;
            this.isLastPage = true;
        } else {
            if (pageNum == 1) {
                this.isFirstPage = true;
            } else if ((total - preRows) > pageRows) {
                //减掉前面所有的数量 还有比当前页面还多的数据 就不是最后一行 反之就是
            } else {
                isLastPage = true;
            }
        }

        if (isFirstPage){
            if (isLastPage){
            //即是第一个也是最后一个 代表就这一个页面
            }else {
                this.hasNextPage=true;
            }
        }else {
            if (isLastPage){
                //不是第一个但是是最后一个 会有前一页
               this.hasPreviousPage=true;
            }else {
                //既不是第一个 也不是最后一个 都有
                this.hasPreviousPage=true;
                this.hasNextPage=true;
            }
        }

        if (isLastPage) {
            this.size = total - preRows;
        } else {
            this.size = pageRows;
        }


        if (isFirstPage){
            if (isLastPage) {
                //同上 没有上一页与下一页
            }else {
                this.nextPage = pageNum + 1;
            }
         }else {
             if (isLastPage){
                 //是最后一页 没有下一页
                 this.prePage=pageNum-1;
             }else {
                 //都有
                 this.prePage=pageNum-1;
                 this.nextPage=pageNum+1;
             }
         }
    }

}
