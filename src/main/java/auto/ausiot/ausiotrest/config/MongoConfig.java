package auto.ausiot.ausiotrest.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages="auto.ausiot.ausiotrest.repository")
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "test"; //name of database in mongo
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        // ip and port for mongodb
        return new MongoClient("localhost", 27017);
    }
}