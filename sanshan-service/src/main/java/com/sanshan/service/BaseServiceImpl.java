package com.sanshan.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.entity.BaseDO;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 封装到子类进行供给实现
 * @param <T>
 */
public class BaseServiceImpl<T extends BaseDO> implements BaseService<T>{

    //这里利用了Spring4才支持的泛型注入
    @Autowired
    private Mapper<T> mapper;

    /**
     * 根据id查询
     */
    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据条件查询一条数据
     */
    public T queryOne(T example){
        return this.mapper.selectOne(example);
    }


    /**
     * 查询所有数据
     */
    public List<T> queryAll(){
        return this.mapper.select(null);
    }


    /**
     * 根据条件查询数据列表
     */
    public List<T> queryListByWhere(T example){
        return this.mapper.select(example);
    }


    /**
     * 分页查询数据列表
     * @param example 查询条件
     * @param page 页数
     * @param rows 页面大小
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T example, Integer page, Integer rows){

        //设置分页参数
        PageHelper.startPage(page,rows);
        //执行查询
        List<T> list = this.mapper.select(example);
        return new PageInfo<T>(list);
    }


    /**
     * 新增数据，注意设置数据的创建和更新时间
     * 返回成功的条数
     */
    public Integer save(T t){
        Date date=new Date();
        t.setCreated(date);
        t.setUpdated(date);
        return this.mapper.insertSelective(t);
    }

    /**
     * 更新数据，设置数据的更新时间
     * 返回成功的条数
     */
    public Integer update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }

    /**
     * 更新数据，设置数据的更新时间（更新部分数据）
     * 返回成功的条数
     */
    public Integer updateSelective(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * 根据id删除数据
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除数据
     * @param clazz
     * @param property
     * @param list
     * @return
     */
    public Integer deleteByIds(Class<T> clazz,String property,List<Object> list){
        Example example=new Example(clazz);
        example.createCriteria().andIn(property,list);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除数据
     */
    public Integer deleteByWhere(T example){
        return this.mapper.delete(example);
    }



}
