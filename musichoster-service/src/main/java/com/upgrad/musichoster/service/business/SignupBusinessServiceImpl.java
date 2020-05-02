package com.upgrad.musichoster.service.business;

import com.upgrad.musichoster.service.dao.UserDao;
import com.upgrad.musichoster.service.entity.UserEntity;
import com.upgrad.musichoster.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SignupBusinessServiceImpl implements SignupBusinessService {

	@Autowired private UserDao userDao;

	@Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

//	public boolean createUser(UserEntity userEntity) throws SignUpRestrictedException{
//		if( (userDao.createUser(userEntity) != null)){
//			throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
//		}
//		else {
//			return true;
//		}
//	}

	public boolean getUserByEmail(String email) throws SignUpRestrictedException{
		if((userDao.getUserByEmail(email) != null)){
			throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
		}
		else {
			return true;
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException{
//		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(userEntity.getEmail());
//		userEntity.setSalt(regex);
//		userEntity.setPassword(regex);
		String[] encrypt = passwordCryptographyProvider.encrypt(userEntity.getPassword());
		userEntity.setSalt(encrypt[0]);
		userEntity.setPassword(encrypt[1]);
		return userDao.signup(userEntity);

	}
}
