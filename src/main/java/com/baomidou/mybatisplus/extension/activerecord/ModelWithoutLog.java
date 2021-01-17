package com.baomidou.mybatisplus.extension.activerecord;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;

/**
 * 从 MyBatis Plus 的 com.baomidou.mybatisplus.extension.activerecord.Model 复制过来，删掉了log对象
 */
public abstract class ModelWithoutLog<T extends ModelWithoutLog<?>> implements Serializable {

  private static final long serialVersionUID = 1L;

  // 去掉Model类中自带的log对象，下面用到log的地方改为实时创建log
  // private final transient Log log = LogFactory.getLog(getClass());

  /**
   * 插入（字段选择插入）
   */
  public boolean insert() {
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retBool(sqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), this));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 插入 OR 更新
   */
  public boolean insertOrUpdate() {
    return StringUtils.checkValNull(pkVal()) || Objects.isNull(selectById(pkVal())) ? insert() : updateById();
  }

  /**
   * 根据 ID 删除
   *
   * @param id 主键ID
   */
  public boolean deleteById(Serializable id) {
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retBool(sqlSession.delete(sqlStatement(SqlMethod.DELETE_BY_ID), id));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 根据主键删除
   */
  public boolean deleteById() {
    Assert.isFalse(StringUtils.checkValNull(pkVal()), "deleteById primaryKey is null.");
    return deleteById(pkVal());
  }

  /**
   * 删除记录
   *
   * @param queryWrapper 实体对象封装操作类（可以为 null）
   */
  public boolean delete(Wrapper<T> queryWrapper) {
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(1);
    map.put(Constants.WRAPPER, queryWrapper);
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retBool(sqlSession.delete(sqlStatement(SqlMethod.DELETE), map));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 更新（字段选择更新）
   */
  public boolean updateById() {
    Assert.isFalse(StringUtils.checkValNull(pkVal()), "updateById primaryKey is null.");
    // updateById
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(1);
    map.put(Constants.ENTITY, this);
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retBool(sqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), map));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 执行 SQL 更新
   *
   * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
   */
  public boolean update(Wrapper<T> updateWrapper) {
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(2);
    map.put(Constants.ENTITY, this);
    map.put(Constants.WRAPPER, updateWrapper);
    // update
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retBool(sqlSession.update(sqlStatement(SqlMethod.UPDATE), map));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 查询所有
   */
  public List<T> selectAll() {
    SqlSession sqlSession = sqlSession();
    try {
      return sqlSession.selectList(sqlStatement(SqlMethod.SELECT_LIST));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 根据 ID 查询
   *
   * @param id 主键ID
   */
  public T selectById(Serializable id) {
    SqlSession sqlSession = sqlSession();
    try {
      return sqlSession.selectOne(sqlStatement(SqlMethod.SELECT_BY_ID), id);
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 根据主键查询
   */
  public T selectById() {
    Assert.isFalse(StringUtils.checkValNull(pkVal()), "selectById primaryKey is null.");
    return selectById(pkVal());
  }

  /**
   * 查询总记录数
   *
   * @param queryWrapper 实体对象封装操作类（可以为 null）
   */
  public List<T> selectList(Wrapper<T> queryWrapper) {
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(1);
    map.put(Constants.WRAPPER, queryWrapper);
    SqlSession sqlSession = sqlSession();
    try {
      return sqlSession.selectList(sqlStatement(SqlMethod.SELECT_LIST), map);
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 查询一条记录
   *
   * @param queryWrapper 实体对象封装操作类（可以为 null）
   */
  public T selectOne(Wrapper<T> queryWrapper) {
    return SqlHelper.getObject(LogFactory.getLog(getClass()), selectList(queryWrapper));
  }

  /**
   * 翻页查询
   *
   * @param page         翻页查询条件
   * @param queryWrapper 实体对象封装操作类（可以为 null）
   */
  public <E extends IPage<T>> E selectPage(E page, Wrapper<T> queryWrapper) {
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(2);
    map.put(Constants.WRAPPER, queryWrapper);
    map.put("page", page);
    SqlSession sqlSession = sqlSession();
    try {
      page.setRecords(sqlSession.selectList(sqlStatement(SqlMethod.SELECT_PAGE), map));
    } finally {
      closeSqlSession(sqlSession);
    }
    return page;
  }

  /**
   * 查询总数
   *
   * @param queryWrapper 实体对象封装操作类（可以为 null）
   */
  public Integer selectCount(Wrapper<T> queryWrapper) {
    Map<String, Object> map = CollectionUtils.newHashMapWithExpectedSize(1);
    map.put(Constants.WRAPPER, queryWrapper);
    SqlSession sqlSession = sqlSession();
    try {
      return SqlHelper.retCount(sqlSession.<Integer>selectOne(sqlStatement(SqlMethod.SELECT_COUNT), map));
    } finally {
      closeSqlSession(sqlSession);
    }
  }

  /**
   * 执行 SQL
   */
  public SqlRunner sql() {
    return new SqlRunner(getClass());
  }

  /**
   * 获取Session 默认自动提交
   */
  protected SqlSession sqlSession() {
    return SqlHelper.sqlSession(getClass());
  }

  /**
   * 获取SqlStatement
   *
   * @param sqlMethod sqlMethod
   */
  protected String sqlStatement(SqlMethod sqlMethod) {
    return sqlStatement(sqlMethod.getMethod());
  }

  /**
   * 获取SqlStatement
   *
   * @param sqlMethod sqlMethod
   */
  protected String sqlStatement(String sqlMethod) {
    //无法确定对应的mapper，只能用注入时候绑定的了。
    return SqlHelper.table(getClass()).getSqlStatement(sqlMethod);
  }

  /**
   * 主键值
   */
  protected Serializable pkVal() {
    return (Serializable) ReflectionKit.getFieldValue(this, TableInfoHelper.getTableInfo(getClass()).getKeyProperty());
  }

  /**
   * 释放sqlSession
   *
   * @param sqlSession session
   */
  protected void closeSqlSession(SqlSession sqlSession) {
    SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(getClass()));
  }

}
