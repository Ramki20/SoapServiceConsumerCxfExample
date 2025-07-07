package com.example.soap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

/**
 * Simple test to verify what classes were actually generated
 */
@SpringBootTest
public class GeneratedClassesTest {

    @Test
    public void listGeneratedClasses() {
        System.out.println("=== Generated Classes Verification ===");
        
        // List of classes we expect to find
        String[] expectedClasses = {
            "AuthorizationSharedService",
            "UserIdentity", 
            "MapEntry",
            "OfficeType",
            "RequestToken",
            "ListType"
        };
        
        // Check for request/response classes with different possible names
        String[] requestResponseClasses = {
            "FindOfficesByEauthIdRequest",
            "FindOfficesByEauthIdRequestType", 
            "FindOfficesByEauthIdResponse",
            "FindOfficesByEauthIdResponseType",
            "FindMatchingUserIdentityRequest",
            "FindMatchingUserIdentityRequestType",
            "FindMatchingUserIdentityResponse", 
            "FindMatchingUserIdentityResponseType",
            "GetUserRolesRequest",
            "GetUserRolesRequestType",
            "GetUserRolesResponse",
            "GetUserRolesResponseType",
            "FindUsersByCriteriaRequest",
            "FindUsersByCriteriaRequestType",
            "FindUserCriteriaResponse",
            "FindUserCriteriaResponseType"
        };
        
        String packageName = "com.example.soap.client.generated";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        System.out.println("Checking basic classes:");
        for (String className : expectedClasses) {
            checkClass(classLoader, packageName + "." + className);
        }
        
        System.out.println("\nChecking request/response classes:");
        for (String className : requestResponseClasses) {
            checkClass(classLoader, packageName + "." + className);
        }
        
        // Check for exception classes
        System.out.println("\nChecking exception classes:");
        String[] exceptionClasses = {
            "AuthorizationServiceValidationException",
            "AuthorizationServiceValidationException_Exception"
        };
        
        for (String className : exceptionClasses) {
            checkClass(classLoader, packageName + "." + className);
        }
        
        // Print service methods if found
        try {
            Class<?> serviceClass = classLoader.loadClass(packageName + ".AuthorizationSharedService");
            System.out.println("\nService methods:");
            Method[] methods = serviceClass.getMethods();
            for (Method method : methods) {
                if (!method.getDeclaringClass().equals(Object.class)) {
                    System.out.println("  " + method.getName() + "(" + 
                        java.util.Arrays.toString(method.getParameterTypes()) + ") : " + 
                        method.getReturnType().getSimpleName());
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Could not analyze service methods");
        }
        
        System.out.println("\n=== End Generated Classes Verification ===");
    }
    
    private void checkClass(ClassLoader classLoader, String fullClassName) {
        try {
            Class<?> clazz = classLoader.loadClass(fullClassName);
            String simpleName = clazz.getSimpleName();
            System.out.println("✓ FOUND: " + simpleName + " (" + fullClassName + ")");
        } catch (ClassNotFoundException e) {
            String simpleName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
            System.out.println("✗ MISSING: " + simpleName + " (" + fullClassName + ")");
        }
    }
}