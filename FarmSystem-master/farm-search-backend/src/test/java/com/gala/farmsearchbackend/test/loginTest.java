package com.gala.farmsearchbackend.test;

import com.gala.farmsearchbackend.utils.PasswordEncoderUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
@SpringBootTest
public class loginTest {
    @Test
    public void encodeTest(){
        PasswordEncoderUtil encoder = new PasswordEncoderUtil();
        String rawPassword = "123456";
        String encodedPassword1 = encoder.encodePassword(rawPassword);
        String encodedPassword2 = encoder.encodePassword(rawPassword);
        String encodedPassword3 = encoder.encodePassword(rawPassword);
        String encodedPassword4 = encoder.encodePassword(rawPassword);
        String encodedPassword5 = encoder.encodePassword(rawPassword);

        System.out.println("加密后: " + encodedPassword1);
        System.out.println("验证结果: " + encoder.matches("123456", encodedPassword1)); // 返回 true

        System.out.println("加密后: " + encodedPassword2);
        System.out.println("验证结果: " + encoder.matches("123456", encodedPassword2)); // 返回 true

        System.out.println("加密后: " + encodedPassword3);
        System.out.println("验证结果: " + encoder.matches("123456", encodedPassword3)); // 返回 true

        System.out.println("加密后: " + encodedPassword4);
        System.out.println("验证结果: " + encoder.matches("123456", encodedPassword4)); // 返回 true

        System.out.println("加密后: " + encodedPassword5);
        System.out.println("验证结果: " + encoder.matches("123456", encodedPassword5)); // 返回 true

    }
}
