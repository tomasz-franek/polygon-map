package org.polygonMap.backend.controllers;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BaseMongoTest {
    @BeforeAll
    public static void createMongoDbSchema() throws LiquibaseException {
        MongoLiquibaseDatabase database = (MongoLiquibaseDatabase) DatabaseFactory.getInstance()
                .openDatabase("mongodb://127.0.0.1:27017/test", null, null, null, null);
        final Liquibase liquibase = new Liquibase("db/changelog/db.changelog-test.yaml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
    }
}
