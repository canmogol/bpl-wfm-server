package com.bplsoft.wfm.model;

import com.bplsoft.common.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "HELLO_MODEL")
public class HelloModel extends BaseModel<Integer> {

    public HelloModel() {
    }

    public HelloModel(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "HM_SEQ")
    @SequenceGenerator(name = "HM_SEQ", sequenceName = "HM_SEQ", allocationSize = 1)
    @Column(name = "HM_ID", updatable = false, nullable = false)
    private Integer id;

    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HelloModel{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
