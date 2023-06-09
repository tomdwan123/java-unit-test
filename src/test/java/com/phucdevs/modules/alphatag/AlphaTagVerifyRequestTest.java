/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.modules.alphatag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlphaTagVerifyRequestTest {

    @Test
    public void testAlphaTagVerifyRequestWithConstructorNoArgument() {
        assertNotNull(TestData.getAlphaTagVerifyRequestConstructorNoArgument());
    }
}