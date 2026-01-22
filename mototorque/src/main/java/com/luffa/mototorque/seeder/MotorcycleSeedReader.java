package com.luffa.mototorque.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.luffa.mototorque.seeder.dto.MotorcycleSeedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MotorcycleSeedReader {

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public List<MotorcycleSeedDTO> readAll(){
        try{
            Resource[] resources = resolver.getResources("classpath:data/motorcycles/**/*.yml");
            List<MotorcycleSeedDTO> seeds = new ArrayList<>();
            for (Resource resource : resources) {
                seeds.add(yamlMapper.readValue(resource.getInputStream(), MotorcycleSeedDTO.class));
            }
            return seeds;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read motorcycle seed files", e);
        }
    }
}
