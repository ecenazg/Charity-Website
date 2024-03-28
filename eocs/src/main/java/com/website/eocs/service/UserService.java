package com.website.eocs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.eocs.dao.AccountDAO;
import com.website.eocs.dao.CategoriesDAO;
import com.website.eocs.dao.DonationDAO;
import com.website.eocs.dao.FoundationDAO;
import com.website.eocs.dao.FundDao;
import com.website.eocs.entity.Account;
import com.website.eocs.entity.Categories;
import com.website.eocs.entity.Donation;
import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.Fund;

@Service
public abstract class UserService implements IUserService {
	@Autowired
	FoundationDAO foundationDAO;
	@Autowired
	CategoriesDAO categoriesDAO;
	@Autowired
	FundDao fundDao;
	@Autowired
	DonationDAO donationDAO;
	@Autowired
	AccountDAO accountDAO;

	@Override
	public List<Foundation> getDataFoundationActive() {
		return foundationDAO.getDataFoundationActive();
	}

	@Override
	public List<Foundation> getDataFoundationPaginationActive(int start, int end) {
		return foundationDAO.getDataFoundationPaginationActive(start, end);
	}

	@Override
	public List<Fund> getDataFundByFoundationActive(int foundationId) {
		return fundDao.getDataFundByFoundationActive(foundationId);
	}

	@Override
	public List<Categories> getDataCategoriesActive() {
		return categoriesDAO.getDataCategoriesActive();
	}

	@Override
	public List<Categories> getDataCategoriesPaginationActive(int start, int end) {
		return categoriesDAO.getDataCategoriesPaginationActive(start, end);
	}

	@Override
	public List<Fund> getDataFundActive() {
		return fundDao.getDataFundActive();
	}

	@Override
	public List<Fund> getDataFundsPaginationActive(int start, int end) {
		return fundDao.getDataFundsPaginationActive(start, end);
	}

	@Override
	public List<Fund> getDataFundByCategoriesActive(int categoryId) {
		return fundDao.getDataFundByCategoriesActive(categoryId);
	}

	@Override
	public Integer getCurrentMoneyByFund(int fundId) {
		return donationDAO.getCurrentMoneyByFund(fundId);
	}

	@Override
	public void createDonation(Donation donation, int accountId, int fundId) {
		donationDAO.createDonation(donation, accountId, fundId);
	}

	@Override
	public List<Donation> searchDonationByAccount(String accountName) {
		return donationDAO.searchDonationByAccount(accountName);
	}

	@Override
	public List<Donation> searchDonationByAccountPagination(String accountName, int start, int end) {
		return donationDAO.searchDonationByAccountPagination(accountName, start, end);
	}

	@Override
	public void updateUser(Account user) {
		accountDAO.updateUser(user);
	}

	@Override
	public List<Fund> userSearchFunds(String fundName, int categoryId, int foundationId) {
		return fundDao.userSearchFunds(fundName, categoryId, foundationId);
	}

	@Override
	public List<Fund> userSearchFundsPagination(String fundName, int categoryId, int foundationId, int start, int end) {
		return fundDao.userSearchFundsPagination(fundName, categoryId, foundationId, start, end);
	}

}
