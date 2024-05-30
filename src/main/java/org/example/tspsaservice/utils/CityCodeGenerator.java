package org.example.tspsaservice.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CityCodeGenerator implements CodeGenerator {

    private final String suffix = "";
    private final String prefix = "";

    @Override
    public String generate() {

        Random rand = new Random();
        return prefix + Math.abs(rand.nextInt()) + suffix;
    }
}
