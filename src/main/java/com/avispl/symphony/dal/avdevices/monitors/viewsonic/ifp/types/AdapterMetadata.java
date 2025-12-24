/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseProperty;

/**
 * Represents adapter metadata properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
public enum AdapterMetadata implements BaseProperty {
	ACTIVE_PROPERTY_GROUPS("ActivePropertyGroups", "adapter.active.property.groups"),
	ADAPTER_BUILD_DATE("AdapterBuildDate", "adapter.build.date"),
	ADAPTER_UPTIME("AdapterUptime", "adapter.uptime"),
	ADAPTER_UPTIME_MIN("AdapterUptime(min)", "adapter.uptime"),
	ADAPTER_VERSION("AdapterVersion", "adapter.version");

	private final String name;
	private final String property;
}
