/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases;

import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.communicator.SocketCommunicator;

/**
 * Configures the communicator and provides helper methods for managing adapter properties.
 * <p>This class centralizes all communicator-related configuration and exposes utility methods to access adapter properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public abstract class BaseCommunicator extends SocketCommunicator {
	/** Lock for thread-safe operations. */
	protected final ReentrantLock reentrantLock;

	protected BaseCommunicator() {
		this.reentrantLock = new ReentrantLock();
	}

	@Override
	protected void internalInit() throws Exception {
		this.setCommandSuccessList(Collections.singletonList(Constant.CR));
		this.setCommandErrorList(Collections.singletonList(Constant.CR));
		super.internalInit();
	}
}
