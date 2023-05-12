/*
 * Copyright (c) Message4U Pty Ltd 2014-2018
 *
 * Except as otherwise permitted by the Copyright Act 1967 (Cth) (as amended from time to time) and/or any other
 * applicable copyright legislation, the material may not be reproduced in any format and in any way whatsoever
 * without the prior written consent of the copyright owner.
 */

package com.phucdevs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(value = {
        Context.class,
        Initializer.class,
        RestUtilConfig.class,
        FeatureSetClientConfig.class,
        AMSClientConfig.class
})
@ComponentScan(basePackages = {"com.phucdevs"})
@PropertySource("classpath:test.environment.properties")
public class ServiceTestConfig {
}
