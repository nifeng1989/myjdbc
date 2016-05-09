package net.fengni.example.dao;

import net.fengni.example.model.User;
import net.fengni.jdbc.ShardBaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/9/4.
 */
@Repository
public class UserDao extends ShardBaseDao<User> {
}
