/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.bases;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import javax.security.auth.login.FailedLoginException;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import com.avispl.symphony.api.common.error.InvalidArgumentException;
import com.avispl.symphony.api.dal.error.CommandFailureException;
import com.avispl.symphony.dal.avdevices.monitors.viewsonic.ifp.common.Logger;
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
	/** Set of supported group filter for {@link #displayPropertyGroups}. */
	private static final Set<String> SUPPORTED_GROUP_FILTERS = new TreeSet<>(Set.of(
			Constant.GENERAL_GROUP, Constant.DISPLAY_GROUP, Constant.SETTING_GROUP
	));

	/** Lock for thread-safe operations. */
	protected final ReentrantLock reentrantLock;
	/** Logger used for recording diagnostic and runtime information. */
	protected final Logger log;

	/** Adapter property representing the device ID, used when sending commands to the device. */
	@Getter
	private String deviceId;
	/** Indicates whether groups are displayed; defaults to {@link Constant#GENERAL_GROUP}. */
	private final Set<String> displayPropertyGroups;

	protected BaseCommunicator() {
		this.reentrantLock = new ReentrantLock();
		this.log = new Logger(super.logger);
		this.displayPropertyGroups = new TreeSet<>(Set.of(Constant.GENERAL_GROUP));
	}

	@Override
	protected void internalInit() throws Exception {
		this.setCommandSuccessList(Collections.singletonList(Constant.CR));
		this.setCommandErrorList(Collections.singletonList(Constant.CR));
		super.internalInit();
	}

	@Override
	protected void internalDestroy() {
		this.displayPropertyGroups.clear();
		super.internalDestroy();
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
		if (StringUtils.isNullOrEmpty(deviceId, true) || Util.isNonInt(deviceId)) {
			return;
		}
		var parsedDeviceId = Integer.parseInt(deviceId);
		if (parsedDeviceId < 1 || parsedDeviceId > 98) {
			return;
		}
		this.deviceId = String.format(Constant.TWO_DIGIT_NUMBER_FORMAT, parsedDeviceId);
	}

	/**
	 * Returns a comma-separated list of property group names that are configured to be displayed.
	 *
	 * @return a comma-separated string of display property group names; may be empty if no groups are configured
	 */
	public String getDisplayPropertyGroups() {
		return String.join(Constant.COMMA_SPACE, this.displayPropertyGroups);
	}

	/**
	 * Sets the display property groups based on a comma-separated list.
	 * <p>
	 * Trims values automatically. If {@link Constant#ALL} is present, {@link #SUPPORTED_GROUP_FILTERS} are added.
	 * Invalid groups trigger a warning and only the default group applied. {@code null} or empty input is ignored.
	 * </p>
	 *
	 * @param displayPropertyGroups comma-separated group names; may be {@code null} or empty
	 */
	public void setDisplayPropertyGroups(String displayPropertyGroups) {
		if (StringUtils.isNullOrEmpty(displayPropertyGroups, true)) {
			return;
		}
		Set<String> checkedGroups = Arrays.stream(displayPropertyGroups.split(Constant.COMMA))
				.map(String::trim).filter(p -> !p.isEmpty()).collect(Collectors.toSet());
		if (checkedGroups.contains(Constant.ALL)) {
			this.displayPropertyGroups.addAll(SUPPORTED_GROUP_FILTERS);
			return;
		}
		if (!CollectionUtils.containsAny(SUPPORTED_GROUP_FILTERS, checkedGroups)) {
			this.log.warn(Constant.NO_VALID_DISPLAY_PROPERTY_GROUPS_WARNING.formatted(displayPropertyGroups));
		} else {
			this.displayPropertyGroups.removeIf(propertyGroup -> !Constant.GENERAL_GROUP.equals(propertyGroup));
			checkedGroups.stream().filter(SUPPORTED_GROUP_FILTERS::contains).forEach(this.displayPropertyGroups::add);
		}
	}

	/**
	 * Checks whether the specified property group is configured to be displayed.
	 *
	 * @param groupName the name of the property group to check
	 * @return {@code true} if the group is configured to be displayed; {@code false} otherwise
	 */
	protected boolean shouldShowGroup(String groupName) {
		return CollectionUtils.isNotEmpty(this.displayPropertyGroups) && this.displayPropertyGroups.contains(groupName);
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
	 * Sends a command to the device and returns the parsed response payload.
	 *
	 * @param <C> the command type, which must be an {@link Enum} implementing {@link BaseCommand}
	 * @param command the command used to generate the request payload
	 * @return the parsed response payload, or {@code null} if no response is received
	 * @throws CommandFailureException if the response is invalid or does not match the command
	 * @throws Exception if an error occurs while sending or receiving data
	 */
	protected <C extends Enum<C> & BaseCommand> String send(C command) throws Exception {
		return this.sendAndParse(command, command.generateCommand());
	}

	/**
	 * Sends a command with an associated value to the device.
	 *
	 * @param <C> the command type, which must be an {@link Enum} implementing {@link BaseCommand}
	 * @param command the command used to generate the request payload
	 * @param value the value associated with the command
	 * @throws CommandFailureException if the response is invalid or does not match the command
	 * @throws Exception if an error occurs while sending or receiving data
	 */
	protected <C extends Enum<C> & BaseCommand> void send(C command, String value) throws Exception {
		this.sendAndParse(command, command.generateCommand(value));
	}

	/**
	 * Sends a protocol-formatted command to the device and parses the response.
	 *
	 * <p>This method performs the following steps:
	 * <ul>
	 *   <li>Builds the normalized command using protocol rules</li>
	 *   <li>Sends the request to the device</li>
	 *   <li>Retries on gateway-level failures up to a maximum number of attempts</li>
	 *   <li>Normalizes the raw response</li>
	 *   <li>Validates and parses the response based on command type</li>
	 * </ul>
	 *
	 * @param <C> the command enum type implementing {@link BaseCommand}
	 * @param command the command definition associated with the request
	 * @param request the command-specific payload (without protocol header)
	 * @return the parsed response payload if the command is successful
	 * @throws FailedLoginException if authentication with the device fails
	 * @throws InvalidArgumentException if the device rejects the control property
	 * @throws IllegalStateException if the request fails after retries or a communication error occurs
	 * @throws Exception if a transport-level error occurs
	 */
	private <C extends Enum<C> & BaseCommand> String sendAndParse(C command, String request) throws Exception {
		var normalizedCommand = Constant.ACK_RESPONSE_LENGTH + this.deviceId + request;
		var maxRetry = 3;

		for (int attemp = 1; attemp <= maxRetry; attemp++) {
			try {
				var response = this.sendRequest(normalizedCommand);
				var normalizedResponse = this.normalizeResponse(response);
				return this.parseResponse(command, normalizedCommand, normalizedResponse);
			} catch (FailedLoginException e) {
				this.disconnect();
				throw e;
			} catch (Exception e) {
				this.disconnect();
				if (Constant.SET_COMMAND_TYPE.equals(command.getType())) {
					throw new InvalidArgumentException(Constant.CONTROL_PROPERTY_FAILED, e);
				}
				if (attemp < maxRetry) {
					this.log.error("Attempt %s failed for request '%s', retrying".formatted(attemp, normalizedCommand), e);
					Thread.sleep(400);
					continue;
				}
				throw new IllegalStateException(Constant.FETCH_DATA_FAILED.formatted(command), e);
			} finally {
				Thread.sleep(100);
			}
		}
		this.log.warn("All %s attempts failed for request='%s'. Returning null response.".formatted(maxRetry, normalizedCommand));
		return null;
	}

	/**
	 * Sends a normalized command to the device and returns the raw response.
	 * If the device does not respond, a {@link CommandFailureException} is thrown.
	 *
	 * @param normalizedCommand the fully formatted command string
	 * @return the raw response bytes from the device
	 * @throws CommandFailureException if the device does not return any response
	 * @throws Exception if a transport-level error occurs
	 */
	private byte[] sendRequest(String normalizedCommand) throws Exception {
		var response = super.send(normalizedCommand.getBytes(StandardCharsets.US_ASCII));
		if (response.length == 1 && response[0] == (byte) -1) {  //	handle no response
			throw new CommandFailureException(this.getAddress(), normalizedCommand, null, HttpStatus.NOT_FOUND.value());
		}
		return response;
	}

	/**
	 * Normalizes the raw response returned from the device.
	 * <p>This method:
	 * <ul>
	 *   <li>Converts bytes to an ASCII string</li>
	 *   <li>Removes carriage return characters</li>
	 *   <li>Trims leading and trailing whitespace</li>
	 * </ul>
	 *
	 * @param response the raw response bytes
	 * @return the normalized response string
	 * @throws CommandFailureException if the response is too short or invalid
	 */
	private String normalizeResponse(byte[] response) {
		var normalizedResponse = new String(response, StandardCharsets.US_ASCII).replace(Constant.CR, Constant.EMPTY).trim();
		if (normalizedResponse.length() < 4) {  //	handle undefine response
			throw new CommandFailureException(getAddress(), null, normalizedResponse, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return normalizedResponse;
	}

	/**
	 * Parses and validates the normalized response based on the command type.
	 * <p>Supported behaviors:
	 * <ul>
	 *   <li>GET response: validates header, command type, and command code, then extracts payload</li>
	 *   <li>SET response: checks acknowledgment status</li>
	 * </ul>
	 *
	 * @param <C> the command enum type implementing {@link BaseCommand}
	 * @param command the original command definition
	 * @param normalizedCommand the normalized command sent to the device
	 * @param normalizedResponse the normalized response string
	 * @return the parsed response payload
	 * @throws CommandFailureException if the response is invalid, mismatched, or rejected
	 */
	private <C extends Enum<C> & BaseCommand> String parseResponse(C command, String normalizedCommand, String normalizedResponse) {
		if (normalizedResponse.length() < 4) {
			throw new CommandFailureException(getAddress(), normalizedCommand, normalizedResponse, HttpStatus.NOT_FOUND.value());
		}
		var statusResponseHeader = Constant.STATUS_RESPONSE_LENGTH + this.deviceId;
		//	Handle NON-GET response
		if (!command.isGetCommand()) {
			if ((statusResponseHeader + Constant.NEGATIVE_ACK).equals(normalizedResponse)) {
				throw new CommandFailureException(getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_REQUEST.value());
			}
			return normalizedResponse;
		}
		var responseHeader = normalizedResponse.substring(0, 3);
		//	Handle ACK response
		if ((Constant.ACK_RESPONSE_HEADER_1 + this.deviceId).equals(responseHeader)
				|| (Constant.ACK_RESPONSE_HEADER_2 + this.deviceId).equals(responseHeader)) {
			var expectedPrefix = Constant.ACK_RESPONSE_LENGTH + deviceId + command.getType() + command.getCode();
			if (normalizedResponse.charAt(4) != command.getCode().charAt(0) || !normalizedCommand.startsWith(expectedPrefix)) {
				throw new CommandFailureException(getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_GATEWAY.value());
			}
			return normalizedResponse.substring(expectedPrefix.length());
		}
		// Handle STATUS response
		if (statusResponseHeader.equals(responseHeader)) {
			if (normalizedResponse.charAt(3) == Constant.NEGATIVE_ACK) {
				throw new CommandFailureException(getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_REQUEST.value());
			}
			return normalizedResponse;
		}
		throw new CommandFailureException(getAddress(), normalizedCommand, normalizedResponse, HttpStatus.NOT_FOUND.value());
	}
}
