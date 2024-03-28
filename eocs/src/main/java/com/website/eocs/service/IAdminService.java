package com.website.eocs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.website.eocs.dto.StatusDto;
import com.website.eocs.entity.Account;
import com.website.eocs.entity.Categories;
import com.website.eocs.entity.Donation;
import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.Fund;

@Service
public interface IAdminService {
	@Autowired
	public List<StatusDto> statusList();
	
	public List<StatusDto> statusListForFunds();

	public List<Donation> getDataDonations();

	public List<Donation> getDataDonationsPagination(int start, int end);

	public List<Donation> searchDonations(int id, String accountName, String fundName);

	public List<Donation> searchDonationsPagination(int id, String accountName, String fundName, int start, int end);

	public List<Fund> getDataFunds();

	public List<Fund> getDataFundsPagination(int start, int end);

	public List<Fund> searchFunds(int id, String fundName, String categoryName);

	public List<Fund> searchFundsPagination(int id, String fundName, String categoryName, int start, int end);

	public boolean isUniqueFund(String name, int id);

	public void deleteFund(int id);

	public void unDeleteFund(int id);

	public void deleteMultipleFunds(List<Integer> ids);

	public Fund getDataFundById(int id);

	public void updateFund(Fund fund, int categoryId, int foundationId);

	public void insertFund(Fund fund, int categoryId, int foundationId);
	
	public void finishFund(int id);

	public List<Categories> getDataCategories();

	public List<Categories> getDataCategoriesPagination(int start, int end);

	public List<Categories> searchCategories(int id, String name);

	public List<Categories> searchCategoriesPagination(int id, String name, int start, int end);

	public void deleteCategories(int id);

	public void unDeleteCategories(int id);

	public void deleteMultipleCategories(List<Integer> ids);

	public Categories getDataCategoriesById(int id);

	public int getCategoryIdByName(String name);

	public void updateCategories(Categories categories);

	public void insertCategories(Categories categories);

	public boolean isUniqueCategories(String name, int id);

	public List<Foundation> getDataFoundation();

	public List<Foundation> getDataFoundationPagination(int start, int end);

	public List<Foundation> searchFoundation(int id, String name);

	public List<Foundation> searchFoundationPagination(int id, String name, int start, int end);

	public void deleteFoundation(int id);

	public void unDeleteFoundation(int id);

	public void deleteMultipleFoundation(List<Integer> ids);

	public Foundation getDataFoundationById(int id);

	public int getFoundationIdByName(String name);

	public void updateFoundation(Foundation foundation);

	public void insertFoundation(Foundation foundation);

	public boolean isUniqueFoundation(String name, int id);

	public List<Account> getDataAccount();

	public List<Account> getDataAccountPagination(int start, int end);

	public List<Account> searchAccount(String username, String phone);

	public List<Account> searchAccountPagination(String username, String phone, int start, int end);

	public void deleteAccount(int id);

	public void unDeleteAccount(int id);

	public void deleteMultipleAccount(List<Integer> ids);

	public Account getDataAccountById(int id);

	public void updateAccount(Account account);

	public void insertAccount(Account account);

	public boolean isUniqueAccount(String email, int id);
	
	public boolean isUniqueAccount(String email);
	
	

}
