package com.project.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.project.dto.ItemFilterDto;
import com.project.dto.ItemRequestDto;
import com.project.dto.ItemResponseDto;
import com.project.entities.Item;
import com.project.exception.NotFoundException;
import com.project.exception.ValidationException;
import com.project.locale.MessageByLocaleService;
import com.project.mapper.ItemMapper;
import com.project.repository.ItemRepository;
import com.project.service.ItemService;
import com.project.specification.ItemSpecification;
import com.project.util.CommonUtility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final ItemMapper itemMapper;
	private final MessageByLocaleService message;
	private final Cloudinary cloudinary;

	@Override
	@Transactional
	public ItemResponseDto addItem(ItemRequestDto itemRequestDto) throws ValidationException {
		log.info("inside ItemServiceImpl::addItem");
		CommonUtility.trimAllStringFields(itemRequestDto);
		if (ObjectUtils.isNotEmpty(itemRequestDto.getDiscountedPrice())
				&& (itemRequestDto.getDiscountedPrice() > itemRequestDto.getPrice())) {
			throw new ValidationException(message.getMessage("item.discounted.price.greater", null));
		}
		Item item = itemMapper.requestToEntity(itemRequestDto);
		item.setUuid(CommonUtility.generateUuid(itemRepository));
		item.setActive(Boolean.TRUE);
		item = setPrices(item);
		item.setImageUrls(uploadImages(itemRequestDto.getImageFiles(),
				"items/" + item.getCategory().toString().toLowerCase(), item.getUuid()));
		itemRepository.save(item);
		return itemMapper.entityToResponse(item);
	}

	@Override
	public ItemResponseDto getItemByUuid(String uuid) throws NotFoundException {
		log.info("inside ItemServiceImpl::getItemByUuid");
		return itemMapper.entityToResponse(itemRepository.findByUuid(uuid).orElseThrow(
				() -> new NotFoundException(message.getMessage("item.uuid.not.found", new Object[] { uuid }))));
	}

	@Override
	@Transactional
	public ItemResponseDto updateItem(ItemRequestDto itemRequestDto) throws NotFoundException, ValidationException {
		log.info("inside ItemServiceImpl::updateItem");
		if (StringUtils.isBlank(itemRequestDto.getUuid())) {
			throw new ValidationException("item.uuid.invalid");
		}
		String uuid = itemRequestDto.getUuid();
		Item existingItem = itemRepository.findByUuid(uuid).orElseThrow(
				() -> new NotFoundException(message.getMessage("item.uuid.not.found", new Object[] { uuid })));
		CommonUtility.trimAllStringFields(itemRequestDto);
		if (ObjectUtils.isNotEmpty(itemRequestDto.getDiscountedPrice())
				&& (itemRequestDto.getDiscountedPrice() > itemRequestDto.getPrice())) {
			throw new ValidationException(message.getMessage("item.discounted.price.greater", null));
		}
		Item newItem = itemMapper.requestToEntity(itemRequestDto, existingItem);
		newItem = setPrices(newItem);
		deleteImages(newItem.getImageUrls());
		newItem.setImageUrls(uploadImages(itemRequestDto.getImageFiles(),
				"items/" + newItem.getCategory().toString().toLowerCase(), newItem.getUuid()));
		itemRepository.save(newItem);
		return itemMapper.entityToResponse(newItem);
	}

	@Override
	@Transactional
	public void changeStatus(String uuid, Boolean active) throws NotFoundException, ValidationException {
		log.info("inside ItemServiceImpl::changeStatus");
		Item item = itemRepository.findByUuid(uuid).orElseThrow(
				() -> new NotFoundException(message.getMessage("item.uuid.not.found", new Object[] { uuid })));

		if (active == null) {
			throw new ValidationException(message.getMessage("item.active.null", null));
		} else if (item.getActive().equals(active)) {
			throw new ValidationException(message.getMessage("item.active.value", new Object[] { active }));
		}

		item.setActive(active ? Boolean.TRUE : Boolean.FALSE);
		itemRepository.save(item);
	}

	@Override
	public Page<Item> allItems(Integer pageNumber, Integer pageSize, ItemFilterDto itemFilterDto) {
		log.info("inside ItemServiceImpl::allItems");
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
		Specification<Item> spec = ItemSpecification.filterItems(itemFilterDto);
		return itemRepository.findAll(spec, pageable);
	}

	/**
	 * Set DiscountedPrice, DiscountPercentage, Final Price according to provided
	 * values
	 * 
	 * @param item
	 * @return
	 */
	private Item setPrices(Item item) {
		log.info("inside ItemServiceImpl::setPrices");
		if (item.getDiscountedPrice() != null || item.getDiscountPercent() != null) {
			Double finalPrice;
			if (item.getDiscountedPrice() == null && item.getDiscountPercent() != null) {
				finalPrice = calculateFinalPrice(item.getPrice(), item.getDiscountPercent());
				item.setDiscountedPrice(finalPrice);
			} else {
				finalPrice = item.getDiscountedPrice();
				item.setDiscountPercent(calculateDiscountPercentage(item.getPrice(), item.getDiscountedPrice()));
			}
			item.setFinalPrice(finalPrice);
		} else {
			item.setFinalPrice(item.getPrice());
		}
		return item;
	}

	/**
	 * Calculate final Price from price and percentage
	 * 
	 * @param price
	 * @param discountPercentage
	 * @return
	 */
	private Double calculateFinalPrice(Double price, Double discountPercentage) {
		log.info("inside ItemServiceImpl::calculateFinalPrice");
		if (price == null || discountPercentage == null) {
			return price;
		}

		double discountAmount = price * (discountPercentage / 100);
		double finalPrice = price - discountAmount;

		return Math.round(finalPrice * 100.0) / 100.0;

	}

	/**
	 * Calculate Discount Percentage from provided discounted Price
	 * 
	 * @param price
	 * @param discountPrice
	 * @return
	 */
	private Double calculateDiscountPercentage(Double price, Double discountPrice) {
		log.info("inside ItemServiceImpl::calculateDiscountPercentage");
		if (price == null || discountPrice == null) {
			return 0.0;
		}

		if (discountPrice > price) {
			throw new IllegalArgumentException(message.getMessage("item.discount.price.greater", null));
		}

		double discountAmount = price - discountPrice;
		double discountPercentage = (discountAmount / price) * 100;

		return Math.round(discountPercentage * 100.0) / 100.0;

	}

	/**
	 * uploads the images to the cloud and fetches the urls of the images.
	 * 
	 * @param files
	 * @param folderPath
	 * @param uuid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> uploadImages(List<MultipartFile> files, String folderPath, String uuid) {
		log.info("inside ItemServiceImpl::uploadImages");
		List<String> imageUrls = new ArrayList<>();

		List<MultipartFile> validFiles = files.stream().filter(Objects::nonNull).filter(file -> !file.isEmpty())
				.filter(file -> {
					String name = file.getOriginalFilename();
					return name != null && name.toLowerCase().matches(".*\\.(jpg|jpeg|png|webp)$");
				}).toList();

		int idx = 1;
		for (MultipartFile file : validFiles) {

			String fileName = (idx++) + "_" + uuid + "_" + System.currentTimeMillis();
			try {
				Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
						com.cloudinary.utils.ObjectUtils.asMap("folder", folderPath, "public_id", fileName,
								"resource_type", "image"));

				String secureUrl = uploadResult.get("secure_url").toString();
				imageUrls.add(secureUrl);

			} catch (IOException e) {
				throw new RuntimeException("Image upload failed");
			}
		}

		return imageUrls;
	}

	/**
	 * Delete old photos to update the item with new photos.
	 * 
	 * @param publicIds
	 */
	public void deleteImages(List<String> imageUrls) {
		log.info("inside ItemServiceImpl::deleteImages");
		/**
		 * The url always contains "/upload/" and then - "version/public_id". We need to
		 * fetch that public_id without extension to delete it.
		 */
		List<String> publicIds = imageUrls.stream().map(url -> {
			String afterUpload = url.substring(url.indexOf("/upload/") + 8);
			return afterUpload.substring(afterUpload.indexOf("/") + 1, afterUpload.lastIndexOf("."));
		}).toList();

		for (String publicId : publicIds) {
			try {
				cloudinary.uploader().destroy(publicId, com.cloudinary.utils.ObjectUtils.emptyMap());
			} catch (Exception e) {
				throw new RuntimeException("Failed to delete image: " + publicId);
			}
		}
	}

}
