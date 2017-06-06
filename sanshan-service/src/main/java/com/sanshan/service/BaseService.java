package com.sanshan.service;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.entity.BaseDO;

import java.util.List;

public interface BaseService<T extends BaseDO>  {

      /**
       * 根据ID查询
       * @param id ID
       * @return
       */
      T queryById(Long id);

      /**
       * 根据条件查询一条数据
       * @param example 条件
       * @return
       */
      T queryOne(T example);

      /**
       *  查询所有数据
       * @return 返回所有数据
       */
      List<T> queryAll();

      /**
       * 根据条件查询数据列表
       * @param example 条件
       * @return
       */
      List<T> queryListByWhere(T example);

      /**
       * 分页查询数据列表
       * @param example 查询条件
       * @param page 页数
       * @param rows 页面大小
       * @return
       */
      PageInfo<T> queryPageListByWhere(T example, Integer page, Integer rows);

      /**
       * 新增数据，注意设置数据的创建和更新时间
       * @param t
       * @return 返回成功的条数
       */
      Integer save(T t);

      /**
       * 更新数据，设置数据的更新时间
       * @param t
       * @return 返回成功的条数
       */
      Integer update(T t);

      /**
       * 更新数据，设置数据的更新时间（更新部分数据）
       * @param t
       * @return 返回成功的条数
       */
      Integer updateSelective(T t);

      /**
       * 根据id删除数据
       * @param id
       * @return
       */
      Integer deleteById(Long id);

      /**
       * 批量删除数据
       * @param clazz
       * @param property
       * @param list
       * @return
       */
      Integer deleteByIds(Class<T> clazz,String property,List<Object> list);

      /**
       * 根据条件删除数据
       * @param example
       * @return
       */
      Integer deleteByWhere(T example);

}


