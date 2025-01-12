package com.mono.trigo.domain.area.repository;

import com.mono.trigo.domain.area.entity.AreaDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaDetailRepository extends JpaRepository<AreaDetail, Long> {

    AreaDetail findByAreaCodeAndCode(String areaCode, String code);
}
