package com.ravi.aws.data.uploader.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${access.key}")
    private String accessKey;

    @Value("${access.secret}")
    private String accessSecret;

    @Bean
    public AmazonS3 s3Client(){
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider())
                .build();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return new AWSStaticCredentialsProvider(credentials);
    }
}
