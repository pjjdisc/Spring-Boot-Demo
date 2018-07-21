package com.demo.base;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 21:05
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
