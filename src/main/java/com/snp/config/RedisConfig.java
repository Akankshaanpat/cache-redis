package com.snp.config;
 
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
  
@Configuration // To mark class as configuration class
@EnableCaching // @EnableCaching enables caching support
public class RedisConfig {

 
	 
// Inject 'RedisConnectionFactory' bean into class. This connection factory is used to connect to the Redis server.
	@Autowired
	private RedisConnectionFactory connectionFactory;

	@Bean // Here redisCacheManager() is annotated as Bean that creates and return
			// CacheManager bean for Redis Caching
	CacheManager redisCacheManager() {
		// ObjectMapper is a class provided by Jackson library in java it is like translator between Java object and JSON. Its job is to
		// convert(serialize) Java object into JSON format and vice versa.
		ObjectMapper objectMapper = new ObjectMapper();
		// Serialization means converting java object to JSON
		objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);// this tells the ObjectMapper to ignore any fields in
																	// your java object that dont have corresponding
																	// fields in JSON during serialization.
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// tells ObjectMapper to ignore any
																				// extra field in JSON when converting
																				// it back to java object.

		// PolymorphicTypeValidator is interface and acts like guard while handling
		// different types of objects when converting between java object and JSON
		// it is part of Jackson library in java. it is used to define which types are
		// allowed to deserialize

		PolymorphicTypeValidator ptv = LaissezFaireSubTypeValidator.instance;// LaissezFaireSubTypeValidator is class in jackson library. 
																	//"laissez-faire" means it allows flexibility, not imposing strict rules on handling different types.
																	// Creates an instance of a flexible tool for handling different types during JSON processing.
								
		objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
		// Tells Jackson to include type information for classes that are not marked as final, allowing it to correctly 
		// serialize and deserialize objects of those classes, even if they have subclasses.
//here pvt checks if classes can be safely used in default typing.
		//activateDefaultTyping is a method used to turn on feature called default typing.
   
		GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);// GenericJackson2JsonRedisSerializer is a class that implements RedisSerializer, provided by spring data redis library.
		// and is used to serialize and deserialize java object into JSON format for storage in redis.
		// this objectMapper is responsible for converting java object to JSON format during serialization and deserialization and vice versa.
		// Here deserialization means while retrieving the value from redis that was stored in JSON format, the value need to be converted back into a java object. This is called deserialization.

		// Configure RedisCacheManager
		RedisSerializationContext.SerializationPair<Object> jsonSerializer = RedisSerializationContext.SerializationPair
				.fromSerializer(jsonRedisSerializer);// RedisSerializationContext provides a way to configure the serialization and deserialization of objects when interacting with redis. The <Object> part specifie the types of objects that will
		//serialized or deserialized. in this case it is configured for generic object serialization.

		return RedisCacheManager
				.builder(connectionFactory).cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
						.entryTtl(Duration.ofSeconds(30)).serializeValuesWith(jsonSerializer).disableCachingNullValues())
				.build();
}
	
}
              