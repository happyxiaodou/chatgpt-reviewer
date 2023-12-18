package io.gitlab.chatgpt.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@Data
@Primary
@ConfigurationProperties("gitlab")
@EnableConfigurationProperties
public class GitLabClientConfiguration {

    private String hostUrl;
    private String accessToken;
    private String secretToken;


    @Bean
    public GitLabApi gitLabApi() {
        return new GitLabApi(getHostUrl(), getAccessToken(), getSecretToken());
    }
}
