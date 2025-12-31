/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents detailed general setting about a device.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class DeviceGeneralSetting {
	String inputSource;
	String pipMode;
	String tilingMode;
	String volume;
	String mute;
}
