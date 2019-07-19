/*
 * UserService.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.user;

import com.google.common.collect.Lists;
import com.heb.pm.entity.User;
import com.heb.pm.repository.LdapSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serves user (partner) related queries to the Active Directory. (to be used for vendor ldap queries as well in future.)
 *
 * @author vn40486
 * @since  2.16.0
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String MESSAGE_LDAP_FETCH_USER_ERROR = "Error while fetch user info from LDAP : %s";
    private static final int LDAP_FETCH_SIZE = 20;

    @Autowired
    LdapSearchRepository ldapSearchRepository;

    /**
     * Used to get user info by id.
     * @param userId
     * @return
     */
    public User getUserById(final String userId) {
        User user = null;
        if (userId != null) {
            List<String> userIds = new ArrayList<>();
            userIds.add(userId);
            List<User> users = this.fetchUsers(userIds);
            if(users != null && !users.isEmpty()) {
                user = users.get(0);//Searching by one userId, expecting only one user object. Hence get(0) - first record.
            }
        }
        return user;
    }

    public List<User> getUserById(final List<String> userIds) {
        List<User> users = new ArrayList<>();
        if(userIds != null && !userIds.isEmpty()) {
            if(userIds.size() > LDAP_FETCH_SIZE) {
                users = fetchUsersByBatch(userIds, LDAP_FETCH_SIZE);
            } else {
                users = fetchUsers(userIds);
            }
        }
        return users;
    }

    private List<User> fetchUsers(final List<String> userIds) {
        List<User> users = new ArrayList<>();
        try {
           users = this.ldapSearchRepository.getUserList(userIds);
        } catch (Exception e) {
            logger.error(String.format(MESSAGE_LDAP_FETCH_USER_ERROR,e.getMessage()));
        }
        return users;
    }

    private List<User> fetchUsersByBatch(final List<String> userIds, final int fetchSize) {
        List<List<String>> subSetsOfUserIds = Lists.partition(userIds,fetchSize);

        List<User> users = new ArrayList<>();
        subSetsOfUserIds.stream().forEach(userIdSet -> {
            try {
                users.addAll(this.ldapSearchRepository.getUserList(userIdSet));
            } catch (Exception e) {
                logger.error(String.format(MESSAGE_LDAP_FETCH_USER_ERROR,e.getMessage()));
            }
        });
        /*for(List<String> userIdSet : subSetsOfUserIds) {
            try {
                users.addAll(this.ldapSearchRepository.getUserList(userIdSet));
            } catch (Exception e) {
                logger.error(String.format(MESSAGE_LDAP_FETCH_USER_ERROR,e.getMessage()));
            }
        }*/
        return users;
    }
}
