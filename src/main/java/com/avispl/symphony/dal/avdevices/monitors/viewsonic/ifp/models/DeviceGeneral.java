/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents detailed information about a device.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class DeviceGeneral {
	String deviceName;
	String firmwareVersion;
	String ipAddress;
	String macAddress;
	String powerStatus;
	String serialNumber;
}
