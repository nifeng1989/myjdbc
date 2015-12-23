package com.nifeng.example.dao;

import com.nifeng.example.model.User;
import com.nifeng.jdbc.ShardBaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/9/4.
 */
@Repository
public class UserDao extends ShardBaseDao<User> {
}
