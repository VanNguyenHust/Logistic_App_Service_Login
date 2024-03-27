package com.example.Logistic_Web_App_Service_Login.common.components;
import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Độ dài của secret key (tùy chọn)
        int keyLength = 256; // Ví dụ: 256 bits

        // Tạo một mảng byte để lưu trữ secret key
        byte[] keyBytes = new byte[keyLength / 8];

        // Sử dụng SecureRandom để tạo ngẫu nhiên các byte cho secret key
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // Chuyển đổi mảng byte thành chuỗi Base64 để dễ dàng lưu trữ và truyền
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);

        // In ra secret key
        System.out.println("Secret Key: " + secretKey);
    }
}
