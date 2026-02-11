package com.project.entities;

import java.util.List;

import com.project.enums.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Item extends CommonModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "uuid", nullable = false, unique = true)
	private String uuid;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "price", nullable = false)
	private Double price;
	
	@Column(name = "discount_percent")
	private Double discountPercent;
	
	@Column(name = "discount_price")
	private Double discountedPrice;
	
	@Column(name = "final_price", nullable = false)
	private Double finalPrice;
	
	@Column(name = "brand", nullable = false)
	private String brand;
		
	@Column(name = "category", nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@Column(name = "sub_category", nullable = false)
	private String subCategory;
	
	@Column(name = "size", nullable = false)
	private String size;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "image_urls", nullable = false)
	private List<String> imageUrls;
	
}
