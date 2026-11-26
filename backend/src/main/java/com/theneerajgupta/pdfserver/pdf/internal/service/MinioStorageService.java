package com.theneerajgupta.pdfserver.pdf.internal.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioStorageService {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageService.class);

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioStorageService(MinioClient minioClient,
                               @Value("${minio.bucket-name}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public void uploadFile(String key, byte[] content, String contentType) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(key)
                        .stream(bais, content.length, -1)
                        .contentType(contentType)
                        .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to upload object {} to MinIO bucket {}: {}", key, bucketName, e.getMessage(), e);
            throw new RuntimeException("MinIO upload failure: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFile(String key) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .build()
            );
        } catch (Exception e) {
            log.error("Failed to download object {} from MinIO: {}", key, e.getMessage(), e);
            throw new RuntimeException("MinIO download failure: " + e.getMessage(), e);
        }
    }

    public String getPresignedDownloadUrl(String key, int expiryMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(key)
                    .expiry(expiryMinutes, TimeUnit.MINUTES)
                    .build()
            );
        } catch (Exception e) {
            log.warn("Could not generate pre-signed URL for {}: {}", key, e.getMessage());
            return null;
        }
    }

    public String getBucketName() {
        return bucketName;
    }
}
