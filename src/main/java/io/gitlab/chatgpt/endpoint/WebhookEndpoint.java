package io.gitlab.chatgpt.endpoint;

import io.gitlab.chatgpt.service.WebHookService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.webhook.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description WebhookEndpoint
 * @Date 12/15/2023 2:40 PM
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("webhook")
public class WebhookEndpoint {
    private final WebHookService webHookService;

    public WebhookEndpoint(WebHookService webHookService) {
        this.webHookService = webHookService;
    }

    @PostMapping(path = {"", "/issue", "/merge_request", "/push"},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String processEvent(HttpServletRequest request) {
        try {
            Event event = webHookService.handleRequest(request);
            return (event != null ? String.format("Processed '%s' event", event.getObjectKind()) : "");
        } catch (GitLabApiException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
        }
    }
}
