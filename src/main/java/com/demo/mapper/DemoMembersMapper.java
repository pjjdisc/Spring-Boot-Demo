package com.demo.mapper;

import com.demo.base.BaseMapper;
import com.demo.pojo.DemoMembersPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:48
 */
@Mapper
public interface DemoMembersMapper extends BaseMapper<DemoMembersPojo> {

    @Select("select * from `demo_members` where user_name = #{userName}")
    DemoMembersPojo getUserByUserName(@Param("userName") String userName);
}
