package io.gitlab.chatgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Date 12/15/2023 3:18 PM
 * @Version 1.0
 */
@SpringBootApplication
public class MRCodereviewApplication {
    public static void main(String[] args) {
        // Set up the embedded Tomcat to allow for encoded slashes in the URLs
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
        SpringApplication.run(MRCodereviewApplication.class, args);
    }
}
