package com.upgrad.musichoster.service.business;

import com.upgrad.musichoster.service.dao.UserDao;
import com.upgrad.musichoster.service.entity.UserAuthTokenEntity;
import com.upgrad.musichoster.service.entity.UserEntity;
import com.upgrad.musichoster.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordCryptographyProvider CryptographyProvider;


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UserAuthTokenEntity authenticate(String username, String password)
			throws AuthenticationFailedException {
		UserEntity userEntity = userDao.getUserByEmail(username);
		if(userEntity == null)
		{
			throw new AuthenticationFailedException("ATH-001","User with wrong email");
		}

		final String encryptedPassword = CryptographyProvider.encrypt(password, userEntity.getSalt());
		if(encryptedPassword.equals(userEntity.getPassword()))
		{
			JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
			UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
			userAuthToken.setUser(userEntity);
			final ZonedDateTime now =ZonedDateTime.now();
			final ZonedDateTime expiresAt = now.plusHours(8);
			userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
			userAuthToken.setLoginAt(now);
			userAuthToken.setExpiresAt(expiresAt);

			userDao.createAuthToken(userAuthToken);
			userDao.updateUser(userEntity);
			userEntity.setLastLoginAt(now);
			return userAuthToken;
		}
		else
		{
			throw new AuthenticationFailedException("ATH-002","Password Incorrect");
		}

	}
}
