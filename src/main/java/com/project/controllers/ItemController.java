package com.project.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ItemFilterDto;
import com.project.dto.ItemRequestDto;
import com.project.dto.ItemResponseDto;
import com.project.entities.Item;
import com.project.exception.NotFoundException;
import com.project.exception.ValidationException;
import com.project.locale.MessageByLocaleService;
import com.project.mapper.ItemMapper;
import com.project.responseHandler.GenericResponseHandlers;
import com.project.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clothes")
@Slf4j
public class ItemController {

	private final ItemService itemService;
	private final MessageByLocaleService messageByLocaleService;
	private final ItemMapper itemMapper;

	/**
	 * Controller to view all available items
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param itemFilterDto
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getAll(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,
			@RequestBody ItemFilterDto itemFilterDto) {
		log.info("inside ItemController::getAll");
		Page<Item> allItems = itemService.allItems(pageNumber - 1, pageSize, itemFilterDto);
		return new GenericResponseHandlers.Builder().setMessage(messageByLocaleService.getMessage("item.fetch.all.success", null))
				.setStatus(HttpStatus.OK).setData(itemMapper.listToResponse(allItems.getContent()))
				.setPageNumber(pageNumber - 1).setPageSize((long) pageSize).setTotalPages(allItems.getTotalPages())
				.setTotalCount(allItems.getTotalElements()).setHasNextPage(allItems.hasNext())
				.setHasPreviousPage(allItems.hasPrevious()).create();
	}

	/**
	 * Controller to add an item
	 * 
	 * @param itemRequestDto
	 * @return
	 * @throws ValidationException
	 */
	@PostMapping
	public ResponseEntity<?> addItem(@Valid @ModelAttribute ItemRequestDto itemRequestDto) throws ValidationException {
		log.info("inside ItemController::addItem");
		ItemResponseDto itemResponseDto = itemService.addItem(itemRequestDto);
		return new GenericResponseHandlers.Builder().setMessage(messageByLocaleService.getMessage("item.add.success", null))
				.setStatus(HttpStatus.OK).setData(itemResponseDto).create();
	}
	
	/**
	 * Controller to get an item by uuid
	 * 
	 * @param uuid
	 * @return
	 * @throws NotFoundException
	 */
	@GetMapping("/uuid")
	public ResponseEntity<?> getByUuid(@RequestParam String uuid) throws NotFoundException{
		log.info("inside ItemController::getByUuid");
		ItemResponseDto itemResponseDto = itemService.getItemByUuid(uuid);
		return new GenericResponseHandlers.Builder().setMessage(messageByLocaleService.getMessage("item.get.uuid.success", null))
				.setStatus(HttpStatus.OK).setData(itemResponseDto).create();
	}
	
	/**
	 * Controller to update an existing item
	 * 
	 * @param itemRequestDto
	 * @return
	 * @throws NotFoundException
	 * @throws ValidationException
	 */
	@PutMapping
	public ResponseEntity<?> updateItem(@Valid @ModelAttribute ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException{
		log.info("inside ItemController::updateItem");
		ItemResponseDto itemResponseDto = itemService.updateItem(itemRequestDto);
		return new GenericResponseHandlers.Builder().setMessage(messageByLocaleService.getMessage("item.update.success", null))
				.setStatus(HttpStatus.OK).setData(itemResponseDto).create();
	}
	
	/**
	 * Controller to deactive an item
	 * 
	 * @param uuid
	 * @param active
	 * @return
	 * @throws NotFoundException
	 * @throws ValidationException
	 */
	@PutMapping("/change/status")
	public ResponseEntity<?> changeStatus(@RequestParam String uuid, @RequestParam Boolean active) throws NotFoundException, ValidationException{
		log.info("inside ItemController::changeStatus");
		itemService.changeStatus(uuid, active);
		return new GenericResponseHandlers.Builder().setMessage(messageByLocaleService.getMessage("item.change.status.success", null))
				.setStatus(HttpStatus.OK).create();
	}

}
