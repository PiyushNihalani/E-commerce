package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T> extends JpaRepository<T, Long> {

	Optional<T> findByUuid(String uuid);

	Optional<T> findByUuidAndActive(String uuid, Boolean active);

}
