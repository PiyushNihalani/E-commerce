package com.project.dto;

import java.util.List;

import com.project.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

	private String uuid;
	
	private String name;
	
	private Boolean active;
	
	private Double price;
	
	private Double discountPercent;
	
	private Double finalPrice;
	
	private String brand;
		
	private Category category;
	
	private String subCategory;
		
	private String size;
	
	private String description;
	
	private List<String> imageUrls;
	
}
