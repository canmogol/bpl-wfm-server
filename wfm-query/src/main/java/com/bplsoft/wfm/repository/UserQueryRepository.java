package com.bplsoft.wfm.repository;

import com.bplsoft.common.repository.builder.ParamRelation;
import com.bplsoft.common.repository.query.QueryRepository;
import com.bplsoft.wfm.model.UserModel;

import java.util.List;
import java.util.Optional;

public class UserQueryRepository extends QueryRepository<UserModel, Integer> {

    public Optional<UserModel> findByUsernameAndPassword(String username, String password) {
        return query()
            .where("username", username)
            .and("password", password)
            .findOne();
    }

    public List<UserModel> findByName(String name) {
        return query()
            .where("name", name)
            .and("age", ParamRelation.GE, 15)
            .find();
    }

}
