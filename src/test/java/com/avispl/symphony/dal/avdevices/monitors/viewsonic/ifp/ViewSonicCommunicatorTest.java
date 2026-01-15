/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.types.properties.Display;

/**
 * Unit tests for the {@link ViewSonicCommunicator} class.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
class ViewSonicCommunicatorTest {
	private ExtendedStatistics extendedStatistics;
	private ViewSonicCommunicator communicator;

	@BeforeEach
	void setUp() throws Exception {
		this.communicator = new ViewSonicCommunicator();
		this.communicator.setHost("");
		this.communicator.setPort(5000);
		this.communicator.setLogin("");
		this.communicator.setPassword("");
		this.communicator.init();
		this.communicator.connect();
	}

	@AfterEach
	void destroy() throws Exception {
		this.communicator.disconnect();
		this.communicator.destroy();
	}

	@Test
	void testGetMultipleStatistics() throws Exception {
		this.communicator.setDeviceId("01");
		this.communicator.setDisplayPropertyGroups(Constant.ALL);
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		Map<String, String> statistics = this.extendedStatistics.getStatistics();

		this.verifyStatistics(statistics);
	}

	@Test
	void testGetMultipleStatisticsWithDisplayPropertyGroups() throws Exception {
		this.communicator.setDeviceId("01");
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		Map<String, String> statistics = this.extendedStatistics.getStatistics();
		Assertions.assertEquals(Constant.GENERAL_GROUP, this.communicator.getDisplayPropertyGroups());
		Assertions.assertTrue(statistics.keySet().stream()
				.noneMatch(k -> k.startsWith(Constant.DISPLAY_GROUP) && k.startsWith(Constant.SETTING_GROUP)));
	}

	@Test
	void testControlProperties() throws Exception {
		this.communicator.setDeviceId("01");
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		var testedProperty = Display.BACKLIGHT.getPropertyName();
		var controllableProperty = new ControllableProperty(testedProperty, "20.0", null);

		this.communicator.controlProperty(controllableProperty);
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		var statistics = this.extendedStatistics.getStatistics();

		Assertions.assertEquals("20", statistics.get(testedProperty));
	}

	private void verifyStatistics(Map<String, String> statistics) {
		Map<String, Map<String, String>> groups = new LinkedHashMap<>();
		groups.put(Constant.GENERAL_GROUP, this.filterGroupStatistics(statistics, null));
		groups.put(Constant.ADAPTER_METADATA_GROUP, this.filterGroupStatistics(statistics, Constant.ADAPTER_METADATA_GROUP));
		groups.put(Constant.DISPLAY_GROUP, this.filterGroupStatistics(statistics, Constant.DISPLAY_GROUP));
		groups.put(Constant.SETTING_GROUP, this.filterGroupStatistics(statistics, Constant.SETTING_GROUP));

		for (Map<String, String> initGroup : groups.values()) {
			for (Map.Entry<String, String> initStatistics : initGroup.entrySet()) {
				Assertions.assertNotNull(initStatistics.getValue(), "Value is null with property: " + initStatistics.getKey());
			}
		}
	}

	private Map<String, String> filterGroupStatistics(Map<String, String> statistics, String groupName) {
		return statistics.entrySet().stream()
				.filter(e -> (groupName == null) ? !e.getKey().contains("#") : e.getKey().startsWith(groupName))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
