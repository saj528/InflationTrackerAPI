package com.proteamgroup.inflationtrackerapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proteamgroup.inflationtrackerapi.domain.InflationEntity;

@Repository
public interface InflationRepository extends JpaRepository<InflationEntity, Long> {

}
