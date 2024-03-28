package com.website.eocs.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.website.eocs.dto.PaginationDto;
import com.website.eocs.entity.Fund;
import com.website.eocs.service.AdminServiceImpl;
import com.website.eocs.service.PaginationServiceImpl;

@Validated
@Controller
@RequestMapping(value = { "/admin" })
public class FundAdminController {
	@Autowired
	AdminServiceImpl adminServiceImpl;
	@Autowired
	PaginationServiceImpl paginationService;

	// FUNDS
	@RequestMapping(value = "/fund_management")
	public ModelAndView fundManagement(@RequestParam(name = "page", defaultValue = "1") String page) {
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}
		int totalData = adminServiceImpl.getDataFunds().size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		ModelAndView mv = new ModelAndView();
		mv.addObject("fundsPagination",
				adminServiceImpl.getDataFundsPagination(paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/fund_management");
		return mv;
	}

	@RequestMapping(value = "/search_fund")
	public ModelAndView searchFund(@RequestParam(name = "id", required = false) String id,
			@RequestParam(name = "fundName", required = false) String fundName,
			@RequestParam(name = "categoryName", required = false) String categoryName,
			@RequestParam(name = "page", defaultValue = "1") String page) {

		ModelAndView mv = new ModelAndView();
		int fundId;
		try {
			fundId = Integer.parseInt(id);
		} catch (Exception e) {
			fundId = -1;
		}
		int pageSize = 6;
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(page);
		} catch (Exception e) {
			currentPage = 1;
		}

		int totalData = adminServiceImpl.searchFunds(fundId, fundName, categoryName).size();
		PaginationDto paginationInfo = paginationService.getDataPagination(totalData, currentPage, pageSize);
		mv.addObject("searchFundsPagination", adminServiceImpl.searchFundsPagination(fundId, fundName, categoryName,
				paginationInfo.getStart(), paginationInfo.getEnd()));
		mv.addObject("pagination", paginationInfo);
		mv.setViewName("admin/search_fund");
		return mv;
	}

	@RequestMapping(value = "/fund_management/delete/{id}")
	public String deleteFund(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteFund(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/fund_management/undelete/{id}")
	public String unDeleteFund(@PathVariable int id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.unDeleteFund(id);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/fund_management/delete_multi")
	public String deleteMultipleFunds(@RequestParam(name = "ids") List<Integer> ids, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		adminServiceImpl.deleteMultipleFunds(ids);
		return "redirect:" + referer;
	}

	@RequestMapping(value = "/fund_management/edit_fund/{id}")
	public ModelAndView editFunds(@PathVariable int id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("fund", adminServiceImpl.getDataFundById(id));
		mv.addObject("statusList", adminServiceImpl.statusListForFunds());
		mv.addObject("categoryList", adminServiceImpl.getDataCategories());
		mv.addObject("foundationList", adminServiceImpl.getDataFoundation());
		mv.setViewName("admin/edit_fund");
		return mv;
	}

	@RequestMapping(value = "/update_fund", method = RequestMethod.POST)
	public String upDateFund(@javax.validation.Valid @ModelAttribute("fund") Fund fund, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueFund(fund.getName(), fund.getId())) {
				FieldError error = new FieldError("fund", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}

			model.addAttribute("name", fund.getName());
			model.addAttribute("statusList", adminServiceImpl.statusListForFunds());
			model.addAttribute("categoryList", adminServiceImpl.getDataCategories());
			model.addAttribute("foundationList", adminServiceImpl.getDataFoundation());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_fund";
		}
		if (!adminServiceImpl.isUniqueFund(fund.getName(), fund.getId())) {
			FieldError error = new FieldError("fund", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", fund.getName());
			model.addAttribute("statusList", adminServiceImpl.statusListForFunds());
			model.addAttribute("categoryList", adminServiceImpl.getDataCategories());
			model.addAttribute("foundationList", adminServiceImpl.getDataFoundation());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/edit_fund";
		}
		
		
		int categoryId = adminServiceImpl.getCategoryIdByName(fund.getCategoryName());
		int foundationId = adminServiceImpl.getFoundationIdByName(fund.getFoundationName());
		adminServiceImpl.updateFund(fund, categoryId, foundationId);
		return "redirect: fund_management";
	}

	@RequestMapping(value = "/fund_management/add_fund")
	public ModelAndView addFunds() {
		Fund fund = new Fund();
		ModelAndView mv = new ModelAndView();
		mv.addObject("fund", fund);
		mv.addObject("statusList", adminServiceImpl.statusListForFunds());
		mv.addObject("categoryList", adminServiceImpl.getDataCategories());
		mv.addObject("foundationList", adminServiceImpl.getDataFoundation());
		mv.setViewName("admin/add_fund");
		return mv;
	}

	@RequestMapping(value = "/insert_fund", method = RequestMethod.POST)
	public String insertFund(@javax.validation.Valid @ModelAttribute("fund") Fund fund, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			if (!adminServiceImpl.isUniqueFund(fund.getName(), fund.getId())) {
				FieldError error = new FieldError("fund", "name", "Tên đã tồn tại");
				bindingResult.addError(error);
			}
			model.addAttribute("name", fund.getName());
			model.addAttribute("statusList", adminServiceImpl.statusListForFunds());
			model.addAttribute("categoryList", adminServiceImpl.getDataCategories());
			model.addAttribute("foundationList", adminServiceImpl.getDataFoundation());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_fund";
		}
		if (!adminServiceImpl.isUniqueFund(fund.getName(), fund.getId())) {
			FieldError error = new FieldError("fund", "name", "Tên đã tồn tại");
			bindingResult.addError(error);
			model.addAttribute("name", fund.getName());
			model.addAttribute("statusList", adminServiceImpl.statusListForFunds());
			model.addAttribute("categoryList", adminServiceImpl.getDataCategories());
			model.addAttribute("foundationList", adminServiceImpl.getDataFoundation());
			model.addAttribute("errors", bindingResult.getAllErrors());
			return "admin/add_fund";
		}
		int categoryId = adminServiceImpl.getCategoryIdByName(fund.getCategoryName());
		int foundationId = adminServiceImpl.getFoundationIdByName(fund.getFoundationName());
		adminServiceImpl.insertFund(fund, categoryId, foundationId);
		return "redirect: fund_management";
	}

	

}
