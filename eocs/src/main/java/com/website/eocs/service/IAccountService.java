package com.website.eocs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.eocs.entity.Account;

@Service
public interface IAccountService {
	@Autowired
	public String newRandomPassword();
	public Account getDataAccountByEmail(String email);
	public void updateAccountPassword(int id, String password);
}
