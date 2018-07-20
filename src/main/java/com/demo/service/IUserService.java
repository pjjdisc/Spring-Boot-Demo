package com.demo.service;

import com.demo.pojo.DemoMembersPojo;

/**
 * @author Eddy
 * @version V1.0
 * @Date 2018-07-20 14:51
 */
public interface IUserService {
    DemoMembersPojo getUserByUserName(String userName);

    DemoMembersPojo getUserById(Long id);
}
