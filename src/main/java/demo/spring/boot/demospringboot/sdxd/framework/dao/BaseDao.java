package demo.spring.boot.demospringboot.sdxd.framework.dao;


import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

import demo.spring.boot.demospringboot.sdxd.framework.entity.BaseEntity;
import demo.spring.boot.demospringboot.sdxd.framework.mybatis.BaseProvider;
import demo.spring.boot.demospringboot.sdxd.framework.mybatis.Sort;
import demo.spring.boot.demospringboot.sdxd.framework.mybatis.complexQuery.CustomQueryParam;


public interface BaseDao<T extends BaseEntity> {

    @SelectProvider(type = BaseProvider.class, method = "getAll")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> getAll();

    @SelectProvider(type = BaseProvider.class, method = "getById")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public T getById(String id);

    @SelectProvider(type = BaseProvider.class, method = "count")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public int count(T params);

    @SelectProvider(type = BaseProvider.class, method = "countLike")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public int countLike(T findParams);

    @SelectProvider(type = BaseProvider.class, method = "countQuery")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public int countQuery(@Param("queryParams") List<CustomQueryParam> customQueryParams);

    @SelectProvider(type = BaseProvider.class, method = "get")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public T getOne(T findParams);

    @SelectProvider(type = BaseProvider.class, method = "query")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> query(@Param("queryParams") List<CustomQueryParam> customQueryParams, @Param("sortList") List<Sort> sortList);

    @SelectProvider(type = BaseProvider.class, method = "get")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> get(T findParams);

    @SelectProvider(type = BaseProvider.class, method = "find")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> find(T findParams);

    @InsertProvider(type = BaseProvider.class, method = "insert")
    @Options(keyProperty = "id", flushCache = Options.FlushCachePolicy.TRUE)
    public int insert(T t);

    @InsertProvider(type = BaseProvider.class, method = "insertBatch")
    @Options(keyProperty = "id", flushCache = Options.FlushCachePolicy.TRUE)
    public int insertBatch(@Param("list") List<T> list);

    @DeleteProvider(type = BaseProvider.class, method = "delete")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int delete(String id);

    @DeleteProvider(type = BaseProvider.class, method = "deleteByPrimaryKey")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int deleteByPrimaryKey(T t);

    @UpdateProvider(type = BaseProvider.class, method = "update")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int update(T t);

    @DeleteProvider(type = BaseProvider.class, method = "deleteAll")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int deleteAll();

}