/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.usagethreshold;

import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsageTrackingV2ControllerTest extends SupportToolBaseTest {

    public static final String USAGE_TRACKING_URL = Constant.V2_URI + Constant.BASE_URL + "/usage-tracking";

    @MockBean
    private UsageTrackingService usageTrackingService;
    @Captor
    private ArgumentCaptor<UsageTrackingRequest> usageTrackingRequestArgumentCaptor;
    @MockBean
    private FeatureSetServiceClient featureSetServiceClient;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Rule
    public SecurityContextTestRule securityContextTestRule = new SecurityContextTestRule();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        securityContextTestRule.setContext(new VendorAccountId(VENDOR_ID, ACCOUNT_ID));
    }

    @Test
    public void shouldCreateUsageTrackingWithCreatedStatus() throws Exception {
        // Given
        var checkContainsResponse = HttpStatus.CREATED;
        when(usageTrackingService.checkContainsUsageThresholdByLabelHubSupport(
               any(UsageTrackingRequest.class)
        )).thenReturn(checkContainsResponse);

        var featureSet = new FeatureSet();
        featureSet.setFeatures(new HashSet<>(List.of(new String[]{SUPPORT_HUB_FEATURE})));
        when(featureSetServiceClient.getFeatureSet(any())).thenReturn(featureSet);


        var creationUsageTrackingResponse = new UsageThreshold(
                USAGE_TRACKING_URL,
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                "created_by_support_tool_service",
                GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD,
                DateTimeZone.forID("Australia/Melbourne"),
                GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED,
                1L,
                0L,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY
        );
        when(usageTrackingService.add(
                any(UsageTrackingRequest.class))
        ).thenReturn(creationUsageTrackingResponse);

        // When
        mockMvc.perform(headers(post(USAGE_TRACKING_URL))
                    .content(pathToString("/data/usage-threshold-creation-request.json")))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(pathToString("/data/usage-threshold-creation-result-response.json")));

        // Then
        verify(usageTrackingService).checkContainsUsageThresholdByLabelHubSupport(usageTrackingRequestArgumentCaptor.capture());
        verify(usageTrackingService).add(usageTrackingRequestArgumentCaptor.capture());
        UsageTrackingRequest usageTrackingRequest = usageTrackingRequestArgumentCaptor.getValue();
        assertEquals("MessageMedia", usageTrackingRequest.getVendorId());
        assertEquals("ChungCookie_FGX_0001", usageTrackingRequest.getAccountId());
        assertEquals(GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY, usageTrackingRequest.getPeriod());
        assertEquals(1L, usageTrackingRequest.getThreshold().longValue());
        assertEquals(TestData.AUTHENTICATED_ACCOUNT_ID, usageTrackingRequest.getAuthenticatedAccountId());
    }

    @Test
    public void testCreateUsageTrackingWithConflictStatus() throws Exception {

        // Given
        var checkContainsResponse = HttpStatus.CONFLICT;
        when(usageTrackingService.checkContainsUsageThresholdByLabelHubSupport(
                any(UsageTrackingRequest.class)
        )).thenReturn(checkContainsResponse);

        var featureSet = new FeatureSet();
        featureSet.setFeatures(new HashSet<>(List.of(new String[]{SUPPORT_HUB_FEATURE})));
        when(featureSetServiceClient.getFeatureSet(any())).thenReturn(featureSet);

        // When
        mockMvc.perform(headers(post(USAGE_TRACKING_URL))
                        .content(pathToString("/data/usage-threshold-creation-request.json")))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(pathToString("/data/usage-threshold-creation-conflict-response.json")));

        // Then
        verify(usageTrackingService).checkContainsUsageThresholdByLabelHubSupport(usageTrackingRequestArgumentCaptor.capture());
        UsageTrackingRequest usageTrackingRequest = usageTrackingRequestArgumentCaptor.getValue();
        assertEquals("MessageMedia", usageTrackingRequest.getVendorId());
        assertEquals("ChungCookie_FGX_0001", usageTrackingRequest.getAccountId());
        assertEquals(GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY, usageTrackingRequest.getPeriod());
        assertEquals(1L, usageTrackingRequest.getThreshold().longValue());
    }

    @Test
    public void testCreateUsageTrackingWithInValidModelRequest() throws Exception {

        // Given
        // When
        mockMvc.perform(headers(post(USAGE_TRACKING_URL))
                        .content(pathToString("/data/usage-threshold-creation-invalid-request.json")))
                .andExpect(status().isBadRequest());
        // Then
    }

    @Test
    public void createUsageTrackingWithInvalidHubFeatureRequest() throws Exception {
        // when
        mockMvc.perform(headers(post(USAGE_TRACKING_URL))
                        .content(pathToString("/data/usage-threshold-creation-request.json")))
                .andExpect(status().isForbidden());
    }

    private MockHttpServletRequestBuilder headers(MockHttpServletRequestBuilder builder) {
        return builder
                .header("Content-Type", APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .header("Authenticated-Account-Id", AUTHENTICATED_ACCOUNT_ID)
                .header("user-feature-set-id", FEATURE_SET_ID);
    }
}

