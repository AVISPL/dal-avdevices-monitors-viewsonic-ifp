/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.avispl.symphony.dal.util.StringUtils;

/**
 * Represents the available input sources supported by the device.
 * <p>
 * An input source defines the origin of the signal displayed on the device.
 * Each input source is associated with a protocol-specific code used for communication with the device.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum InputSource {
	TV("TV", "000"),
	AV("AV", "001"),
	S_VIDEO("S-Video", "002"),
	YPBPR("YPbPr", "003"),
	HDMI1("HDMI1", "004"),
	HDMI2("HDMI2", "014"),
	HDMI3("HDMI3", "024"),
	HDMI4("HDMI4", "034"),
	DVI("DVI", "005"),
	VGA1("VGA1", "006"),
	VGA2("VGA2", "016"),
	VGA3("VGA3", "026"),
	SLOT_IN_PC("Slot-in PC (OPS/SDM)/HDBT", "007"),
	INTERNAL_MEMORY("Internal memory", "008"),
	DP("DP", "009"),
	EMBEDDED_ANDROID("Embedded / Main (Android)", "00A");

	String name;
	String code;

	/**
	 * Returns all display names of input sources.
	 *
	 * @return a list of input source names
	 */
	public static List<String> getNames() {
		return Arrays.stream(values()).map(InputSource::getName).toList();
	}

	/**
	 * Resolves the input source name by its protocol code.
	 *
	 * @param code the input source code
	 * @return the input source name, or {@code null} if not found or invalid
	 */
	public static String getNameByCode(String code) {
		if (StringUtils.isNullOrEmpty(code)) {
			return null;
		}
		return Arrays.stream(values())
				.filter(source -> source.code.equalsIgnoreCase(code)).map(InputSource::getName)
				.findFirst().orElse(null);
	}
}
