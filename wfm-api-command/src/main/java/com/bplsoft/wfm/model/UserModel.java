package com.bplsoft.wfm.model;

import com.bplsoft.common.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "USER_MODEL")
public class UserModel extends BaseModel<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UM_SEQ")
    @SequenceGenerator(name = "UM_SEQ", sequenceName = "UM_SEQ", allocationSize = 1)
    @Column(name = "UM_ID", updatable = false, nullable = false)
    private Integer id;

    private String username;
    private String password;
    private String site;
    private String name;
    private Integer age;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
