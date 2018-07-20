package com.demo.service.impl;

import com.demo.mapper.DemoMembersMapper;
import com.demo.pojo.DemoMembersPojo;
import com.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:51
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private DemoMembersMapper userMapper;

    @Override
    public DemoMembersPojo getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public DemoMembersPojo getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}
