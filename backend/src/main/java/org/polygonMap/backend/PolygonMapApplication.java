package org.polygonMap.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.polygonMap")
@RequiredArgsConstructor
public class PolygonMapApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolygonMapApplication.class, args);
    }
}
