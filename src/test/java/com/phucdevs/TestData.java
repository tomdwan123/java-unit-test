package com.phucdevs;

import java.util.List;
import java.util.UUID;

public class TestData {

    public static final String VENDOR_ID = "vendorIdTest0";
    public static final String ACCOUNT_ID = "accountIdTest0";
    public static final String FEATURE_SET_ID = "f19fca2b-8773-48d6-812f-030cdb4f2f58";
    public static final String ALPHA_TAG_ID = "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986";
    public static final String AUTHENTICATED_ACCOUNT_ID = "accountIdTest0";
    public static final String DEFAULT_TIMEZONE = "Australia/Melbourne";
    public static final String HUB_SUPPORT_LABEL = "created_by_support_tool_service";
    public static final String USAGE_TRACKING_URL = Constant.V2_URI + Constant.BASE_URL + "/usage-tracking";
    public static final String ALPHA_TAG = "alpha_test";

    public static UsageThreshold getUsageThresholdWithLabelHubSupport() {

        return new UsageThreshold(
                USAGE_TRACKING_URL,
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                HUB_SUPPORT_LABEL,
                GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD,
                DateTimeZone.forID(DEFAULT_TIMEZONE),
                GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED,
                1L,
                0L,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY
        );
    }

    public static UsageThreshold getUsageThresholdWithNonLabelHubSupport() {

        return new UsageThreshold(
                USAGE_TRACKING_URL,
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                "Created by Hub",
                GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD,
                DateTimeZone.forID(DEFAULT_TIMEZONE),
                GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED,
                1L,
                0L,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY
        );
    }

    public static UsageThreshold getUsageThresholdDailyPeriod() {

        return new UsageThreshold(
                USAGE_TRACKING_URL,
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                "Created by Hub",
                GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD,
                DateTimeZone.forID(
                        DEFAULT_TIMEZONE),
                GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED,
                100L,
                0L,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.DAILY
        );
    }

    public static UsageThreshold getUsageThresholdConstructorNoArgument() {
        return new UsageThreshold();
    }

    public static UsageThresholdList getUsageThresholdListWithContainLabelHubSupport() {

        return new UsageThresholdList(
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                List.of(
                        getUsageThresholdWithLabelHubSupport(),
                        getUsageThresholdDailyPeriod()
                )
        );
    }

    public static UsageThresholdList getUsageThresholdListWithNotContainLabelHubSupport() {

        return new UsageThresholdList(
                "b9ee3fe8-2c20-47b1-96e9-c5d12d7ed986",
                List.of(
                        getUsageThresholdWithNonLabelHubSupport(),
                        getUsageThresholdDailyPeriod()
                )
        );
    }

    public static UsageThresholdList getUsageThresholdListConstructorNoArgument() {
        return new UsageThresholdList();
    }

    public static UsageTrackingRequest getUsageTrackingRequest() {

        return new UsageTrackingRequest(
                VENDOR_ID,
                ACCOUNT_ID,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY,
                100L,
                ACCOUNT_ID
        );
    }

    public static UsageThresholdCreationRequest getUsageThresholdCreationRequestConstructorNoArgument() {
        return new UsageThresholdCreationRequest();
    }

    public static AlphaTagVerifyRequest getAlphaTagVerifyRequestConstructorNoArgument() {
        return new AlphaTagVerifyRequest();
    }

    public static UsageThresholdCreationRequest getUsageThresholdCreationRequest() {

        return new UsageThresholdCreationRequest(
                1L,
                HUB_SUPPORT_LABEL,
                GatewayUsageTrackingThreshold.UsageTrackingPeriod.MONTHLY,
                DateTimeZone.forID(DEFAULT_TIMEZONE),
                GatewayUsageTrackingThreshold.UsageTrackingAction.DISCARD,
                GatewayUsageTrackingThreshold.UsageTrackingTimezoneType.FIXED
        );
    }

    public static AlphaTagVerifyRequest getAlphaTagVerifyApprovedRequest() {

        return new AlphaTagVerifyRequest(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                AlphaTagStatusEnum.approved,
                ALPHA_TAG,
                ACCOUNT_ID);
    }

    public static AlphaTagVerifyRequest getAlphaTagVerifyRejectedRequest() {

        return new AlphaTagVerifyRequest(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                AlphaTagStatusEnum.rejected,
                ALPHA_TAG,
                ACCOUNT_ID);
    }
}
