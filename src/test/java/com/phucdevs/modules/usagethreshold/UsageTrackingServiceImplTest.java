/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.usagethreshold;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsageTrackingServiceImplTest extends SupportToolBaseTest {

    @Mock
    private RestApiClient restApiClient;

    private UsageTrackingServiceImpl usageTrackingService;

    @BeforeEach
    public void setup() {
        usageTrackingService = new UsageTrackingServiceImpl(restApiClient);
    }

    @Test
    public void shouldCheckContainsUsageThresholdByLabelHubSupportWithCreatedStatus() {

        // Given
        var usageThresholds = TestData.getUsageThresholdListWithNotContainLabelHubSupport();

        // When
        when(restApiClient.findUsageTrackingThresholds(
                    TestData.ACCOUNT_ID,
                    TestData.VENDOR_ID,
                    TestData.AUTHENTICATED_ACCOUNT_ID)
            ).thenReturn(usageThresholds);

        // Then
        var actual = usageTrackingService.
                checkContainsUsageThresholdByLabelHubSupport(TestData.getUsageTrackingRequest());
        var expected = HttpStatus.CREATED;

        assertEquals(actual, expected);
    }

    @Test
    public void shouldCheckContainsUsageThresholdByLabelHubSupportWithConflictStatus() {

        // Given
        var usageThresholds = TestData.getUsageThresholdListWithContainLabelHubSupport();

        // When
        when(restApiClient.findUsageTrackingThresholds(
                TestData.ACCOUNT_ID,
                TestData.VENDOR_ID,
                TestData.AUTHENTICATED_ACCOUNT_ID)
        ).thenReturn(usageThresholds);

        // Then
        var actual = usageTrackingService.
                checkContainsUsageThresholdByLabelHubSupport(TestData.getUsageTrackingRequest());
        var expected = HttpStatus.CONFLICT;

        assertEquals(actual, expected);
    }

    @Test
    public void shouldCheckContainsUsageThresholdByLabelHubSupportWithException() {

        // Given
        UsageTrackingRequest request = TestData.getUsageTrackingRequest();
        request.setAccountId(null);

        // When
        when(restApiClient.findUsageTrackingThresholds(
                any(), any(), any())
        ).thenReturn(null);

        // Then
        assertThrows(RuntimeException.class,
                () -> usageTrackingService.checkContainsUsageThresholdByLabelHubSupport(request));
    }

    @Test
    public void shouldAddUsageThresholdWithSuccess() {

        // Given
        var request = TestData.getUsageTrackingRequest();
        var usageThreshold = TestData.getUsageThresholdWithLabelHubSupport();

        // When
        when(restApiClient.createUsageTrackingThreshold(
                any(), any(), any(), any()
        )).thenReturn(usageThreshold);

        // Then
        usageTrackingService.add(request);
    }

    @Test
    public void shouldAddUsageThresholdWithException() {

        // Given
        var request = TestData.getUsageTrackingRequest();

        // When
        when(restApiClient.createUsageTrackingThreshold(
                any(), any(), any(), any()
        )).thenThrow(mock(RestClientException.class));

        // Then
        assertThrows(RuntimeException.class,
                () -> usageTrackingService.add(request));
    }

    @Test
    public void shouldIsContainUsageThresholdByLabelHubSupportWithContain() {

        // Given
        var usageThresholds = List.of(
                TestData.getUsageThresholdWithLabelHubSupport(),
                TestData.getUsageThresholdDailyPeriod()
        );

        // When
        // Then
        boolean actual = usageTrackingService.isContainsUsageThresholdByLabelHubSupport(usageThresholds);
        assertEquals(actual, Boolean.TRUE);
    }

    @Test
    public void shouldIsContainUsageThresholdByLabelHubSupportWithNotContain() {

        // Given
        var usageThresholds = List.of(
                TestData.getUsageThresholdWithNonLabelHubSupport(),
                TestData.getUsageThresholdDailyPeriod()
        );

        // When
        // Then
        boolean actual = usageTrackingService.isContainsUsageThresholdByLabelHubSupport(usageThresholds);
        assertEquals(actual, Boolean.FALSE);
    }
}
