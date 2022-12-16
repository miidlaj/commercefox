package com.ecommerce.common;

public class Constants {

    public static final String S3_BASE_URI;

    static {
        String bucketName =  "commercefox"; //System.getProperty("AWS_BUCKET_NAME");
        System.out.println("bucket Name: " + bucketName);
        String region = "ap-south-1";//System.getProperty("AWS_REGION");
        System.out.println("Region : " + region);
        String pattern = "https://%s.s3.%s.amazonaws.com";
        S3_BASE_URI = bucketName == null ? "" : String.format(pattern, bucketName, region);;
        System.out.println("Base URI is: " + S3_BASE_URI);
    }

}
