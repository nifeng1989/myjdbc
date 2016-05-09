package net.fengni.example.model;

import net.fengni.jdbc.annotation.*;
import net.fengni.jdbc.shard.DefaultShardRule;

/**
 * Created by Administrator on 2015/9/3.
 */
@Entity
@Table("p_user")
@Shard(shardRule = DefaultShardRule.class,size = 128)//默认按照主键分表
@DataSource(dataSourceRW = "dataSourceRW", dataSourceRO = "dataSourceRO", useDataSourceRO = true)

public class User {
    @Id
    @Column
    private int id;
    @Column ("userName")
    private String username;
    @Column
    private String password;
    @Column
    private int grade;//用户级别

    private String phoneNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
