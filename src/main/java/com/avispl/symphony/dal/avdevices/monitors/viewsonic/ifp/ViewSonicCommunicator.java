/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases.BaseCommunicator;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.AdapterMetadata;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.utils.MonitoringUtil;

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

	public ViewSonicCommunicator() {
		super();
		this.versionProperties = new Properties();
		this.adapterInitializationTimestamp = System.currentTimeMillis();
		this.localExtendedStatistics = new ExtendedStatistics();
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
		super.internalDestroy();
	}

	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		this.reentrantLock.lock();
		try {
			var statistics = new HashMap<String, String>();
			statistics.putAll(MonitoringUtil.generateProperties(
					AdapterMetadata.values(), Constant.ADAPTER_METADATA_GROUP,
					property -> MonitoringUtil.mapToAdapterMetadata(this.versionProperties, property)
			));

			this.localExtendedStatistics.setStatistics(statistics);
		} finally {
			this.reentrantLock.unlock();
		}
		return Collections.singletonList(this.localExtendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) throws Exception {

	}

	@Override
	public void controlProperties(List<ControllableProperty> controllableProperties) throws Exception {

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
}
