package com.luffa.mototorque.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.luffa.mototorque.dto.MotorcycleSpecPack;
import com.luffa.mototorque.model.MotorcycleSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecPackExporter implements CommandLineRunner {

    private final ObjectMapper yamlMapper =
            new ObjectMapper(new YAMLFactory());
    private final ObjectMapper jsonMapper =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Value("classpath:data/motorcycles/**/*.yml")
    private Resource[] motorcycleFiles;

    @Override
    public void run(String... args) throws Exception {
        List<MotorcycleSpec> specs = new ArrayList<>();

        for (Resource file : motorcycleFiles) {
            try (InputStream is = file.getInputStream()) {
                specs.add(yamlMapper.readValue(is, MotorcycleSpec.class));
            }
        }

        MotorcycleSpecPack pack = new MotorcycleSpecPack();
        pack.setVersion(LocalDate.now().toString());
        pack.setMotorcycles(specs);

        Path target = Paths.get("target/spec-pack.json");
        Files.createDirectories(target.getParent());
        jsonMapper.writeValue(target.toFile(), pack);

        System.out.println("Spec-pack zapisany do: " + target.toAbsolutePath());

        Path staticDir = Paths.get("mototorque/src/main/resources/static/data");
        Files.createDirectories(staticDir);
        Path staticTarget = staticDir.resolve("spec-pack.json");
        Files.copy(target, staticTarget, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Skopiowano do: " + staticTarget.toAbsolutePath());
    }
}