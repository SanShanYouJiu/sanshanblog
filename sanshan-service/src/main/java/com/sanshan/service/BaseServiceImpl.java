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
    //就算父类不是被IOC容器管理，但是建立关系时添加了@Autowired注解，父类的关系会被继承下来
    @Autowired
    private Mapper<T> mapper;

    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }

    public T queryOne(T example){
        return this.mapper.selectOne(example);
    }


    public List<T> queryAll(){
        return this.mapper.select(null);
    }


    public List<T> queryListByWhere(T example){
        return this.mapper.select(example);
    }


    public PageInfo<T> queryPageListByWhere(T example, Integer page, Integer rows){

        //设置分页参数
        PageHelper.startPage(page,rows);
        //执行查询
        List<T> list = this.mapper.select(example);
        return new PageInfo<T>(list);
    }


    public Integer save(T t){
        Date date=new Date();
        t.setCreated(date);
        t.setUpdated(date);
        return this.mapper.insertSelective(t);
    }

    public Integer update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }

    public Integer updateSelective(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(t);
    }

    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }

    public Integer deleteByIds(Class<T> clazz,String property,List<Object> list){
        Example example=new Example(clazz);
        example.createCriteria().andIn(property,list);
        return this.mapper.deleteByExample(example);
    }

    public Integer deleteByWhere(T example){
        return this.mapper.delete(example);
    }



}
