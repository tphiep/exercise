package com.exercise.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.io.IOException;
import java.util.List;

@Configuration
public class MongoConfig {

    @Autowired
    private Environment env;

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(env.getProperty("spring.data.mongodb.uri"));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongo(), env.getProperty("spring.data.mongodb.database"));
        MappingMongoConverter converter = (MappingMongoConverter) mongoTemplate.getConverter();
        converter.setCustomConversions(getMongoCustomConversions());
        converter.afterPropertiesSet();
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // remove _class
        return mongoTemplate;
    }

    @Bean
    public MongoCustomConversions getMongoCustomConversions() {
        return new MongoCustomConversions(
                List.of(
                        new JsonNodeToDocumentConverter(),
                        new DocumentToJsonNodeConverter()
                    ));
    }

//    class LocalDateTimeConverter implements Converter<LocalDateTime, String> {
//        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        @Override
//        public String convert(LocalDateTime source) {
//            return formatter.format(source);
//        }
//    }
//
    @WritingConverter
    class JsonNodeToDocumentConverter implements Converter<JsonNode, Document> {
        @Override
        public Document convert(final JsonNode source) {
            if (source == null) return null;
            return Document.parse(source.toString());
        }
    }

    @ReadingConverter
    class DocumentToJsonNodeConverter implements Converter<Document, JsonNode> {
        ObjectMapper mapper = new ObjectMapper();
        @Override
        public JsonNode convert(Document source) {
            if(source == null) return null;
            try {
                return mapper.readTree(source.toJson());
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert Document to JsonNode", e);
            }
        }
    }
}
