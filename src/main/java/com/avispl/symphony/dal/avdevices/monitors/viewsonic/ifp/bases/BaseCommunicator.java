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
	 * Sends a protocol-formatted request to the device and parses the response according to the protocol specification.
	 *
	 * <p>The response is processed by:
	 * <ul>
	 *   <li>Removing carriage return characters</li>
	 *   <li>Trimming leading and trailing whitespace</li>
	 *   <li>Validating the response header and command code</li>
	 *   <li>Extracting the response payload when applicable</li>
	 * </ul>
	 *
	 * @param <C> the command type, which must be an {@link Enum} implementing {@link BaseCommand}
	 * @param command the command associated with the request
	 * @param request the command-specific request payload (without protocol header)
	 * @return the parsed response payload for a successful command
	 * @throws CommandFailureException if the response is invalid, does not match the command, or the device does not respond
	 * @throws Exception if a transport-level error occurs while communicating with the device
	 */
	private <C extends Enum<C> & BaseCommand> String sendAndParse(C command, String request) throws Exception {
		try {
			var normalizedCommand = Constant.COMMAND_LENGTH + this.deviceId + request;
			var response = super.send(normalizedCommand.getBytes(StandardCharsets.US_ASCII));
			if (response.length == 1 && response[0] == (byte) -1) {  //	handle no response
				throw new CommandFailureException(this.getAddress(), normalizedCommand, null, HttpStatus.NOT_FOUND.value());
			}
			var normalizedResponse = new String(response, StandardCharsets.US_ASCII).replace(Constant.CR, Constant.EMPTY).trim();
			if (normalizedResponse.length() < 4) {  //	handle undefine response
				throw new CommandFailureException(this.getAddress(), normalizedCommand, normalizedResponse, HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			switch (normalizedResponse.substring(0, 3)) {
				case Constant.GET_RESPONSE_HEADER_1, Constant.GET_RESPONSE_HEADER_2 -> {  //	handle GET response
					var expectedPrefix = Constant.COMMAND_LENGTH + this.deviceId + command.getType() + command.getCode();
					if (normalizedResponse.charAt(4) != command.getCode().charAt(0) || !normalizedCommand.startsWith(expectedPrefix)) {
						throw new CommandFailureException(this.getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_GATEWAY.value());
					}
					return normalizedResponse.substring(expectedPrefix.length());
				}
				case Constant.SET_RESPONSE_HEADER -> {  //	handle SET response
					if (normalizedResponse.charAt(3) == Constant.NEGATIVE_ACK) {
						throw new CommandFailureException(this.getAddress(), normalizedCommand, normalizedResponse, HttpStatus.BAD_REQUEST.value());
					}
					return normalizedResponse;
				}
				default -> throw new CommandFailureException(this.getAddress(), normalizedCommand, normalizedResponse, HttpStatus.NOT_FOUND.value());
			}
		} catch (FailedLoginException e) {
			throw e;
		} catch (Exception e) {
			//	handle invalid control property
			if (e instanceof CommandFailureException cmdEx && HttpStatus.BAD_REQUEST.value() == cmdEx.getStatusCode()) {
				throw new InvalidArgumentException(Constant.CONTROL_PROPERTY_FAILED, cmdEx);
			}
			throw new IllegalStateException(Constant.FETCH_DATA_FAILED.formatted(command), e);
		} finally {
			this.disconnect();
		}
	}
}
