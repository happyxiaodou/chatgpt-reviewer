package io.gitlab.chatgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.webhook.MergeRequestEvent;
import org.gitlab4j.api.webhook.MergeRequestEvent.ObjectAttributes;
import org.gitlab4j.api.webhook.WebHookListener;
import org.gitlab4j.api.webhook.WebHookManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description WebHookService
 * @Date 12/15/2023 2:46 PM
 * @Version 1.0
 */
@Slf4j
@Service
public class WebHookService extends WebHookManager implements WebHookListener {

    private final GitLabApi gitLabApi;

    public WebHookService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
        addListener(this);
    }

    @Override
    public void onMergeRequestEvent(MergeRequestEvent mergeRequestEvent) {

        ObjectAttributes attributes = mergeRequestEvent.getObjectAttributes();
        String branchName = attributes.getSourceBranch();
        Long userId = attributes.getAuthorId();
        Long projectId = attributes.getTargetProjectId();
        Long mergeRequestId = attributes.getIid();
        String mergeState = attributes.getState();
        String mergeStatus = attributes.getMergeStatus();

        log.info("Merge request notification received, userId={}, projectId={}, mergeRequestId={}, mergeStatus={}, mergeState={}",
                userId, projectId, mergeRequestId, mergeStatus, mergeState);

        // We only operate on merged or closed state changes
        if (!"opened".equals(mergeState)) {
            return;
        }
        // Make sure the merge request is valid
        MergeRequest mergeRequest = null;
        try {
            mergeRequest = gitLabApi.getMergeRequestApi().getMergeRequest(projectId, mergeRequestId);
            List<Diff> diffList = gitLabApi.getMergeRequestApi().getDiffs(projectId, mergeRequestId);
            System.out.println(diffList.size());
        } catch (GitLabApiException glae) {
            log.error("Problem getting merge request info, httpStatus={}, error={}", glae.getHttpStatus(), glae.getMessage(), glae);
            return;
        }


        log.info("Updated push record, userId={}, projectId={}, mergeRequestId={}, mergeStatus={}, mergeState={}",
                userId, projectId, mergeRequestId, mergeStatus, mergeState);
    }


}
