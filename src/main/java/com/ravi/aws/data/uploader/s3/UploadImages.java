package com.ravi.aws.data.uploader.s3;

import com.amazonaws.services.s3.AmazonS3;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class UploadImages {

    @Value("${images.location}")
    private String imagesLocation;

    @Value("${myapp.image.s3.key}")
    private String s3Key;

    @Value("${myapp.s3.bucket.name}")
    private String s3BucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @PostConstruct
    public void uploadImages(){
        try(Stream<Path> paths = Files.walk(Paths.get(imagesLocation))){
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().contains(".DS_Store"))
                    .forEach(path ->{
                        String fileName = path.getFileName().toString();
                        String objectName = s3Key
                                .concat("/")
                                .concat(path.getParent().getFileName().toString())
                                .concat("/")
                                .concat(fileName);
                        if(!amazonS3.doesObjectExist(s3BucketName, objectName)) {
                            amazonS3.putObject(s3BucketName, objectName, path.toFile());
                            System.out.println("Image uploaded ::" + fileName);
                        }else{
                            System.out.println("Image already exists ::" + fileName);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
