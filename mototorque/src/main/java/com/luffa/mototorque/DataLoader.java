package com.luffa.mototorque;

import com.luffa.mototorque.seeder.MotorcycleSeedPersister;
import com.luffa.mototorque.seeder.MotorcycleSeedReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MotorcycleSeedReader seedReader;
    private final MotorcycleSeedPersister seedPersister;

    public DataLoader(MotorcycleSeedReader seedReader,
                      MotorcycleSeedPersister seedPersister) {
        this.seedReader = seedReader;
        this.seedPersister = seedPersister;
    }

    @Override
    public void run(String... args) {
        seedReader.readAll().forEach(seedPersister::persist);
    }
}