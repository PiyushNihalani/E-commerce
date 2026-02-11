package com.project.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.entities.Item;

public interface ItemRepository extends CustomJpaRepository<Item>, JpaSpecificationExecutor<Item>{

	
}
