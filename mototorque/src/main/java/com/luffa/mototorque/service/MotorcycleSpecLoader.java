package com.luffa.mototorque.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.luffa.mototorque.model.MotorcycleSpec;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MotorcycleSpecLoader {

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final Map<String, MotorcycleSpec> cache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IOException{
        var resolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resolver.getResources("classpath:data/motorcycles/**/*.yml");

        for (Resource resource : files) {
            MotorcycleSpec spec = yamlMapper.readValue(resource.getInputStream(), MotorcycleSpec.class);

            if (spec.getId() == null || spec.getId().isBlank()){
                String generatedId = resource.getFilename().replace(".yml","").toLowerCase();
                spec.setId(generatedId);
            }

            cache.put(spec.getId(), spec);
            log.info("Loaded spec {} ({})", spec.getId(), resource.getFilename());
        }

        log.info("Loaded {} motorcycle specs", cache.size());
    }

    public Collection<MotorcycleSpec> getAll(){
        return cache.values();
    }

    public MotorcycleSpec getById(String id){
        return cache.get(id);
    }
}
