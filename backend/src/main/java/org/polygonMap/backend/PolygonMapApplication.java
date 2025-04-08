package org.polygonMap.backend;

import com.mongodb.client.MongoDatabase;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.executor.ExecutorService;
import liquibase.ext.mongodb.database.MongoConnection;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.nosql.executor.NoSqlExecutor;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import static liquibase.nosql.executor.NoSqlExecutor.EXECUTOR_NAME;

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
        NoSqlExecutor executor;
        MongoConnection connection = (MongoConnection) database.getConnection();
        MongoDatabase mongoDatabase = connection.getMongoDatabase();
        executor = (NoSqlExecutor) Scope.getCurrentScope().getSingleton(ExecutorService.class)
                .getExecutor(EXECUTOR_NAME, database);
        final Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update("");
        connection.close();
        database.close();
        return liquibase;
    }
}
