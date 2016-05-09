package com.nifeng.example.model;

import net.fengni.jdbc.annotation.*;

/**
 * Created by Administrator on 2015/12/23.
 */
@Entity
@Table("p_product")
@DataSource(dataSourceRW = "dataSourceRW", dataSourceRO = "dataSourceRO", useDataSourceRO = true)
public class Product {
    @Id
    @AutoIncrement
    @Column
    private int id;

    @Column
    private String name;
    @Column("description")
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
