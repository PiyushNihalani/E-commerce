package com.project.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommonModel {

	@Column(name = "active",nullable = false)
	private Boolean active;
	
	@Column(name = "created_at",updatable = false,nullable = false)
	@CreatedDate
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at",nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private Long createdBy;
	
	@Column(name = "updated_by")
	private Long updatedBy;
	
}
