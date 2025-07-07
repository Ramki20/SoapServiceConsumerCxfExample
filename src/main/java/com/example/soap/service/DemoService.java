package com.example.soap.service;

import com.example.soap.client.generated.OfficeType;
import com.example.soap.client.generated.UserIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DemoService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private AuthorizationSoapService authorizationSoapService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting SOAP Service Demo...");
        
        try {
            // Demo 1: Health Check
            demonstrateHealthCheck();
            
            // Demo 2: Find Offices by eAuth ID
            demonstrateFindOfficesByEauthId();
            
            // Demo 3: Find Matching User Identity
            demonstrateFindMatchingUserIdentity();
            
            // Demo 4: Get User Roles
            demonstrateGetUserRoles();
            
        } catch (Exception e) {
            logger.error("Error during demo execution", e);
        }
        
        logger.info("SOAP Service Demo completed.");
    }

    private void demonstrateHealthCheck() {
        logger.info("=== Health Check Demo ===");
        try {
            boolean isHealthy = authorizationSoapService.isHealthy();
            logger.info("Service health status: {}", isHealthy);
        } catch (Exception e) {
            logger.error("Health check failed", e);
        }
    }

    private void demonstrateFindOfficesByEauthId() {
        logger.info("=== Find Offices by eAuth ID Demo ===");
        try {
            String eAuthId = "28692023052412555531317";
            List<OfficeType> officeTypes = Arrays.asList(OfficeType.FLP);
            String appId = "demo-app";
            String requestHost = "localhost";
            
            List<String> offices = authorizationSoapService.findOfficesByEauthId(
                eAuthId, officeTypes, appId, requestHost);
            
            logger.info("Found offices: {}", offices);
        } catch (Exception e) {
            logger.error("Find offices by eAuth ID failed", e);
        }
    }

    private void demonstrateFindMatchingUserIdentity() {
        logger.info("=== Find Matching User Identity Demo ===");
        try {
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("usda_eauth_id", "28200310169021026877");
            
            UserIdentity userIdentity = authorizationSoapService.findMatchingUserIdentity(searchCriteria);
            
            logger.info("Found user identity:");
            logger.info("  Authentication System ID: {}", userIdentity.getAuthenticationSystemIdentifier());
            logger.info("  Authorization System ID: {}", userIdentity.getAuthorizationSystemIdentifier());
            logger.info("  User Login Name: {}", userIdentity.getUserLoginName());
            
        } catch (Exception e) {
            logger.error("Find matching user identity failed", e);
        }
    }

    private void demonstrateGetUserRoles() {
        logger.info("=== Get User Roles Demo ===");
        try {
            // First find a user identity
            Map<String, String> searchCriteria = new HashMap<>();
            searchCriteria.put("usda_eauth_id", "28200310169021026877");
            
            UserIdentity userIdentity = authorizationSoapService.findMatchingUserIdentity(searchCriteria);
            
            // Then get their roles
            List<String> roles = authorizationSoapService.getUserRoles(userIdentity);
            
            logger.info("User roles for {}: {}", userIdentity.getUserLoginName(), roles);
            
        } catch (Exception e) {
            logger.error("Get user roles failed", e);
        }
    }
}