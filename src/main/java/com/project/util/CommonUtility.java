package com.project.util;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.project.repository.CustomJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonUtility {

	public static final Predicate<String> NOT_NULL_NOT_EMPTY_STRING = s -> s != null && !s.isEmpty();

	public static final Predicate<String> NOT_NULL_NOT_EMPTY_NOT_BLANK_STRING = s -> s != null && !s.isEmpty() && !s.isBlank();

	public static final Predicate<List<?>> NOT_NULL_NOT_EMPTY_LIST = s -> s != null && !s.isEmpty();

	public static final Predicate<Map<?, ?>> NOT_NULL_NOT_EMPTY_MAP = s -> s != null && !s.isEmpty();
	
	/**
	 * Generate uuid pattern (XXXX-XXXX-XXXX-XXXX)
	 * 
	 * @param clazz
	 * @return
	 */
	public static String generateUuid(final CustomJpaRepository<?> clazz) {
		String uuid = CommonUtility.getRandomAlphaNumericNumber();
		if (clazz.findByUuid(uuid).isPresent()) {
			return generateUuid(clazz);
		} else {
			return uuid;
		}
	}
	
	/**
	 * Method to trim String fields in an object
	 * 
	 * @param obj
	 */
	public static void trimAllStringFields(Object obj) {
        if (obj == null) return;

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null) {
                        field.set(obj, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error trimming field: " + field.getName(), e);
                }
            }
        }
    }
	
	private static String getRandomAlphaNumericNumber() {
		final SecureRandom number = new SecureRandom();
		String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder sb = new StringBuilder(20);
		for (int i = 0; i < 19; i++) {
			if ((i + 1) % 5 == 0) {
				sb.append("-");
			} else {
				sb.append(alphaNumeric.charAt(number.nextInt(alphaNumeric.length())));
			}
		}
		return sb.toString();
	}
	
}
