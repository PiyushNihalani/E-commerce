package com.project.service;

import org.springframework.data.domain.Page;

import com.project.dto.ItemFilterDto;
import com.project.dto.ItemRequestDto;
import com.project.dto.ItemResponseDto;
import com.project.entities.Item;
import com.project.exception.NotFoundException;
import com.project.exception.ValidationException;

public interface ItemService {

	/**
	 * 
	 * @param itemRequestDto
	 * @return
	 * @throws ValidationException 
	 */
	public ItemResponseDto addItem(ItemRequestDto itemRequestDto) throws ValidationException;

	/**
	 * 
	 * @param uuid
	 * @return
	 * @throws NotFoundException 
	 */
	public ItemResponseDto getItemByUuid(String uuid) throws NotFoundException;

	/**
	 * 
	 * @param uuid
	 * @param itemRequestDto
	 * @return
	 * @throws NotFoundException 
	 * @throws ValidationException 
	 */
	public ItemResponseDto updateItem(String uuid, ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException;

	/**
	 * 
	 * @param uuid
	 * @param active
	 * @throws NotFoundException 
	 * @throws ValidationException 
	 */
	public void changeStatus(String uuid, Boolean active) throws NotFoundException, ValidationException;

	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param itemFilterDto
	 * @return
	 */
	public Page<Item> allItems(Integer pageNumber, Integer pageSize, ItemFilterDto itemFilterDto);

}
