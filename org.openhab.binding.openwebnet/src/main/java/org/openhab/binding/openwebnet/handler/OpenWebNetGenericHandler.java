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

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.openwebnet.OpenWebNetBindingConstants;
import org.openwebnet.message.BaseOpenMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OpenWebNetGenericHandler} is responsible for handling Generic OpenWebNet
 * devices. It does not too much, but it is needed to avoid handler errors.
 * It extends the abstract {@link OpenWebNetThingHandler}.
 *
 * @author Massimo Valla - Initial contribution
 */
public class OpenWebNetGenericHandler extends OpenWebNetThingHandler {

    private final Logger logger = LoggerFactory.getLogger(OpenWebNetGenericHandler.class);

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = OpenWebNetBindingConstants.GENERIC_SUPPORTED_THING_TYPES;

    public OpenWebNetGenericHandler(@NonNull Thing thing) {
        super(thing);
        logger.debug("==OWN:GenericHandler== constructor");
    }

    @Override
    public void initialize() {
        super.initialize();
        logger.debug("==OWN:GenericHandler== initialize() thing={}", thing.getUID());
    }

    @Override
    protected void requestChannelState(ChannelUID channel) {
        // do nothing
        logger.warn("==OWN:GenericHandler== There are no channels here!");
    }

    @Override
    protected void handleChannelCommand(ChannelUID channel, Command command) {
        // do nothing
        logger.warn("==OWN:GenericHandler== There are no channels here!");
    }

    @Override
    protected String ownIdPrefix() {
        return "G";
    }

    @Override
    protected void handleMessage(BaseOpenMessage msg) {
        super.handleMessage(msg);
        // do nothing
        logger.warn("==OWN:GenericHandler== Nothing to do!");
    }

} // class
