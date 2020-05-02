package com.upgrad.musichoster.api.controller;


import com.upgrad.musichoster.api.model.SignupUserRequest;
import com.upgrad.musichoster.api.model.SignupUserResponse;
import com.upgrad.musichoster.service.business.AuthenticationService;
import com.upgrad.musichoster.service.business.SignupBusinessService;
import com.upgrad.musichoster.service.business.SignupBusinessServiceImpl;
import com.upgrad.musichoster.service.entity.UserEntity;
import com.upgrad.musichoster.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController

@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private SignupBusinessService signupBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/usersignup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest)
            throws SignUpRestrictedException {
        UserEntity entity = null;
      //if(signupBusinessService.equals(signupUserRequest.getEmailAddress())){
            final UserEntity userEntity = new UserEntity();
            userEntity.setFirstName(signupUserRequest.getFirstName());
            userEntity.setLastName(signupUserRequest.getLastName());
            userEntity.setPassword(signupUserRequest.getPassword());
            userEntity.setEmail(signupUserRequest.getEmailAddress());
            userEntity.setMobilePhone(signupUserRequest.getMobileNumber());
            userEntity.setUuid(UUID.randomUUID().toString());

            userEntity.setRole("nonadmin");

            entity = signupBusinessService.signup(userEntity);
     //  }
        SignupUserResponse userResponse = new SignupUserResponse().id(entity.getUuid()).status("REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }
}
