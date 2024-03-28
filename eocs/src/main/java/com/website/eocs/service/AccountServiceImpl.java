package com.website.eocs.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.eocs.dao.AccountDAO;
import com.website.eocs.entity.Account;

@Service
public class AccountServiceImpl implements IAccountService {
	@Autowired
	private AccountDAO accountDAO;

	
	public String newRandomPassword() {
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder newPassword = new StringBuilder();
		
		for(int i = 0; i < 12; i++) {
			int index = random.nextInt(allowedChars.length());
			newPassword.append(allowedChars.charAt(index));
		}
		
		return newPassword.toString();
	}
	
	@Override
	public Account getDataAccountByEmail(String email) {
		return accountDAO.getDataAccountByEmail(email);
	}

	@Override
	public void updateAccountPassword(int id, String password) {
		accountDAO.updateAccountPassword(id, password);
		
	}
}
