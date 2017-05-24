package com.sanshan.service;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.entity.BaseDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T extends BaseDO>  {


      T queryById(Long id);

      T queryOne(T example);

      List<T> queryAll();

      List<T> queryListByWhere(T example);

      PageInfo<T> queryPageListByWhere(T example, Integer page, Integer rows);

      Integer save(T t);

      Integer update(T t);

      Integer updateSelective(T t);

      Integer deleteById(Long id);

      Integer deleteByIds(Class<T> clazz,String property,List<Object> list);

      Integer deleteByWhere(T example);

}


