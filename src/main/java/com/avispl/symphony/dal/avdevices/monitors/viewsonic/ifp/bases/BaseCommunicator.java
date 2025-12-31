/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpStatus;

import lombok.Getter;

import com.avispl.symphony.api.common.error.InvalidArgumentException;
import com.avispl.symphony.api.dal.error.CommandFailureException;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.utils.Util;
import com.avispl.symphony.dal.communicator.SocketCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

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

	@Getter
	private String deviceId;

	protected BaseCommunicator() {
		this.reentrantLock = new ReentrantLock();
	}

	@Override
	protected void internalInit() throws Exception {
		this.setCommandSuccessList(Collections.singletonList(Constant.CR));
		this.setCommandErrorList(Collections.singletonList(Constant.CR));
		super.internalInit();
	}

	/**
	 * Sets the device ID after validating and normalizing the input value from Adapter Properties.
	 * <p>
	 * The device ID is considered valid only if it:
	 * <ul>
	 *   <li>Is not {@code null} or empty</li>
	 *   <li>Contains numeric characters only</li>
	 *   <li>Is within the range {@code 1} to {@code 98} (inclusive)</li>
	 * </ul>
	 * <p>
	 * If the input is invalid, the {@code deviceId} field will be set to {@code null}.
	 * When valid, the device ID is formatted as a two-digit number (e.g. {@code "01"}, {@code "98"}).
	 *
	 * @param deviceId the raw device ID value to be validated and set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = null;
		if (StringUtils.isNullOrEmpty(deviceId, true) || Util.isNonNumeric(deviceId)) {
			return;
		}
		var parsedDeviceId = Integer.parseInt(deviceId);
		if (parsedDeviceId < 1 || parsedDeviceId > 98) {
			return;
		}
		this.deviceId = String.format(Constant.TWO_DIGIT_NUMBER_FORMAT, parsedDeviceId);
	}

	/**
	 * Validates required adapter properties before executing any command.
	 * <p>
	 * This method ensures that mandatory properties have been properly initialized.
	 * Currently, it validates that {@code deviceId} is not {@code null}.
	 *
	 * @throws InvalidArgumentException if the device ID has not been set or is invalid
	 */
	protected void validateAdapterProperties() {
		if (this.deviceId == null) {
			throw new InvalidArgumentException(Constant.SET_DEVICE_ID_FAILED);
		}
	}

	/**
	 * Sends a command to the device and retrieves the normalized response data.
	 * <p>
	 * This method builds a request command using the configured device ID and the provided command definition,
	 * sends it to the device, and processes the response according to the protocol specification.
	 * <p>
	 * The response will be normalized by:
	 * <ul>
	 *   <li>Removing carriage return characters</li>
	 *   <li>Trimming leading and trailing whitespace</li>
	 *   <li>Stripping protocol-specific header fields (command length, device ID, command type, and command code)</li>
	 * </ul>
	 *
	 * @param <C> the command type, which must be an {@link Enum} implementing {@link BaseCommand}
	 * @param command the command used to generate the request
	 * @return the normalized response payload, or {@code null} if the response is invalid or indicates an error
	 * @throws Exception if an error occurs while sending data
	 */
	protected <C extends Enum<C> & BaseCommand> String send(C command) throws Exception {
		var normalizedCommand = Constant.COMMAND_LENGTH + this.deviceId + command.generateCommand();
		var response = super.send(normalizedCommand.getBytes(StandardCharsets.US_ASCII));
		if (response.length == 1 && response[0] == -1) {
			return null;
		}
		var normalizedResponse = new String(response, StandardCharsets.US_ASCII).replace(Constant.CR, Constant.EMPTY).trim();
		var invalidResponse = normalizedResponse.charAt(3) == '-';
		var isResponseMismatch = normalizedResponse.charAt(4) != command.getCode().charAt(0);
		if (invalidResponse || isResponseMismatch) {
			throw new CommandFailureException(this.getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_REQUEST.value());
		}

		return normalizedResponse.substring(5);  // remove command length, device id, command type and command code
	}
}
