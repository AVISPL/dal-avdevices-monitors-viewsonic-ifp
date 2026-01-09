/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseCommunicator;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils.ControlUtil;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils.MonitoringUtil;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceGeneral;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.models.DeviceGeneralSetting;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.InputSource;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands.DisplayCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands.GeneralCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.commands.GeneralSettingCommand;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.AdapterMetadata;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.Display;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.General;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.GeneralSetting;

/**
 * Main adapter class for View Sonic (Direct). Responsible for generating monitoring, controllable.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public class ViewSonicCommunicator extends BaseCommunicator implements Monitorable, Controller {
	/** Application configuration loaded from {@code version.properties}. */
	private final Properties versionProperties;
	/** Device adapter instantiation timestamp. */
	private final long adapterInitializationTimestamp;
	/** Stores extended statistics to be sent to the adapter. */
	private final ExtendedStatistics localExtendedStatistics;

	/** Stores the general information of the device */
	private DeviceGeneral deviceGeneral;
	/** Stores the general setting of the device */
	private DeviceGeneralSetting deviceGeneralSetting;
	/** Stores the display setting of the device */
	private DeviceDisplay deviceDisplay;

	public ViewSonicCommunicator() {
		super();
		this.versionProperties = new Properties();
		this.adapterInitializationTimestamp = System.currentTimeMillis();
		this.localExtendedStatistics = new ExtendedStatistics();

		this.deviceGeneral = new DeviceGeneral();
		this.deviceGeneralSetting = new DeviceGeneralSetting();
		this.deviceDisplay = new DeviceDisplay();
	}

	@Override
	protected void internalInit() throws Exception {
		this.loadVersionProperties(this.versionProperties);
		super.internalInit();
	}

	@Override
	protected void internalDestroy() {
		this.versionProperties.clear();
		Optional.ofNullable(this.localExtendedStatistics.getStatistics()).ifPresent(Map::clear);
		Optional.ofNullable(this.localExtendedStatistics.getControllableProperties()).ifPresent(List::clear);

		this.deviceGeneral = null;
		this.deviceGeneralSetting = null;
		this.deviceDisplay = null;

		super.internalDestroy();
	}

	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		this.reentrantLock.lock();
		try {
			this.validateAdapterProperties();
			this.setupData();
			var statistics = new HashMap<String, String>();
			statistics.putAll(MonitoringUtil.generateProperties(
					General.values(), null,
					property -> MonitoringUtil.mapToGeneral(this.deviceGeneral, property)
			));
			statistics.putAll(MonitoringUtil.generateProperties(
					AdapterMetadata.values(), Constant.ADAPTER_METADATA_GROUP,
					property -> MonitoringUtil.mapToAdapterMetadata(this.versionProperties, property)
			));
			statistics.putAll(MonitoringUtil.generateProperties(
					Display.values(), Constant.DISPLAY_GROUP,
					property -> MonitoringUtil.mapToDisplay(this.deviceDisplay, property)
			));
			statistics.putAll(MonitoringUtil.generateProperties(
					GeneralSetting.values(), Constant.GENERAL_SETTING_GROUP,
					property -> MonitoringUtil.mapToGeneralSettings(this.deviceGeneralSetting, property)
			));

			var controllableProperties = new ArrayList<AdvancedControllableProperty>();
			controllableProperties.addAll(ControlUtil.getGeneralControllers(this.deviceGeneral));
			controllableProperties.addAll(ControlUtil.getDisplayControllers(this.deviceDisplay));
			controllableProperties.addAll(ControlUtil.getGeneralSettingsControllers(this.deviceGeneralSetting));

			this.localExtendedStatistics.setStatistics(statistics);
			this.localExtendedStatistics.setControllableProperties(controllableProperties);
		} finally {
			this.reentrantLock.unlock();
		}
		return Collections.singletonList(this.localExtendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) throws Exception {
		this.reentrantLock.lock();
		try {
			var property = controllableProperty.getProperty();
			//	General
			if (General.POWER_STATUS.getName().equals(property)) {
				this.send(GeneralCommand.SET_POWER_STATUS, ControlUtil.getStatusValue(controllableProperty.getValue()));
			}
			//	Display
			else if (Display.BACKLIGHT_STATUS.getPropertyName().equals(property)) {
				this.send(DisplayCommand.SET_BACKLIGHT_STATUS, ControlUtil.getStatusValue(controllableProperty.getValue()));
			} else if (Display.BACKLIGHT.getPropertyName().equals(property)) {
				this.send(DisplayCommand.SET_BACKLIGHT, ControlUtil.getRangeValue(controllableProperty.getValue()));
			} else if (Display.BRIGHTNESS.getPropertyName().equals(property)) {
				this.send(DisplayCommand.SET_BRIGHTNESS, ControlUtil.getRangeValue(controllableProperty.getValue()));
			} else if (Display.COLOR.getPropertyName().equals(property)) {
				this.send(DisplayCommand.SET_COLOR, ControlUtil.getRangeValue(controllableProperty.getValue()));
			} else if (Display.CONTRAST.getPropertyName().equals(property)) {
				this.send(DisplayCommand.SET_CONTRAST, ControlUtil.getRangeValue(controllableProperty.getValue()));
			}
			//	General settings
			else if (GeneralSetting.INPUT_SOURCE.getPropertyName().equals(property)) {
				this.send(GeneralSettingCommand.SET_INPUT_SOURCE, InputSource.getCodeByName(controllableProperty.getValue()));
			} else if (GeneralSetting.TILING_MODE.getPropertyName().equals(property)) {
				this.send(GeneralSettingCommand.SET_TILING_MODE, ControlUtil.getStatusValue(controllableProperty.getValue()));
			} else if (GeneralSetting.VOLUME.getPropertyName().equals(property)) {
				this.send(GeneralSettingCommand.SET_VOLUME, ControlUtil.getRangeValue(controllableProperty.getValue()));
			} else if (GeneralSetting.MUTE.getPropertyName().equals(property)) {
				this.send(GeneralSettingCommand.SET_MUTE, ControlUtil.getStatusValue(controllableProperty.getValue()));
			}
		} finally {
			this.reentrantLock.unlock();
		}
	}

	@Override
	public void controlProperties(List<ControllableProperty> controllableProperties) throws Exception {
		if (CollectionUtils.isEmpty(controllableProperties)) {
			return;
		}
		for (ControllableProperty controllableProperty : controllableProperties) {
			this.controlProperty(controllableProperty);
		}
	}

	/**
	 * Loads version properties and sets initial values used to create Adapter metadata group.
	 *
	 * @param versionProperties the properties to load and set default values
	 */
	private void loadVersionProperties(Properties versionProperties) {
		try {
			versionProperties.load(this.getClass().getResourceAsStream("/version.properties"));
			versionProperties.setProperty(AdapterMetadata.ACTIVE_PROPERTY_GROUPS.getProperty(), Constant.NOT_AVAILABLE);
			versionProperties.setProperty(AdapterMetadata.ADAPTER_UPTIME.getProperty(), String.valueOf(this.adapterInitializationTimestamp));
		} catch (IOException e) {
			this.logger.error(Constant.READ_PROPERTIES_FILE_FAILED, e);
		}
	}

	/**
	 * Initializes and loads required device data from the commands.
	 *
	 * @throws Exception if authentication or data retrieval fails
	 */
	private void setupData() throws Exception {
		this.deviceGeneral.setDeviceName(this.send(GeneralCommand.GET_DEVICE_NAME));
		this.deviceGeneral.setFirmwareVersion(this.send(GeneralCommand.GET_FIRMWARE_VERSION));
		this.deviceGeneral.setIpAddress(this.send(GeneralCommand.GET_IP_ADDRESS));
		this.deviceGeneral.setMacAddress(this.send(GeneralCommand.GET_MAC_ADDRESS));
		this.deviceGeneral.setPowerStatus(this.send(GeneralCommand.GET_POWER_STATUS));
		this.deviceGeneral.setSerialNumber(this.send(GeneralCommand.GET_SERIAL_NUMBER));

		this.deviceGeneralSetting.setInputSource(this.send(GeneralSettingCommand.GET_INPUT_SOURCE));
//		this.deviceGeneralSetting.setPipMode(this.send(GeneralSettingCommand.GET_PIP_MODE));
		this.deviceGeneralSetting.setTilingMode(this.send(GeneralSettingCommand.GET_TILING_MODE));
		this.deviceGeneralSetting.setVolume(this.send(GeneralSettingCommand.GET_VOLUME));
		this.deviceGeneralSetting.setMute(this.send(GeneralSettingCommand.GET_MUTE));

		this.deviceDisplay.setBacklightStatus(this.send(DisplayCommand.GET_BACKLIGHT_STATUS));
		this.deviceDisplay.setBacklight(this.send(DisplayCommand.GET_BACKLIGHT));
//		this.deviceDisplay.setBluelightFilter(this.send(DisplayCommand.GET_BLUE_LIGHT_FILTER));
		this.deviceDisplay.setBrightness(this.send(DisplayCommand.GET_BRIGHTNESS));
		this.deviceDisplay.setColor(this.send(DisplayCommand.GET_COLOR));
//		this.deviceDisplay.setColorMode(this.send(DisplayCommand.GET_COLOR_MODE));
		this.deviceDisplay.setContrast(this.send(DisplayCommand.GET_CONTRAST));
//		this.deviceDisplay.setTint(this.send(DisplayCommand.GET_TINT));
//		this.deviceDisplay.setSharpness(this.send(DisplayCommand.GET_SHARPNESS));
	}
}
