package com.ecommerce.common;

public class Constants {

    public static final String S3_BASE_URI;

    static {
        String bucketName = System.getProperty("AWS_BUCKET_NAME"); //"commercefox";
        System.out.println("bucket Name: " + bucketName);
        String region = System.getProperty("AWS_REGION"); //"ap-south-1";
        System.out.println("Region : " + region);
        String pattern = "https://%s.s3.%s.amazonaws.com";
        S3_BASE_URI = bucketName == null ? "" : String.format(pattern, bucketName, region);;
        System.out.println("Base URI is: " + S3_BASE_URI);
    }

}
