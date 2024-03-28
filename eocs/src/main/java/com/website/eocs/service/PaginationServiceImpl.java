package com.website.eocs.service;

import org.springframework.stereotype.Service;

import com.website.eocs.dto.PaginationDto;

@Service
public class PaginationServiceImpl {
	public PaginationDto getDataPagination(int totalData, int currentPage, int pageSize) {
		PaginationDto pagination = new PaginationDto();
		pagination.setPageSize(pageSize);
		pagination.setTotalPage(setInfoTotalPage(totalData, pageSize));
		pagination.setCurrentPage(checkCurrentPage(currentPage, pagination.getTotalPage()));
		int start = findStart(pagination.getCurrentPage(), pageSize);
		pagination.setStart(start);
		int end = findEnd(pagination.getStart(), pageSize, totalData);
		pagination.setEnd(end);
		return pagination;
	}
	
	private int findStart(int currentPage, int pageSize) {
		int start = 0;
		if (((currentPage - 1) * pageSize) < 1) {
			start = 0;
		} else {
			start = ((currentPage - 1) * pageSize);
		}
		return start;
	}

	private int findEnd(int start, int pageSize, int totalData) {
		return (start + pageSize) > totalData? totalData : (start + pageSize);
	}


	private int setInfoTotalPage(int totalData, int pageSize) {
		int totalPage = 0;
		totalPage = totalData/pageSize;
		totalPage = totalPage * pageSize < totalData? totalPage + 1: totalPage;
		return totalPage;
	}
	
	private int checkCurrentPage(int currentPage, int totalPage) {
	
		if (currentPage < 1) {
			return 1;
		}
		if (currentPage > totalPage) {
			return totalPage;
		}
		return currentPage;
	}
}


