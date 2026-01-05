/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty.Switch;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceGeneral;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceGeneralSetting;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.InputSource;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.Display;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.General;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.GeneralSetting;
import com.avispl.symphony.dal.util.ControllablePropertyFactory;

/**
 * Utility class providing helper methods for controllable property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControlUtil {
	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceGeneral}.
	 *
	 * @param deviceGeneral the {@link DeviceGeneral} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the general
	 */
	public static List<AdvancedControllableProperty> getGeneralControllers(DeviceGeneral deviceGeneral) {
		if (deviceGeneral == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(
				createSwitch(General.POWER_STATUS.getName(), Constant.OFF_STANDBY, Constant.ON, Integer.parseInt(deviceGeneral.getPowerStatus()))
		);
	}

	/**
	 * Generates a list of {@link AdvancedControllableProperty} for the given {@link DeviceGeneralSetting}.
	 *
	 * @param deviceGeneralSetting the {@link DeviceGeneralSetting} to build controllers from; if null, an empty list is returned
	 * @return list of controllable properties for the general settings
	 */
	public static List<AdvancedControllableProperty> getGeneralSettingsControllers(DeviceGeneralSetting deviceGeneralSetting) {
		if (deviceGeneralSetting == null) {
			return Collections.emptyList();
		}
		return List.of(
				ControllablePropertyFactory.createDropdown(GeneralSetting.INPUT_SOURCE.getPropertyName(), InputSource.getNames(), InputSource.getNameByCode(deviceGeneralSetting.getInputSource())),
//				ControllablePropertyFactory.createSwitch(GeneralSetting.PIP_MODE.getPropertyName(), Integer.parseInt(deviceGeneralSetting.getPipMode())),
				ControllablePropertyFactory.createSwitch(GeneralSetting.TILING_MODE.getPropertyName(), Integer.parseInt(deviceGeneralSetting.getTilingMode())),
				ControllablePropertyFactory.createDropdown(GeneralSetting.VOLUME.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceGeneralSetting.getVolume()))),
				createSwitch(GeneralSetting.VOLUME_MUTE.getPropertyName(), Constant.UNMUTE, Constant.MUTE, Integer.parseInt(deviceGeneralSetting.getMute()))
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
			return Collections.emptyList();
		}
		return List.of(
				ControllablePropertyFactory.createSwitch(Display.BACKLIGHT_STATUS.getPropertyName(), Integer.parseInt(deviceDisplay.getBacklightStatus())),
				ControllablePropertyFactory.createDropdown(Display.BACKLIGHT.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getBacklight()))),
//				ControllablePropertyFactory.createDropdown(Display.BLUE_LIGHT_FILTER.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getBluelightFilter()))),
				ControllablePropertyFactory.createDropdown(Display.BRIGHTNESS.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getBrightness()))),
				ControllablePropertyFactory.createDropdown(Display.COLOR.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getColor()))),
				ControllablePropertyFactory.createDropdown(Display.CONTRAST.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getContrast())))
//				ControllablePropertyFactory.createDropdown(Display.HUE.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getTint()))),
//				ControllablePropertyFactory.createDropdown(Display.SHARPNESS.getPropertyName(), Constant.RANGE_0_TO_100, String.valueOf(Integer.parseInt(deviceDisplay.getSharpness())))
		);
	}

	private static AdvancedControllableProperty createSwitch(String name, String labelOff, String labelOn, Object value) {
		var sw = new Switch();
		sw.setLabelOff(labelOff);
		sw.setLabelOn(labelOn);

		return new AdvancedControllableProperty(name, new Date(), sw, value);
	}
}
