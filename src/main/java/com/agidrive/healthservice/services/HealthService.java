package com.agidrive.healthservice.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.bson.Document;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * The health service
 */
@Service
public class HealthService {

    // constants
    private static final String MONGODB_URI_PROPERTY = "spring.data.mongodb.uri";
    private static final String DATABASE = "spring.data.mongodb.database";
    private static final String REDIS_ADDRESS = "redis.uri";
    private static final String RABBITMQ_ADDRESS = "rabbitmq.uri";
    private static final String REDIS_DSN = "REDIS_DSN";
    private static final String RABBITMQ_DSN = "RABBITMQ_DSN";

    @Autowired
    private Environment environment;

    /**
     * Check mongodb status. Throws an exception if the service is not available
     */
    public void checkMongoDbStatus() {
	MongoClient mongoClient = null;
	try {
	    // mongo driver uses 30s as a default value
	    // set to 3s to raise an exception time quickly if having a connection issue
	    MongoClientOptions.Builder builder = MongoClientOptions.builder();
	    builder.serverSelectionTimeout(3000);

	    mongoClient = new MongoClient(new MongoClientURI( environment.getProperty( MONGODB_URI_PROPERTY ) , builder));
	    MongoDatabase db = mongoClient.getDatabase( environment.getProperty( DATABASE ) );

	    // will throw MongoTimeoutException if fails
	    db.runCommand(new Document().append("ping", 1));
	} finally {
            if (mongoClient != null) {
		mongoClient.close();
	    }
	}
    }

    /**
     * Check redis status
     */
    public void checkRedisStatus() {
        // get redis address
	String address = System.getenv( REDIS_DSN );
	if (address == null || address.isEmpty()) {
	    address = environment.getProperty( REDIS_ADDRESS );
	}

	// setup client - throws an exception if redis is not available
	Config config = new Config();
	config.useSingleServer().setAddress( address );
	RedissonClient redisClient = Redisson.create(config);

	// shutdown
	redisClient.shutdown();
    }

    /**
     * Check rabbit MQ status
     * @throws Exception
     */
    public void checkRabbitMqStatus() throws Exception {
        // rabbit connection
	Connection connection = null;
        
        // get rabbitMQ address
	String address = System.getenv( RABBITMQ_DSN );
	if (address == null || address.isEmpty()) {
	    address = environment.getProperty( RABBITMQ_ADDRESS );
	}

	// setup connection - throws an exception if redis is not available
	ConnectionFactory factory = new ConnectionFactory();
	try {
	    factory.setUri( address );
	    connection = factory.newConnection();
	} catch ( URISyntaxException | NoSuchAlgorithmException | KeyManagementException | TimeoutException | IOException exception ) {
	    throw new Exception( exception.getMessage() );
	} finally {
	    // close the connection
	    connection.close();
	}
    }

}
