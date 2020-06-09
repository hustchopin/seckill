//package com.zyw.shopping.base.service;
//
//import org.apache.ibatis.annotations.Param;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public interface IService<T> {
//
//    Long getSequence(@Param("seqName") String seqName);
//
//    List<T> selectAll();
//
//    T selectByKey(Object key);
//
//    int save(T entity);
//
//    int delete(Object key);
//
//    int batchDelete(List<String> list, String property, Class<T> clazz);
//
//    int updateAll(T entity);
//
//    int updateNotNull(T entity);
//
//    List<T> selectByExample(Object example);
//}
