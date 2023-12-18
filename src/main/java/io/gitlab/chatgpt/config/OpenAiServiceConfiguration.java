package io.gitlab.chatgpt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;

import java.net.Proxy;
import java.time.Duration;
import java.util.concurrent.ExecutorService;



@Slf4j
@Configuration
@Data
@Primary
@ConfigurationProperties("openai")
@EnableConfigurationProperties
public class OpenAiServiceConfiguration {

    private String baseUrl;
    private String token;
    private Proxy proxy;
    private String level = "BASIC";

    @Bean
    public OpenAiService openAiService() {
        return builder(baseUrl, token, getProxy());
    }


    public OpenAiService builder(String baseUrl, String token, Proxy proxy) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient okHttpClient = defaultClient(token, Duration.ofSeconds(10), proxy);
        Retrofit retrofit = defaultRetrofit(baseUrl, okHttpClient, mapper);
        ExecutorService executorService = okHttpClient.dispatcher().executorService();
        return new OpenAiService(retrofit.create(OpenAiApi.class), executorService);
    }

    public ObjectMapper defaultObjectMapper() {
        return OpenAiService.defaultObjectMapper();
    }

    public OkHttpClient defaultClient(String token, Duration timeout, Proxy proxy) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.valueOf(getLevel()));
        return OpenAiService.defaultClient(token, timeout).newBuilder()
                .proxy(proxy).addInterceptor(loggingInterceptor).build();
    }

    public Retrofit defaultRetrofit(String url, OkHttpClient client, ObjectMapper mapper) {
        return OpenAiService.defaultRetrofit(client, mapper)
                .newBuilder().baseUrl(url).build();
    }
}
