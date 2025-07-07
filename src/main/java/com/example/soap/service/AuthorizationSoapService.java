package com.example.soap.service;

import com.example.soap.client.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthorizationSoapService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationSoapService.class);

    @Autowired
    private AuthorizationSharedService authorizationSharedService;

    /**
     * Check if the service is healthy
     * @return true if service is healthy
     */
    public boolean isHealthy() {
        try {
            logger.info("Calling isHealthy service...");
            return authorizationSharedService.isHealthy();
        } catch (Exception e) {
            logger.error("Error calling isHealthy service", e);
            throw new RuntimeException("Failed to check service health", e);
        }
    }

    /**
     * Find offices by eAuth ID
     * @param usdaEauthId The USDA eAuth ID
     * @param officeTypes List of office types to search for
     * @param applicationIdentifier Application identifier for request token
     * @param requestHost Request host for request token
     * @return List of office IDs
     */
    public List<String> findOfficesByEauthId(String usdaEauthId, 
                                           List<OfficeType> officeTypes,
                                           String applicationIdentifier,
                                           String requestHost) {
        try {
            logger.info("Calling findOfficesByEauthId for eAuthId: {}", usdaEauthId);
            
            // Create request token
            RequestToken requestToken = new RequestToken();
            requestToken.setApplicationIdentifier(applicationIdentifier);
            requestToken.setRequestHost(requestHost);
            
            // Create request
            FindOfficesByEauthIdRequest request = new FindOfficesByEauthIdRequest();
            request.setUsdaEauthId(usdaEauthId);
            request.getOfficeType().addAll(officeTypes);
            request.setRequestToken(requestToken);
            
            // Call service and cast response
            Object responseObj = authorizationSharedService.findOfficesByEauthId(request);
            FindOfficesByEauthIdResponse response = (FindOfficesByEauthIdResponse) responseObj;
            
            List<String> offices = response.getOffices().getListValue();
            logger.info("Found {} offices for eAuthId: {}", offices.size(), usdaEauthId);
            
            return offices;
            
        } catch (Exception e) {
            // Check if it's a validation exception
            if (e.getClass().getSimpleName().contains("AuthorizationServiceValidationException")) {
                logger.error("Validation error in findOfficesByEauthId", e);
                throw new RuntimeException("Validation error: " + e.getMessage(), e);
            } else {
                logger.error("Error calling findOfficesByEauthId service", e);
                throw new RuntimeException("Failed to find offices by eAuth ID", e);
            }
        }
    }

    /**
     * Find matching user identity
     * @param searchCriteria Map of search criteria (key-value pairs)
     * @return UserIdentity object
     */
    public UserIdentity findMatchingUserIdentity(Map<String, String> searchCriteria) {
        try {
            logger.info("Calling findMatchingUserIdentity with criteria: {}", searchCriteria);
            
            // Create request
            FindMatchingUserIdentityRequest request = new FindMatchingUserIdentityRequest();
            
            // Add map entries
            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                MapEntry mapEntry = new MapEntry();
                mapEntry.setKey(entry.getKey());
                mapEntry.setValue(entry.getValue());
                request.getMapEntry().add(mapEntry);
            }
            
            // Call service and cast response
            Object responseObj = authorizationSharedService.findMatchingUserIdentity(request);
            FindMatchingUserIdentityResponse response = (FindMatchingUserIdentityResponse) responseObj;
            
            UserIdentity userIdentity = response.getUserIdentity();
            logger.info("Found user identity: {}", userIdentity.getUserLoginName());
            
            return userIdentity;
            
        } catch (Exception e) {
            // Check if it's a validation exception
            if (e.getClass().getSimpleName().contains("AuthorizationServiceValidationException")) {
                logger.error("Validation error in findMatchingUserIdentity", e);
                throw new RuntimeException("Validation error: " + e.getMessage(), e);
            } else {
                logger.error("Error calling findMatchingUserIdentity service", e);
                throw new RuntimeException("Failed to find matching user identity", e);
            }
        }
    }

    /**
     * Get user roles for a user identity
     * @param userIdentity The user identity object
     * @return List of user roles
     */
    public List<String> getUserRoles(UserIdentity userIdentity) {
        try {
            logger.info("Calling getUserRoles for user: {}", userIdentity.getUserLoginName());
            
            // Create request
            GetUserRolesRequest request = new GetUserRolesRequest();
            request.setUserIdentity(userIdentity);
            
            // Call service and cast response
            Object responseObj = authorizationSharedService.getUserRoles(request);
            GetUserRolesResponse response = (GetUserRolesResponse) responseObj;
            
            List<String> roles = response.getUserRoles().getListValue();
            logger.info("Found {} roles for user: {}", roles.size(), userIdentity.getUserLoginName());
            
            return roles;
            
        } catch (Exception e) {
            // Check if it's a validation exception
            if (e.getClass().getSimpleName().contains("AuthorizationServiceValidationException")) {
                logger.error("Validation error in getUserRoles", e);
                throw new RuntimeException("Validation error: " + e.getMessage(), e);
            } else {
                logger.error("Error calling getUserRoles service", e);
                throw new RuntimeException("Failed to get user roles", e);
            }
        }
    }

    /**
     * Find users by criteria
     * @param officeId Office ID to search in
     * @param roleName Role name to search for
     * @param applicationIdentifier Application identifier for request token
     * @param requestHost Request host for request token
     * @return List of users
     */
    public List<String> findUsersByCriteria(String officeId, 
                                          String roleName,
                                          String applicationIdentifier,
                                          String requestHost) {
        try {
            logger.info("Calling findUsersByCriteria for office: {}, role: {}", officeId, roleName);
            
            // Create request token
            RequestToken requestToken = new RequestToken();
            requestToken.setApplicationIdentifier(applicationIdentifier);
            requestToken.setRequestHost(requestHost);
            
            // Create request
            FindUsersByCriteriaRequest request = new FindUsersByCriteriaRequest();
            request.setOfficeId(officeId);
            request.setRoleName(roleName);
            request.setRequestToken(requestToken);
            
            // Call service and cast response
            Object responseObj = authorizationSharedService.findUsersByCriteria(request);
            FindUserCriteriaResponse response = (FindUserCriteriaResponse) responseObj;
            
            List<String> users = response.getUsers().getListValue();
            logger.info("Found {} users for office: {}, role: {}", users.size(), officeId, roleName);
            
            return users;
            
        } catch (Exception e) {
            // Check if it's a validation exception
            if (e.getClass().getSimpleName().contains("AuthorizationServiceValidationException")) {
                logger.error("Validation error in findUsersByCriteria", e);
                throw new RuntimeException("Validation error: " + e.getMessage(), e);
            } else {
                logger.error("Error calling findUsersByCriteria service", e);
                throw new RuntimeException("Failed to find users by criteria", e);
            }
        }
    }
}