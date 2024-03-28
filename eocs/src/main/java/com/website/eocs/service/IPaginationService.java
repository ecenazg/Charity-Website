package com.website.eocs.service;

import org.springframework.stereotype.Service;

import com.website.eocs.dto.PaginationDto;

@Service
public interface IPaginationService {
	public PaginationDto getDataPagination(int totalData, int currentPage, int pageSize);
}
