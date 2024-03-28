package com.website.eocs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.website.eocs.entity.Account;
import com.website.eocs.entity.Categories;
import com.website.eocs.entity.Donation;
import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.Fund;

@Service
public interface IUserService {
	public List<Foundation> getDataFoundationActive();

	public List<Foundation> getDataFoundationPaginationActive(int start, int end);
	
	public List<Fund> getDataFundByFoundationActive(int foundationId) ;
	
	public List<Categories> getDataCategoriesActive();
	
	public List<Categories> getDataCategoriesPaginationActive(int start, int end);
	
	public List<Fund> getDataFundActive();
	
	public List<Fund> getDataFundsPaginationActive(int start, int end);
	
	public List<Fund> getDataFundByCategoriesActive(int categoryId) ;
	
	public Integer getCurrentMoneyByFund(int fundId) ;
	
	public void createDonation(Donation donation, int accountId, int fundId);
	
	public List<Donation> searchDonationByAccount(String accountName);
	
	public List<Donation> searchDonationByAccountPagination(String accountName, int start, int end);
	
	public void updateUser(Account user);
	
	public List<Fund> userSearchFunds(String fundName, int categoryId, int foundationId);
	
	public List<Fund> userSearchFundsPagination(String fundName, int categoryId, int foundationId, int start, int end);
}
