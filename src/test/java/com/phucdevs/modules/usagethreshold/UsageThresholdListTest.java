/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.usagethreshold;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UsageThresholdListTest {

    @Test
    public void testUsageThresholdListWithConstructorNoArgument() {
        assertNotNull(TestData.getUsageThresholdListConstructorNoArgument());
    }

    @Test
    public void testUsageThresholdListWithToString() {
        assertToString(TestData.getUsageThresholdListWithContainLabelHubSupport());
    }
}

