/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Represents general setting properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Settings implements BaseProperty {
	INPUT_SOURCE("InputSource"),
	//	PIP_MODE("PIPMode"),
	TILING_MODE("TilingMode"),
	VOLUME("Volume(%)"),
	MUTE("Mute");

	String name;

	public String getPropertyName() {
		return Constant.SETTING_GROUP + Constant.HASH + name;
	}
}
