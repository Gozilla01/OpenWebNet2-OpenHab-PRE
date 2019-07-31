/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.openwebnet.handler;

import static org.openhab.binding.openwebnet.OpenWebNetBindingConstants.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.openwebnet.OpenWebNetBindingConstants;
import org.openwebnet.message.BaseOpenMessage;
import org.openwebnet.message.Lighting;
import org.openwebnet.message.LightingExt;
import org.openwebnet.message.OpenMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenWebNetMotionDetectorHandler} is responsible for handling commands/messages for a Motion Detector
 * OpenWebNet device.
 * It extends the abstract {@link OpenWebNetThingHandler}.
 *
 * @author Massimo Valla - Initial contribution
 */

public class OpenWebNetMotionDetectorHandler extends OpenWebNetThingHandler {

    private final Logger logger = LoggerFactory.getLogger(OpenWebNetMotionDetectorHandler.class);

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = OpenWebNetBindingConstants.MOTION_DETECTOR_SUPPORTED_THING_TYPES;
    protected Lighting.Type lightingType = Lighting.Type.ZIGBEE;
    private final static int SCHEDULE_DELAY = 2000; // ms
    private final static String REQUEST_CHANNEL = "6";
    private boolean automaticToOff = false;

    // protected Command.Type commandType = Command.Type.ZIGBEE;
    public OpenWebNetMotionDetectorHandler(@NonNull Thing thing) {
        super(thing);
        logger.debug("==OWN:MotionDetectorHandler== constructor");
    }

    @Override
    public void initialize() {
        super.initialize();
        logger.debug("==OWN:MotionDetectorHandler== initialize() thing={}", thing.getUID());
        if (bridgeHandler != null && bridgeHandler.isBusGateway()) {
            lightingType = Lighting.Type.POINT_TO_POINT;
            if (getConfig().get(CONFIG_PROPERTY_AUTOMATICTOOFF) instanceof java.lang.Boolean) {
                automaticToOff = (boolean) getConfig().get(CONFIG_PROPERTY_AUTOMATICTOOFF);
            } else {
                logger.warn(
                        "==OWN== invalid discoveryByActivation parameter value (should be true/false). Keeping current value={}.",
                        automaticToOff);
            }
        }
    }

    @Override
    protected void requestChannelState(ChannelUID channel) {
        logger.debug("==OWN:MotionDetectorHandler== requestChannelState() thingUID={} channel={}", thing.getUID(),
                channel.getId());
        // is not possible to request channel state for Command buttons
        updateStatus(ThingStatus.ONLINE);
        updateState(channel, UnDefType.UNDEF);
        if (channel.getId().equals(CHANNEL_MOTION_DETECTOR_VALUE)) {
            bridgeHandler.gateway
                    .send(LightingExt.requestMotionDetectorStatus(toWhere(channel), lightingType, REQUEST_CHANNEL));
        }
    }

    @Override
    protected void handleChannelCommand(ChannelUID channel, Command command) {
        logger.debug("==OWN:MotionDetectorHandler== handleChannelCommand() command={} channel={}", command,
                channel.getId());
        if (command instanceof OnOffType) {
            if (OnOffType.ON.equals(command)) {
                bridgeHandler.gateway.send(LightingExt.requestMotionDetectorTurnOn(toWhere(channel), lightingType));
            } else if (OnOffType.OFF.equals(command)) {
                bridgeHandler.gateway.send(LightingExt.requestMotionDetectorTurnOff(toWhere(channel), lightingType));
            }
        } else {
            logger.warn("==OWN:MotionDetectorHandler== Unsupported command: {}", command);
        }
    }

    @Override
    protected String ownIdPrefix() {
        return org.openwebnet.message.Who.LIGHTING.value().toString();
    }

    @Override
    protected void handleMessage(BaseOpenMessage msg) {
        super.handleMessage(msg);
        updateSensorState((Lighting) msg);
    }

    /**
     * Updates sensor state or value based on a OWN message received
     *
     * @param msg the Lighting message received
     */
    private void updateSensorState(Lighting msg) {
        if (msg.isMovement() || msg.isEndMovement()) {
            updateSensorOnOffState(msg);
        } else {
            updateSensorValue(msg);
        }
    }

    /**
     * Updates sensor state based on a OWN message received
     *
     * @param msg the Lighting message received
     */
    private void updateSensorOnOffState(Lighting msg) {
        logger.debug("==OWN:MotionDetectorHandler== updateSensorOnOffState() for thing: {}", thing.getUID());
        String channelID = CHANNEL_MOTION_DETECTOR_SWITCH;
        if (msg.isMovement()) {
            updateState(channelID, OnOffType.ON);
            if (automaticToOff == true) {
                ScheduleToOff(channelID);
            }
        } else if (msg.isEndMovement()) {
            channelID = CHANNEL_MOTION_DETECTOR_SWITCH;
            updateState(channelID, OnOffType.OFF);
        }
    }

    /**
     * Updates sensor value based on a OWN message received
     *
     * @param msg the Lighting message received
     */
    private void updateSensorValue(Lighting msg) {
        /*
         * to extract value lux
         * example OWN with 23,7 Lux
         * #1*03#4#01*6*237##
         * #1*WHERE*REQUEST_CHANNEL*LUX##
         */
        String channelID = CHANNEL_MOTION_DETECTOR_VALUE;
        Integer value;
        if (msg.toString().indexOf("*6*") != -1) {
            value = Integer.parseInt(msg.toString().substring(
                    msg.toString().indexOf("*" + REQUEST_CHANNEL + "*") + REQUEST_CHANNEL.length() + 2,
                    msg.toString().indexOf(OpenMessage.FRAME_END)));
            logger.debug("==OWN:MotionDetectorHandler== updateSensorValue() for thing: {} value:{}", thing.getUID(),
                    value);
            updateState(channelID, new DecimalType(value));
        }
    }

    /**
     * Schedule to OFF
     *
     * @param channel
     **/
    private void ScheduleToOff(String channel) {
        scheduler.schedule(() -> {
            logger.debug("==OWN:MotionDetectorHandler== ScheduleReleased() where:{} sending virtual UnDef...",
                    toWhere(channel));
            updateState(channel, OnOffType.OFF);
        }, SCHEDULE_DELAY, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns a WHERE address (string) based on bridge type and unit (optional)
     *
     * @param unit device unit
     **/
    protected String toWhere(String unit) {
        logger.debug("==OWN:MotionDetectorHandler== toWhere(unit) ownId={}", ownId);
        if (bridgeHandler.isBusGateway()) {
            return deviceWhere;
        } else {
            return deviceWhere + unit;
        }
    }

    /**
     * Returns a WHERE address based on channel
     *
     * @param channel channel
     **/
    protected String toWhere(ChannelUID channel) {
        logger.debug("==OWN:CommandHandler== toWhere(ChannelUID) ownId={}", ownId);
        if (bridgeHandler.isBusGateway()) {
            return deviceWhere;
        } else if (channel.getId().equals(CHANNEL_SWITCH_02)) {
            return deviceWhere + BaseOpenMessage.UNIT_02;
        } else { // CHANNEL_SWITCH_01 or other channels
            return deviceWhere + BaseOpenMessage.UNIT_01;
        }
    }

} // class
