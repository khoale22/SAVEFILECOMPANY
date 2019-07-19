/*
 * NutritionUpdatesHelper
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * NutritionUpdatesHelper is used to prepare candidate requests for nutrition updates(rejects).
 *
 * @author vn40486
 * @since 2.13.0
 */
public class NutritionUpdatesHelper {

    /**
     * Utility function to update candidate work request with the reject status and a related status record which will
     * carry the candidate reject reason details.
     * @param candidateWorkRequests work request to be updated(rejected).
     * @param rejectReason reason of nutrition update rejection.
     * @param userId user who submitted the reject request.
     * @return Reject status updated list of candidate work request.
     */
    public static List<CandidateWorkRequest> setCandidateAsRejectedWithComments(
            List<CandidateWorkRequest> candidateWorkRequests, String rejectReason, String userId) {
        if (candidateWorkRequests != null) {
            if (candidateWorkRequests != null) {
                candidateWorkRequests.forEach(candidateWorkRequest -> {
                    //Set status of all related work request to status = REJECTED
                    candidateWorkRequest.setStatus(CandidateStatus.REQUEST_STATUS_REJECTED);
                    //Add a status change reason record.
                    candidateWorkRequest.setCandidateStatuses(
                            generateCandidateStatus(candidateWorkRequest.getWorkRequestId(), rejectReason, userId));
                });
            }
        }
        return candidateWorkRequests;
    }

    /**
     * Generates and returns a candidate status record matching the inputs.
     * @param workId work id.
     * @param rejectReason reason of nutrition update rejection.
     * @param userId user who submitted the reject request.
     * @return a new candidate status for the input work id.
     */
    public static List<CandidateStatus> generateCandidateStatus(Long workId, String rejectReason, String userId) {
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey key = new CandidateStatusKey();
        key.setWorkRequestId(workId);
        key.setStatus(CandidateStatus.REQUEST_STATUS_REJECTED);
        key.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(key);
        candidateStatus.setStatusChangeReason(CandidateStatus.CandidateStatusChangeReason.REJECTED.getName());
        candidateStatus.setCommentText(rejectReason);
        List<CandidateStatus> candidateStatusList = new ArrayList<>();
        candidateStatusList.add(candidateStatus);
        return candidateStatusList;
    }
}
