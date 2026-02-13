/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;

/**
 * Marker interface for command enums that define group commands.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public interface BaseCommand {
	String getType();

	String getCode();

	boolean isGetCommand();

	/**
	 * Generates a command string using the default value.
	 * <p>
	 * This method delegates to {@link #generateCommand(String)} with the default value {@code "000"}.
	 *
	 * @return the generated command string
	 */
	default String generateCommand() {
		return generateCommand("000");
	}

	/**
	 * Generates a command string using the specified value. The command is constructed by concatenating:
	 * <ul>
	 *   <li>Command type</li>
	 *   <li>Command code</li>
	 *   <li>The provided value</li>
	 *   <li>A carriage return character</li>
	 * </ul>
	 * The final format follows the protocol specification.
	 *
	 * @param value the value to be appended to the command
	 * @return the generated command string
	 */
	default String generateCommand(String value) {
		return getType() + getCode() + value + Constant.CR;
	}
}
