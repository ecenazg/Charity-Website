package com.website.eocs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.eocs.dao.AccountDAO;
import com.website.eocs.dao.CategoriesDAO;
import com.website.eocs.dao.DonationDAO;
import com.website.eocs.dao.FoundationDAO;
import com.website.eocs.dao.FundDao;
import com.website.eocs.dto.StatusDto;
import com.website.eocs.entity.Account;
import com.website.eocs.entity.Categories;
import com.website.eocs.entity.Donation;
import com.website.eocs.entity.Foundation;
import com.website.eocs.entity.Fund;

@Service
public class AdminServiceImpl implements IAdminService {
	@Autowired
	private DonationDAO donationDAO;
	@Autowired
	private FundDao fundDao;
	@Autowired
	private CategoriesDAO categoriesDAO;
	@Autowired
	private FoundationDAO foundationDAO;
	@Autowired
	private AccountDAO accountDAO;
	
	@Override
	public List<StatusDto> statusList() {
		List<StatusDto> statusList = new ArrayList<StatusDto>();
		statusList.add(new StatusDto("Active"));
		statusList.add(new StatusDto("Inactive"));
		return statusList;
	}
	
	public List<StatusDto> statusListForFunds() {
		List<StatusDto> statusList = new ArrayList<StatusDto>();
		statusList.add(new StatusDto("Active"));
		statusList.add(new StatusDto("Inactive"));
		statusList.add(new StatusDto("Finish"));
		return statusList;
	}
	
	public List<Donation> getDataDonations() {
		return donationDAO.getDataDonations();
	}

	public List<Donation> getDataDonationsPagination(int start, int end) {
		return donationDAO.getDataDonationsPagination(start, end);
	}

	public List<Donation> searchDonations(int id, String accountName, String fundName) {
		return donationDAO.searchDonations(id, accountName, fundName);
	}

	@Override
	public List<Donation> searchDonationsPagination(int id, String accountName, String fundName, int start, int end) {
		return donationDAO.searchDonationsPagination(id, accountName, fundName, start, end);
	}

	@Override
	public List<Fund> getDataFunds() {
		return fundDao.getDataFund();
	}

	@Override
	public List<Fund> getDataFundsPagination(int start, int end) {
		return fundDao.getDataFundsPagination(start, end);
	}

	@Override
	public List<Fund> searchFunds(int id, String fundName, String categoryName) {
		return fundDao.searchFunds(id, fundName, categoryName);
	}

	@Override
	public List<Fund> searchFundsPagination(int id, String fundName, String categoryName, int start, int end) {
		return fundDao.searchFundsPagination(id, fundName, categoryName, start, end);
	}

	@Override
	public void deleteFund(int id) {
		fundDao.deleteFund(id);

	}

	@Override
	public void unDeleteFund(int id) {
		fundDao.unDeleteFund(id);

	}

	@Override
	public void deleteMultipleFunds(List<Integer> ids) {
		fundDao.deleteMultipleFunds(ids);
	}

	@Override
	public Fund getDataFundById(int id) {
		return fundDao.getDataFundById(id);
	}
	
	@Override
	public void updateFund(Fund fund, int categoryId, int foundationId) {
		fundDao.updateFund(fund, categoryId, foundationId);		
	}
	
	@Override
	public void insertFund(Fund fund, int categoryId, int foundationId) {
		fundDao.insertFund(fund, categoryId, foundationId);
		
	}
	
	@Override
	public boolean isUniqueFund(String name, int id) {
		return fundDao.isUniqueFund(name, id);
	}
	
	@Override
	public void finishFund(int id) {
		fundDao.finishFund(id);	
	}
	
	@Override
	public List<Categories> getDataCategories() {
		return categoriesDAO.getDataCategories();
	}

	@Override
	public List<Categories> getDataCategoriesPagination(int start, int end) {
		return categoriesDAO.getDataCategoriesPagination(start, end);
	}

	@Override
	public List<Categories> searchCategories(int id, String name) {
		return categoriesDAO.searchCategories(id, name);
	}

	@Override
	public List<Categories> searchCategoriesPagination(int id, String name, int start, int end) {
		return categoriesDAO.searchCategoriesPagination(id, name, start, end);
	}

	@Override
	public List<Foundation> getDataFoundation() {
		return foundationDAO.getDataFoundation();
	}

	@Override
	public List<Foundation> getDataFoundationPagination(int start, int end) {
		return foundationDAO.getDataFoundationPagination(start, end);
	}

	@Override
	public List<Foundation> searchFoundation(int id, String name) {
		return foundationDAO.searchFoundation(id, name);
	}

	@Override
	public List<Foundation> searchFoundationPagination(int id, String name, int start, int end) {
		return foundationDAO.searchFoundationPagination(id, name, start, end);
	}
	
	@Override
	public void deleteFoundation(int id) {
		foundationDAO.deleteFoundation(id);
	}

	@Override
	public void unDeleteFoundation(int id) {
		foundationDAO.unDeleteFoundation(id);
	}

	@Override
	public void deleteMultipleFoundation(List<Integer> ids) {
		foundationDAO.deleteMultipleFoundation(ids);
	}

	@Override
	public Foundation getDataFoundationById(int id) {
		return foundationDAO.getDataFoundationById(id);
	}
	
	@Override
	public int getFoundationIdByName(String name) {
		return foundationDAO.getFoundationIdByName(name);
	}
	@Override
	public void updateFoundation(Foundation foundation) {
		foundationDAO.updateFoundation(foundation);	
	}

	@Override
	public void insertFoundation(Foundation foundation) {
		foundationDAO.insertFoundation(foundation);
	}

	@Override
	public boolean isUniqueFoundation(String name, int id) {
		return foundationDAO.isUniqueFoundation(name, id);
	}

	@Override
	public List<Account> getDataAccount() {
		return accountDAO.getDataAccount();
	}

	@Override
	public List<Account> getDataAccountPagination(int start, int end) {
		return accountDAO.getDataAccountPagination(start, end);
	}

	@Override
	public List<Account> searchAccount(String username, String phone) {
		return accountDAO.searchAccount(username, phone);
	}

	@Override
	public List<Account> searchAccountPagination(String username, String phone, int start, int end) {
		return accountDAO.searchAccountPagination(username, phone, start, end);
	}

	@Override
	public void deleteCategories(int id) {
		categoriesDAO.deleteCategories(id);
	}

	@Override
	public void unDeleteCategories(int id) {
		categoriesDAO.unDeleteCategories(id);
	}

	@Override
	public void deleteMultipleCategories(List<Integer> ids) {
		categoriesDAO.deleteMultipleCategories(ids);		
	}

	@Override
	public Categories getDataCategoriesById(int id) {
		return categoriesDAO.getDataCategoriesById(id);
	}
	
	@Override
	public int getCategoryIdByName(String name) {
		return categoriesDAO.getCategoryIdByName(name);
	}

	@Override
	public void updateCategories(Categories categories) {
		categoriesDAO.updateCategories(categories);
	}

	@Override
	public void insertCategories(Categories categories) {
		categoriesDAO.insertCategories(categories);
	}

	@Override
	public boolean isUniqueCategories(String name, int id) {
		return categoriesDAO.isUniqueCategories(name, id);
	}

	@Override
	public void deleteAccount(int id) {
		accountDAO.deleteAccount(id);
	}

	@Override
	public void unDeleteAccount(int id) {
		accountDAO.unDeleteAccount(id);
	}

	@Override
	public void deleteMultipleAccount(List<Integer> ids) {
		accountDAO.deleteMultipleAccount(ids);
	}

	@Override
	public Account getDataAccountById(int id) {
		return accountDAO.getDataAccountById(id);
	}

	@Override
	public void updateAccount(Account account) {
		accountDAO.updateAccount(account);
		
	}

	@Override
	public void insertAccount(Account account) {
		accountDAO.insertAccount(account);	
	}

	@Override
	public boolean isUniqueAccount(String email, int id) {
		return accountDAO.isUniqueAccount(email, id);
	}

	@Override
	public boolean isUniqueAccount(String email) {
		return accountDAO.isUniqueAccount(email);
	}



}
