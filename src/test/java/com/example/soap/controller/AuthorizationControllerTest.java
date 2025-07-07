package com.example.soap.controller;

import com.example.soap.client.generated.OfficeType;
import com.example.soap.client.generated.UserIdentity;
import com.example.soap.service.AuthorizationSoapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorizationSoapService authorizationSoapService;

    @Test
    void testHealthCheck() throws Exception {
        when(authorizationSoapService.isHealthy()).thenReturn(true);

        mockMvc.perform(get("/api/authorization/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthy").value(true))
                .andExpect(jsonPath("$.service").value("AuthorizationSharedService"));
    }

    @Test
    void testFindOfficesByEauthId() throws Exception {
        List<String> mockOffices = Arrays.asList("47310", "47318", "47321");
        when(authorizationSoapService.findOfficesByEauthId(anyString(), anyList(), anyString(), anyString()))
                .thenReturn(mockOffices);

        AuthorizationController.FindOfficesRequest request = new AuthorizationController.FindOfficesRequest();
        request.setUsdaEauthId("28692023052412555531317");
        request.setOfficeTypes(Arrays.asList(OfficeType.FLP));
        request.setApplicationIdentifier("demo-app");
        request.setRequestHost("localhost");

        mockMvc.perform(post("/api/authorization/offices/by-eauth-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("47310"))
                .andExpect(jsonPath("$[1]").value("47318"))
                .andExpect(jsonPath("$[2]").value("47321"));
    }

    @Test
    void testFindMatchingUserIdentity() throws Exception {
        UserIdentity mockUserIdentity = new UserIdentity();
        mockUserIdentity.setAuthenticationSystemIdentifier("28200310169021026877");
        mockUserIdentity.setAuthorizationSystemIdentifier("-1452175789");
        mockUserIdentity.setUserLoginName("emp0007966");

        when(authorizationSoapService.findMatchingUserIdentity(anyMap()))
                .thenReturn(mockUserIdentity);

        Map<String, String> searchCriteria = Map.of("usda_eauth_id", "28200310169021026877");

        mockMvc.perform(post("/api/authorization/user-identity/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationSystemIdentifier").value("28200310169021026877"))
                .andExpect(jsonPath("$.authorizationSystemIdentifier").value("-1452175789"))
                .andExpect(jsonPath("$.userLoginName").value("emp0007966"));
    }

    @Test
    void testGetUserRoles() throws Exception {
        List<String> mockRoles = Arrays.asList(
                "app.fsa.flp.dls.fsfl",
                "app.fsa.flp.dls.lm",
                "app.fsa.flp.dls.lm.cm",
                "app.fsa.flp.dls.lm.llm",
                "app.fsa.flp.dls.ls"
        );

        when(authorizationSoapService.getUserRoles(any(UserIdentity.class)))
                .thenReturn(mockRoles);

        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setAuthenticationSystemIdentifier("28200310169021026877");
        userIdentity.setAuthorizationSystemIdentifier("-1452175789");
        userIdentity.setUserLoginName("emp0007966");

        mockMvc.perform(post("/api/authorization/user-roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIdentity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("app.fsa.flp.dls.fsfl"))
                .andExpect(jsonPath("$[4]").value("app.fsa.flp.dls.ls"));
    }

    @Test
    void testFindUsersByCriteria() throws Exception {
        List<String> mockUsers = Arrays.asList("user1", "user2", "user3");
        when(authorizationSoapService.findUsersByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockUsers);

        AuthorizationController.FindUsersRequest request = new AuthorizationController.FindUsersRequest();
        request.setOfficeId("47310");
        request.setRoleName("app.fsa.flp.dls.lm");
        request.setApplicationIdentifier("demo-app");
        request.setRequestHost("localhost");

        mockMvc.perform(post("/api/authorization/users/by-criteria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("user1"))
                .andExpect(jsonPath("$[1]").value("user2"))
                .andExpect(jsonPath("$[2]").value("user3"));
    }
}