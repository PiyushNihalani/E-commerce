package com.project.specification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.dto.ItemFilterDto;
import com.project.entities.Item;
import com.project.enums.Category;

import jakarta.persistence.criteria.Predicate;

public class ItemSpecification {

	public static Specification<Item> filterItems(ItemFilterDto itemFilterDto) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (ObjectUtils.isNotEmpty(itemFilterDto.getActive())) {
				predicates.add(cb.equal(root.get("active"), itemFilterDto.getActive()));
			}

			if (ObjectUtils.isNotEmpty(itemFilterDto.getLessThanPrice())) {
				predicates.add(cb.lessThanOrEqualTo(root.get("finalPrice"), itemFilterDto.getLessThanPrice()));
			}

			if (ObjectUtils.isNotEmpty(itemFilterDto.getMoreThanPrice())) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("finalPrice"), itemFilterDto.getMoreThanPrice()));
			}

			if (ObjectUtils.isNotEmpty(itemFilterDto.getCategory())) {
				try {
					Category categoryEnum = Category.valueOf(itemFilterDto.getCategory().trim().toUpperCase());
					predicates.add(cb.equal(root.get("category"), categoryEnum));

				} catch (IllegalArgumentException ignore) {
				}
			}

			if (StringUtils.isNotBlank(itemFilterDto.getBrand())) {
				predicates.add(cb.like(cb.lower(root.get("brand")), "%" + itemFilterDto.getBrand().toLowerCase() + "%"));
			}

			if (StringUtils.isNotBlank(itemFilterDto.getSize())) {
				predicates.add(cb.like(cb.lower(root.get("size")), itemFilterDto.getSize().toLowerCase()));
			}

			if (StringUtils.isNotBlank(itemFilterDto.getSubCategory())) {
				predicates.add(cb.like(cb.lower(root.get("subCategory")), "%" + itemFilterDto.getSubCategory().toLowerCase() + "%"));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

}
