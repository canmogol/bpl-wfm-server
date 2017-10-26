package com.bplsoft.wfm.service;


import com.bplsoft.wfm.service.model.LoginResponseModel;
import com.bplsoft.wfm.model.UserModel;
import com.bplsoft.wfm.repository.UserQueryRepository;
import com.bplsoft.wfm.service.model.LoginRequestModel;

import javax.inject.Inject;
import java.util.Optional;

public class LoginService {

    @Inject
    UserQueryRepository queryRepository;


    public LoginResponseModel login(LoginRequestModel requestModel) {
        Optional<UserModel> userModelOptional = queryRepository.findByUsernameAndPassword(requestModel.getUsername(), requestModel.getPassword());
        if (userModelOptional.isPresent()) {
            return new LoginResponseModel("success", userModelOptional.get().getName());
        } else {
            return new LoginResponseModel("fail", "");
        }
    }

}
