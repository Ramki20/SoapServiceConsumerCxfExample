package com.example.soap.controller;

import com.example.soap.client.generated.OfficeType;
import com.example.soap.client.generated.UserIdentity;
import com.example.soap.service.AuthorizationSoapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    @Autowired
    private AuthorizationSoapService authorizationSoapService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean isHealthy = authorizationSoapService.isHealthy();
        return ResponseEntity.ok(Map.of(
            "healthy", isHealthy,
            "service", "AuthorizationSharedService"
        ));
    }

    /**
     * Find offices by eAuth ID
     */
    @PostMapping("/offices/by-eauth-id")
    public ResponseEntity<List<String>> findOfficesByEauthId(@RequestBody FindOfficesRequest request) {
        List<String> offices = authorizationSoapService.findOfficesByEauthId(
            request.getUsdaEauthId(),
            request.getOfficeTypes(),
            request.getApplicationIdentifier(),
            request.getRequestHost()
        );
        return ResponseEntity.ok(offices);
    }

    /**
     * Find matching user identity
     */
    @PostMapping("/user-identity/find")
    public ResponseEntity<UserIdentity> findMatchingUserIdentity(@RequestBody Map<String, String> searchCriteria) {
        UserIdentity userIdentity = authorizationSoapService.findMatchingUserIdentity(searchCriteria);
        return ResponseEntity.ok(userIdentity);
    }

    /**
     * Get user roles
     */
    @PostMapping("/user-roles")
    public ResponseEntity<List<String>> getUserRoles(@RequestBody UserIdentity userIdentity) {
        List<String> roles = authorizationSoapService.getUserRoles(userIdentity);
        return ResponseEntity.ok(roles);
    }

    /**
     * Find users by criteria
     */
    @PostMapping("/users/by-criteria")
    public ResponseEntity<List<String>> findUsersByCriteria(@RequestBody FindUsersRequest request) {
        List<String> users = authorizationSoapService.findUsersByCriteria(
            request.getOfficeId(),
            request.getRoleName(),
            request.getApplicationIdentifier(),
            request.getRequestHost()
        );
        return ResponseEntity.ok(users);
    }

    // Request DTOs
    public static class FindOfficesRequest {
        private String usdaEauthId;
        private List<OfficeType> officeTypes;
        private String applicationIdentifier;
        private String requestHost;

        // Getters and setters
        public String getUsdaEauthId() { return usdaEauthId; }
        public void setUsdaEauthId(String usdaEauthId) { this.usdaEauthId = usdaEauthId; }

        public List<OfficeType> getOfficeTypes() { return officeTypes; }
        public void setOfficeTypes(List<OfficeType> officeTypes) { this.officeTypes = officeTypes; }

        public String getApplicationIdentifier() { return applicationIdentifier; }
        public void setApplicationIdentifier(String applicationIdentifier) { this.applicationIdentifier = applicationIdentifier; }

        public String getRequestHost() { return requestHost; }
        public void setRequestHost(String requestHost) { this.requestHost = requestHost; }
    }

    public static class FindUsersRequest {
        private String officeId;
        private String roleName;
        private String applicationIdentifier;
        private String requestHost;

        // Getters and setters
        public String getOfficeId() { return officeId; }
        public void setOfficeId(String officeId) { this.officeId = officeId; }

        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }

        public String getApplicationIdentifier() { return applicationIdentifier; }
        public void setApplicationIdentifier(String applicationIdentifier) { this.applicationIdentifier = applicationIdentifier; }

        public String getRequestHost() { return requestHost; }
        public void setRequestHost(String requestHost) { this.requestHost = requestHost; }
    }
}