/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseProperty;

/**
 * Represents general properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum General implements BaseProperty {
	DEVICE_NAME("DeviceName"),
	FIRMWARE_VERSION("FirmwareVersion"),
	IP_ADDRESS("IPAddress"),
	MAC_ADDRESS("MACAddress"),
	POWER_STATUS("PowerStatus"),
	SERIAL_NUMBER("SerialNumber");

	String name;
}
