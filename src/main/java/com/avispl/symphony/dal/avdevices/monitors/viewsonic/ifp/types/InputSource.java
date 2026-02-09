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
	TV("TV", "000", "100"),
	AV("AV", "001", "101"),
	S_VIDEO("S-Video", "002", "102"),
	YPBPR("YPbPr", "003", "103"),
	HDMI1("HDMI1", "004", "104"),
	HDMI2("HDMI2", "014", "114"),
	HDMI3("HDMI3", "024", "124"),
	HDMI4("HDMI4", "034", "134"),
	DVI("DVI", "005", "105"),
	VGA1("VGA1", "006", "106"),
	VGA2("VGA2", "016", "116"),
	VGA3("VGA3", "026", "126"),
	SLOT_IN_PC("Slot-in PC (OPS/SDM)/HDBT", "007", "107"),
	INTERNAL_MEMORY("Internal memory", "008", "108"),
	DP("DP", "009", "109"),
	EMBEDDED_ANDROID("Embedded / Main (Android)", "00A", "10A");

	String name;
	String code;	//	Common code and inactive code for input source
	String activeCode;	//	Active code for input source

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
				.filter(source -> source.code.equalsIgnoreCase(code) || source.activeCode.equalsIgnoreCase(code))
				.map(InputSource::getName)
				.findFirst().orElse(null);
	}

	/**
	 * Resolves the input source code by its name.
	 *
	 * @param name the input source name
	 * @return the input source code, or {@code null} if not found or invalid
	 */
	public static String getCodeByName(Object name) {
		if (name == null) {
			return null;
		}
		var convertedName = name.toString().trim();
		if (convertedName.isEmpty()) {
			return null;
		}
		return Arrays.stream(values())
				.filter(source -> source.name.equalsIgnoreCase(convertedName)).map(InputSource::getCode)
				.findFirst().orElse(null);
	}
}
