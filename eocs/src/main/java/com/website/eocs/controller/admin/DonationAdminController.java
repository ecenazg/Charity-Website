package com.website.eocs.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.website.eocs.entity.Categories;
import com.website.eocs.dto.PaginationDto;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.PaginationServiceImpl;

@Validated
@Controller
@RequestMapping(value = { "/admin" })
public class DonationAdminController {
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;

	// DONATIONS
	@RequestMapping(value = "/donation_management")
	public ModelAndView donationMangement(@RequestParam(name = "page", defaultValue = "1") String page) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.getDataDonations().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		mv.addObject("donationsPagination",
				adminServiceImpl.getDataDonationsPagination(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/donation_management");
		return mv;
	}

	@RequestMapping(value = "/search_donation")
	public ModelAndView searchDonation(@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "accountName", required = false) String accountName,
			@RequestParam(name = "fundName", required = false) String fundName,
			@RequestParam(name = "page", defaultValue = "1") String page) {

		ModelAndView mv = new ModelAndView();
		int donationId;
		try {
			donationId = Integer.parseInt(id);
		} catch (Exception e) {
			donationId = -1;
		}
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.searchDonations(donationId, accountName, fundName).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		mv.addObject("searchDonationsPagination", adminServiceImpl.searchDonationsPagination(donationId, accountName,
				fundName, paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/search_donation");
		return mv;
	}

	

}
