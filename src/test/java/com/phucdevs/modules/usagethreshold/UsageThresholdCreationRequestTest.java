/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.usagethreshold;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class UsageThresholdCreationRequestTest {

    @Test
    public void testUsageThresholdCreationRequestWithConstructorNoArgument() {
        assertNotNull(TestData.getUsageThresholdCreationRequestConstructorNoArgument());
    }

    @Test
    public void testUsageThresholdCreationRequestWithToString() {
        assertToString(TestData.getUsageThresholdCreationRequest());
    }

    @Test
    public void testUsageThresholdCreationRequestWithSetter() {
        UsageThresholdCreationRequest request = TestData.getUsageThresholdCreationRequestConstructorNoArgument();
        request.setThreshold(1L);
        request.setLabel(TestData.HUB_SUPPORT_LABEL);
        request.setAction(GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD);
        request.setPeriod(GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY);
        request.setTimezone(DateTimeZone.forID(TestData.DEFAULT_TIMEZONE));
        request.setWindowType(GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED);
        assertThat(request.getThreshold(), equalTo(1L));
        assertThat(request.getLabel(), equalTo(TestData.HUB_SUPPORT_LABEL));
        assertThat(request.getAction(), equalTo(GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD));
        assertThat(request.getPeriod(), equalTo(GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY));
        assertThat(request.getTimezone(), equalTo(DateTimeZone.forID(TestData.DEFAULT_TIMEZONE)));
        assertThat(request.getWindowType(), equalTo(GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED));
    }
}

