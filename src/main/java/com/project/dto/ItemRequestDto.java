package com.project.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

	@NotBlank(message = "{item.name.blank}")
	private String name;
	
	@NotBlank(message = "{item.category.blank}")
	private String category;
	
	@NotBlank(message = "{item.sub.category.blank}")
	private String subCategory;

	@NotBlank(message = "{item.brand.blank}")
	private String brand;
	
	@NotBlank(message = "{item.size.blank}")
	private String size;
	
	@NotNull(message = "{item.price.null}")
	@Min(value = 0, message = "{item.price.invalid}")
	private Double price;
	
	@Max(value = 100, message = "{item.discount.percent.invalid}")
	@Min(value = 0, message = "{item.discount.percent.invalid}")
	private Double discountPercent;
	
	@Min(value = 0, message = "{item.discount.price.invalid}")
	private Double discountedPrice;
	
	@NotBlank(message = "{item.desc.blank}")
	private String description;
	
	@NotEmpty(message = "{item.file.empty}")
	private List<MultipartFile> imageFiles;
	
}
