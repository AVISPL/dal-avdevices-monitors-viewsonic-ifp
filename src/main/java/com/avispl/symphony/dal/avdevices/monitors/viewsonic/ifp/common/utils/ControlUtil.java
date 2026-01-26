/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils;

import static com.avispl.symphony.dal.util.ControllablePropertyFactory.createDropdown;
import static com.avispl.symphony.dal.util.ControllablePropertyFactory.createSlider;
import static com.avispl.symphony.dal.util.ControllablePropertyFactory.createSwitch;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty.Switch;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.Logger;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceGeneral;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceSetting;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.InputSource;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.Display;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.General;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.Settings;

/**
 * Utility class providing helper methods for controllable property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControlUtil {
	private static final Logger LOG = Logger.ofClass(ControlUtil.class);

	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceGeneral}.
	 *
	 * @param deviceGeneral the {@link DeviceGeneral} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the general
	 */
	public static List<AdvancedControllableProperty> getGeneralControllers(DeviceGeneral deviceGeneral) {
		if (deviceGeneral == null) {
			LOG.warn("Skip general controllable properties retrieval, the device general data is null");
			return Collections.emptyList();
		}
		return Collections.singletonList(
				createCustomSwitch(General.POWER_STATUS.getName(), Constant.STANDBY, Constant.ON, Util.toInt(deviceGeneral.getPowerStatus()))
		);
	}

	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceSetting}.
	 *
	 * @param deviceSetting the {@link DeviceSetting} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the general settings
	 */
	public static List<AdvancedControllableProperty> getGeneralSettingsControllers(DeviceSetting deviceSetting) {
		if (deviceSetting == null) {
			LOG.warn("Skip general settings controllable properties retrieval, the device setting data is null");
			return Collections.emptyList();
		}
		return List.of(
				createDropdown(Settings.INPUT_SOURCE.getPropertyName(), InputSource.getNames(), InputSource.getNameByCode(deviceSetting.getInputSource())),
//				createSwitch(GeneralSetting.PIP_MODE.getPropertyName(), Util.toInt(deviceGeneralSetting.getPipMode())),
				createSwitch(Settings.TILING_MODE.getPropertyName(), Util.toInt(deviceSetting.getTilingMode())),
				createSlider(Settings.VOLUME.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceSetting.getVolume())),
				createSwitch(Settings.MUTE.getPropertyName(), Util.toInt(deviceSetting.getMute()))
		);
	}

	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceDisplay}.
	 *
	 * @param deviceDisplay the {@link DeviceDisplay} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the display
	 */
	public static List<AdvancedControllableProperty> getDisplayControllers(DeviceDisplay deviceDisplay) {
		if (deviceDisplay == null) {
			LOG.warn("Skip display controllable properties retrieval, the device display data is null");
			return Collections.emptyList();
		}
		return List.of(
				createSwitch(Display.BACKLIGHT_STATUS.getPropertyName(), Util.toInt(deviceDisplay.getBacklightStatus())),
				createSlider(Display.BACKLIGHT.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getBacklight())),
//				createSlider(Display.BLUE_LIGHT_FILTER.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getBluelightFilter())),
				createSlider(Display.BRIGHTNESS.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getBrightness())),
				createSlider(Display.COLOR.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getColor())),
				createSlider(Display.CONTRAST.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getContrast()))
//				createSlider(Display.HUE.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getTint())),
//				createSlider(Display.SHARPNESS.getPropertyName(), Constant.PERCENTAGE_MIN, Constant.PERCENTAGE_MAX, Util.toFloat(deviceDisplay.getSharpness()))
		);
	}

	/**
	 * Resolves the property status value from the given input.
	 *
	 * @param value the raw status value
	 * @return the formatted status value, or {@code null} if the value is unsupported
	 */
	public static String getStatusValue(Object value) {
		return switch (String.valueOf(value)) {
			case "0" -> "000";
			case "1" -> "001";
			default -> null;
		};
	}

	/**
	 * Formats the given numeric value into a three-digit property range value.
	 *
	 * @param value the raw numeric value
	 * @return the value formatted as a three-digit string
	 * @throws NumberFormatException if the value is not a valid number
	 */
	public static String getRangeValue(Object value) {
		return String.format(Constant.THREE_DIGIT_NUMBER_FORMAT, new BigDecimal(String.valueOf(value)).intValue());
	}

	private static AdvancedControllableProperty createCustomSwitch(String name, String offLabel, String onLabel, int status) {
		var sw = new Switch();
		sw.setLabelOff(offLabel);
		sw.setLabelOn(onLabel);

		return new AdvancedControllableProperty(name, new Date(), sw, status);
	}
}
