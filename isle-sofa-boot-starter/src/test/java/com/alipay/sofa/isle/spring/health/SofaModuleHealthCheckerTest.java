/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.isle.spring.health;

import com.alipay.sofa.healthcheck.configuration.HealthCheckConstants;
import com.alipay.sofa.isle.spring.configuration.SofaModuleAutoConfiguration;
import com.alipay.sofa.runtime.spring.configuration.SofaRuntimeAutoConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author abby.zh
 * @since 2.4.10
 */
public class SofaModuleHealthCheckerTest {

    private final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    @After
    public void closeContext() {
        this.applicationContext.close();
    }

    @Test
    public void testDefaultConfig() {
        this.applicationContext.register(SofaModuleAutoConfiguration.class);
        this.applicationContext.refresh();
        SofaModuleHealthChecker sofaModuleHealthChecker = applicationContext
            .getBean(SofaModuleHealthChecker.class);
        Assert.assertEquals(HealthCheckConstants.SOFABOOT_MODULE_CHECK_RETRY_DEFAULT_COUNT,
            sofaModuleHealthChecker.getRetryCount());
        Assert.assertEquals(HealthCheckConstants.SOFABOOT_MODULE_CHECK_RETRY_DEFAULT_INTERVAL,
            sofaModuleHealthChecker.getRetryTimeInterval());
    }

    @Test
    public void testCustomConfig() {
        int customRetryCount = 10;
        int customRetryInterval = 30;
        this.applicationContext.register(SofaModuleAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.applicationContext,
            HealthCheckConstants.SOFABOOT_MODULE_CHECK_RETRY_COUNT + "=" + customRetryCount);
        EnvironmentTestUtils.addEnvironment(this.applicationContext,
            HealthCheckConstants.SOFABOOT_MODULE_CHECK_RETRY_INTERVAL + "=" + customRetryInterval);
        this.applicationContext.register(SofaRuntimeAutoConfiguration.class);
        this.applicationContext.refresh();
        SofaModuleHealthChecker sofaModuleHealthChecker = applicationContext
            .getBean(SofaModuleHealthChecker.class);
        Assert.assertEquals(customRetryCount, sofaModuleHealthChecker.getRetryCount());
        Assert.assertEquals(customRetryInterval, sofaModuleHealthChecker.getRetryTimeInterval());
    }

}