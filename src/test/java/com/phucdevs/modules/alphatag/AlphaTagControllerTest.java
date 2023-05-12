/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.alphatag;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AlphaTagControllerTest extends SupportToolBaseTest {

    public static final String ALPHA_TAG_URL = Constant.V2_URI + Constant.BASE_URL + "/alpha-tag";
    public static final String ALPHA_TAG_WITH_ID_URL = ALPHA_TAG_URL + "/" + ALPHA_TAG_ID;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FeatureSetServiceClient featureSetServiceClient;
    @MockBean
    private HubClient hubClient;

    @MockBean
    private AmsClient amsClient;

    @Rule
    public SecurityContextTestRule securityContextTestRule = new SecurityContextTestRule();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        securityContextTestRule.setContext(new VendorAccountId(VENDOR_ID, ACCOUNT_ID));
    }

    @Test
    public void testVerifyAlphaTag() throws Exception {
        // Given
        var featureSet = new FeatureSet();
        featureSet.setFeatures(new HashSet<>(List.of(new String[]{SUPPORT_HUB_FEATURE, MESSAGING_SOURCE_ALPHA_UPDATE_STATUS})));
        when(featureSetServiceClient.getFeatureSet(any())).thenReturn(featureSet);

        when(hubClient.approveAlphaTag(any(), any())).thenReturn(new SenderApprovalResponse());

        // When
        mockMvc.perform(headers(patch(ALPHA_TAG_WITH_ID_URL))
                        .content(pathToString("/data/alpha-tag-verify-request.json")))
                .andExpect(status().isOk());
        // Then
        verify(amsClient).createAccountProviderWeight(any(), any(), any());
    }

    @Test
    public void testVerifyAlphaTagWithNotFoundException() throws Exception {
        // Given
        var featureSet = new FeatureSet();
        featureSet.setFeatures(new HashSet<>(List.of(
                new String[]{SUPPORT_HUB_FEATURE, MESSAGING_SOURCE_ALPHA_UPDATE_STATUS})));
        when(featureSetServiceClient.getFeatureSet(any())).thenReturn(featureSet);

        when(hubClient.approveAlphaTag(any(), any())).thenThrow(SupportToolNotFoundException.class);
        // When
        mockMvc.perform(headers(patch(ALPHA_TAG_WITH_ID_URL))
                        .content(pathToString("/data/alpha-tag-verify-request.json")))
                .andExpect(status().isNotFound());
        // Then
    }


    @Test
    public void testVerifyAlphaTagWithInvalidModelRequest() throws Exception {

        // Given
        // When
        mockMvc.perform(headers(patch(ALPHA_TAG_WITH_ID_URL))
                        .content(pathToString("/data/alpha-tag-verify-invalid-request.json")))
                .andExpect(status().isBadRequest());
        // Then
    }

    @Test
    public void testVerifyAlphaTagInvalidHubFeatureRequest() throws Exception {
        // when
        mockMvc.perform(headers(patch(ALPHA_TAG_WITH_ID_URL))
                        .content(pathToString("/data/alpha-tag-verify-request.json")))
                .andExpect(status().isForbidden());
    }

    private MockHttpServletRequestBuilder headers(MockHttpServletRequestBuilder builder) {
        return builder
                .header("Content-Type", APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .header("Authenticated-Account-Id", AUTHENTICATED_ACCOUNT_ID)
                .header("Hub-User-Id", UUID.randomUUID())
                .header("user-feature-set-id", FEATURE_SET_ID);
    }
}

