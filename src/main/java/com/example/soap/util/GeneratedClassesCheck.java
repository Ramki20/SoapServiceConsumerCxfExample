package com.example.soap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.jar.JarFile;

/**
 * Utility class to check and list generated SOAP client classes
 * This helps verify what classes were actually generated after WSDL processing
 */
@Component
@Order(1) // Run before DemoService
public class GeneratedClassesCheck implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GeneratedClassesCheck.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Checking Generated SOAP Client Classes ===");
        
        try {
            // Check what classes are available in the generated package
            String packageName = "com.example.soap.client.generated";
            
            // Get the current classloader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            // List some expected classes to verify they exist
            String[] expectedClasses = {
                "AuthorizationSharedService",
                "UserIdentity",
                "MapEntry",
                "OfficeType",
                "RequestToken",
                "ListType",
                "FindOfficesByEauthIdRequestType",
                "FindOfficesByEauthIdResponseType",
                "FindMatchingUserIdentityRequestType", 
                "FindMatchingUserIdentityResponseType",
                "GetUserRolesRequestType",
                "GetUserRolesResponseType",
                "FindUsersByCriteriaRequestType",
                "FindUserCriteriaResponseType",
                "AuthorizationServiceValidationException_Exception"
            };
            
            logger.info("Checking for generated classes in package: {}", packageName);
            
            for (String className : expectedClasses) {
                String fullClassName = packageName + "." + className;
                try {
                    Class<?> clazz = classLoader.loadClass(fullClassName);
                    logger.info("✓ Found class: {} (Package: {})", className, clazz.getPackage().getName());
                } catch (ClassNotFoundException e) {
                    logger.warn("✗ Missing class: {}", className);
                }
            }
            
            // Check for alternative naming patterns if the standard ones are missing
            checkAlternativeNaming(classLoader, packageName);
            
        } catch (Exception e) {
            logger.error("Error checking generated classes", e);
        }
        
        logger.info("=== Generated Classes Check Complete ===\n");
    }
    
    private void checkAlternativeNaming(ClassLoader classLoader, String packageName) {
        logger.info("Checking for alternative naming patterns...");
        
        // Common alternative patterns CXF might generate
        String[] alternativePatterns = {
            "FindOfficesByEauthIdRequest",
            "FindOfficesByEauthIdResponse", 
            "FindMatchingUserIdentityRequest",
            "FindMatchingUserIdentityResponse",
            "GetUserRolesRequest",
            "GetUserRolesResponse",
            "FindUsersByCriteriaRequest",
            "FindUsersByCriteriaResponse",
            "AuthorizationServiceValidationException"
        };
        
        for (String className : alternativePatterns) {
            String fullClassName = packageName + "." + className;
            try {
                Class<?> clazz = classLoader.loadClass(fullClassName);
                logger.info("✓ Found alternative class: {}", className);
            } catch (ClassNotFoundException e) {
                // This is expected for most patterns
            }
        }
    }
}