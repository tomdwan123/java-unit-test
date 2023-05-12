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
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AlphaTagServiceImplTest extends SupportToolBaseTest {

    @MockBean
    private HubClient hubClient;

    @MockBean
    private AmsClient amsClient;
    private AlphaTagServiceImpl alphaTagService;
    @Rule
    public SecurityContextTestRule securityContextTestRule = new SecurityContextTestRule();

    @BeforeEach
    public void setup() {
        alphaTagService = new AlphaTagServiceImpl(hubClient, amsClient);
        securityContextTestRule.setContext(new VendorAccountId(VENDOR_ID, ACCOUNT_ID));
    }

    @Test
    public void shouldApproveSuccessfully() {
        //Given
        var expected = new SenderApprovalResponse();
        when(hubClient.approveAlphaTag(any(), any())).thenReturn(expected);

        // When
        var actual = alphaTagService.
                update(TestData.getAlphaTagVerifyApprovedRequest());

        // Then
        verify(amsClient).createAccountProviderWeight(any(), any(), any());

        assertEquals(actual, expected);
    }

    @Test
    public void shouldRejectSuccessfully() {
        //Given
        var expected = new SenderApprovalResponse();
        when(hubClient.rejectAlphaTag(any(), any())).thenReturn(expected);

        // Then
        var actual = alphaTagService.
                update(TestData.getAlphaTagVerifyRejectedRequest());

        assertEquals(actual, expected);
    }

    @Test
    public void shouldThrowException() {
        //Given
        when(hubClient.rejectAlphaTag(any(), any())).thenThrow(RuntimeException.class);
        when(hubClient.approveAlphaTag(any(), any())).thenThrow(RuntimeException.class);

        // Then
        assertThrows(RuntimeException.class, () -> alphaTagService.
                update(TestData.getAlphaTagVerifyRejectedRequest()));
    }

}
