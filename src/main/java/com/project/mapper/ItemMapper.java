package com.project.mapper;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.project.dto.ItemRequestDto;
import com.project.dto.ItemResponseDto;
import com.project.entities.Item;
import com.project.enums.Category;
import com.project.locale.MessageByLocaleService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemMapper {

	private final MessageByLocaleService message;
	
	public Item requestToEntity(ItemRequestDto itemRequestDto) {
		Item item = new Item();
		BeanUtils.copyProperties(itemRequestDto, item);
		item.setCategory(convertToCategory(itemRequestDto.getCategory()));
		return item;
	}
	
	public Item requestToEntity(ItemRequestDto itemRequestDto, Item item) {
		Item newItem = new Item();
		BeanUtils.copyProperties(item, newItem);
		BeanUtils.copyProperties(itemRequestDto, item);
		newItem.setCategory(convertToCategory(itemRequestDto.getCategory()));
		return item;
	}
	
	public ItemResponseDto entityToResponse(Item item) {
		ItemResponseDto itemResponseDto = new ItemResponseDto();
		BeanUtils.copyProperties(item, itemResponseDto);
		return itemResponseDto;
	}
	
	public List<ItemResponseDto> listToResponse(List<Item> items){
		return items.stream().map(this::entityToResponse).toList();
	}
	
	private Category convertToCategory(String input) {
	    try {
	        return Category.valueOf(input.trim().toUpperCase());
	    } catch (IllegalArgumentException e) {
	        throw new RuntimeException(message.getMessage("category.input.invalid", new Object[] {input}));
	    }
	}
	
}
