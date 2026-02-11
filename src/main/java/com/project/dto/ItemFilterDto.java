package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemFilterDto {

	private Double lessThanPrice;
	
	private Double moreThanPrice;
	
	private String brand;
	
	private String category;
	
	private String subCategory;
		
	private String size;
	
}
