package org.polygonMap.backend;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan("org.polygonMap")
@RequiredArgsConstructor
public class PolygonMapApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolygonMapApplication.class, args);
    }

    @Bean
    public Liquibase liquibase() throws LiquibaseException {
        MongoLiquibaseDatabase database = (MongoLiquibaseDatabase) DatabaseFactory.getInstance()
                .openDatabase("mongodb://127.0.0.1:27017/test", null, null, null, null);

        final Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
        liquibase.close();
        return liquibase;
    }
}
