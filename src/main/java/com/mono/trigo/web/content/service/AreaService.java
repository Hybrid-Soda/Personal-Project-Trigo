package com.mono.trigo.web.content.service;

import com.mono.trigo.domain.area.repository.AreaRepository;
import org.springframework.stereotype.Service;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public void createArea() {


    }
}
