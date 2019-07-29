# OpenWebNet (BTicino/Legrand) Binding - Installation instructions (BETA phase)

The OpenWebNet binding is still under development. This documentation is useful during the beta phase until the binding is added to the official openHAB 2 Addons distribution.
See the [README](./README.md) for the main documentation of the binding.

## Installation

The binding it's still *under development* and not part of the official openHAB distribution.
You must **follow carefully the following instruction steps 1. and 2.** in order to have the binding properly installed and working.

### 1. Bundle Installation

To install this binding two **alternative** options are available: from Marketplace or Manually

### 1a. Install from Marketplace

The binding can be installed from the [Eclipse IoT Marketplace](https://marketplace.eclipse.org/content/openwebnet-2x-binding-testing).

Make sure that the [marketplace plugin is activated](https://www.openhab.org/docs/configuration/eclipseiotmarket.html), and then install the *OpenWebNet binding* from PaperUI (Add-ons -> Bindings -> search for 'openwebnet' then INSTALL).

If you cannot find the binding in the search, probably you have an issue with certificates in your Java environment, that must be updated. Follow [this solution](https://community.openhab.org/t/solved-failed-downloading-marketplace-entries-received-fatal-alert-handshake-failure/52045) to add the required certificates to access all bindings on the Marketplace.

You will have to re-install the binding after an openHAB upgrade. This is a limitation of the Eclipse Marketplace plugin and will be changed in future versions of openHAB.

### 1b. Install Manually

1. Download the [latest released JAR file](https://github.com/mvalla/openhab2-addons/releases)

1. Copy the JAR file to your openHAB2 `addons` folder. On Linux and RaspberryPi it is under `/usr/share/openhab2/addons/`

### 2. Activate Dependencies

After the binding has been installed, from Marketplace or manually, some *features dependencies must be activated manually*:

- from [Karaf console](https://www.openhab.org/docs/administration/console.html):
    - `feature:install openhab-transport-serial`
    - `feature:install esh-io-transport-upnp`

The binding should now be installed: check in *PaperUI > Configuration > Bindings*.

After upgrading the binding to a new version, there is no need to activate dependencies again.

**However dependencies must be activated again if you upgrade openHAB to a new version or clean its cache.**

## Upgrade of the binding

When upgrading the binding to a new version it's suggested also to remove OpenWebNet Things before un-installing the old binding, and discover/configure them again after binding has been updated. Instead of removing things also re-setting the bridge in things properties should work.
If things are defined using .things file, they do not need to be re-created.

### Upgrade - Marketplace installation

1. Goto PaperUI > Add-ons > Bindings > search `openweb` > UNINSTALL
1. reload the page in the browser to make sure latest version is selected
1. search again `openweb` > INSTALL

### Upgrade - Manual installation

Since openHAB uses some cache mechanisms to load bindings, it is not enough to remove and update the binding JAR file from `addons` folder:

1. From [Karaf console](https://www.openhab.org/docs/administration/console.html):
    - `bundle:list` to list all bundles and take note of current bundle `<ID>` for the OpenWebNet Binding
    - `bundle:uninstall <ID>` to remove previous version of the binding
1. remove the previous version of the binding JAR file from `addons/` folder
1. copy the new version of the binding JAR file to `addons/` folder

The new version of the binding should now be installed, check the version number in *PaperUI > Configuration > Bindings*.

## Debugging and Log Files

This binding is currently *under development*. 

To help testing it and provide feedbacks, this is the procedure to set log level to `DEBUG`:

   - from [Karaf console](https://www.openhab.org/docs/administration/console.html):
     - `log:set DEBUG org.openhab.binding.openwebnet`
     - `log:set DEBUG org.openwebnet` 

Both `log:set` commands *are important* to log OWN commands exchanged between the binding and the OpenWebNet gateway.

Log files are written to either `userdata/log` (manual setup) or `/var/log/openhab2` (Linux and RaspberryPi image setup) and can be accessed using standard text reading software. 

The interesting file to provide for feedback is `openhab.log` (`events.log` is not so important).

## FAQs & Known Issues

### FAQs

#### My BTicino devices are visible from PaperUI but cannot be discovered by Google Home / Alexa

Not all device types are supported by Google Home / Alexa and the respective openHAB add-ons. This is not a limitation of the binding.
Visit the links at the end of section [Integration with Assistants](./README.md#integration-with-assistants) to check compatibility with your selected assistant.

#### When message/feature XXXX will be supported ?

You can check if someone has already requested support for a message/feature here: [GitHub repo](https://github.com/mvalla/openhab2-addons/issues).
If not, add a new issue. Issues are organised by milestones, but deadlines -of course- are not guaranteed (other volunteer developers are welcome!).

### Known Issues

For a full list of current open issues / features requests see [GitHub repo](https://github.com/mvalla/openhab2-addons/issues)

- In some cases dimmers connected to a F429 Dali-interface cannot be discovered, even if switched ON and dimmed. This looks like as a limitation of some gateways that do not report status of Dali devices when requested. See: https://github.com/mvalla/openhab2-addons/issues/14

## Changelog

**v2.5.0.M2-2** - 16/06/2019

- [FIX #76] Migrated to new OH2 build system (bndtools)
- [FIX #30] manually configured things are now ignored during auto-discovery
- [FIX #67] *[BREAKING CHANGE]* param discoveryByActivation changed to boolean (`"false"` -> `false`)
- [FIX #74] Updated README with energy example
- checked licence headers & javadocs. Improved logging
- removed logging INFO when discovering devices via UPnP
- firmware ver. and MAC address are now read from BUS gateway
- BUS gateway MAC addr. used as `serialNumber` (representation-property) to avoid discovery of same gateway that was added manually
- removed dependency on Guava lib
- Now using openwebnet-lib-0.9.20
- on the ZigBee part:
    - [FIX] fixed detecting wrong device id for discovered Zigbee devices
    - [FIX #77] Zigbee bridge: serial port as config parameter
    - *[BREAKING CHANGE]* ZigBee gateway: now `zigbeeid` is used as representation-property
    - *[BREAKING CHANGE]* renamed thing-types for ZigBee devices and GenericDevice. Previous ZigBee configured things must be updated (file) / re-discovered (PaperUI)
    - debug messages for ZB connect/connectGateway


**v2.5.0.M2-1** - **EXPIRED** - 20/03/2019

- [FIX #66] USB dongle (gateway) cannot connect anymore
- [FIX #65] Rollershutter % Position does not work in 2.5.0.M2
- removed Switchable tag to shutter channel

**v2.5.0.M2** - **EXPIRED** - 08/03/2019

- [FIX #29] Fixed (again) Automation command translation (1000#)
- [FIX] Fixed Energy Meter subscription (every 10min)
- [FIX] corrected deviceWhere address management for ZigBee devices and discovery of ON_OFF_SWITCH_2UNITS
- [FIX #54] CEN scenarios are now detected when activated from Touchscreens (added PRESSURE>RELEASE schedule)
- [FIX #56] Thermostat Cannot set setpoint temperature (now WHERE=N is first used, then WHERE=#N if it fails)
- [FIX #59] added discoverByActivation parameter (optional) to BUS gateway
- added FAQs section to README.md
- added ownId as representation-property
- added Switchable tag to shutter channel

**v2.5.0.M1** - **EXPIRED** - 28/01/2019

- [FIX #28] automatic discovery of BUS gateways is now supported
- gateway model, firmwareVersion and serialNumber are now read from UPnP discovery
- [FIX #39] set subscription interval for Energy Meter
- [FIX #4] added support for BTicino movement sensors (like AM5658 Green Switch) 
- updated to openHAB 2.5.0 dev branch

**v2.4.0-b9-2** - **EXPIRED** - 18/01/2019

- [FIX #37] CEN commands WHAT (buttons) 0-9 are now 00-09
- [FIX] CEN/CEN+ scenarioButton channel is now able to receive commands
- [FIX #45] Thermoregulation setpoint command refused
- [FIX #46] Thermo: activeMode displays only the state “Off" and "anti freeze”
- [FIX #42 & #43] Devices with same WHERE receive wrong messages from BUS
- [FIX] improved device registration to BridgeHandler

**v2.4.0-b9-1** - **EXPIRED** - 27/12/2018

- [FIX #6] and [FIX #33] Initial support for `WHO=15/25` CEN/CEN+ for receiving events from BUS Scenario Control physical devices/buttons (for example Scenario Control: HC/HD/HS/L/N/NT4680) and sending virtual pressure commands to activate MH202 scenarios on the BUS from openHAB
- [FIX #9] Support for `WHO=25` Dry Contact interfaces and IR Sensors on BUS, with discovery
- [FIX #11] Initial support for `WHO=18` Energy Management on BUS, with discovery. Currently supported: Energy Management Central Unit (F521) power measures
- [FIX #29] Added support for command translation (1000# ) for Automation
- [FIX #27] Device Discovery by Activation for Lighting and CEN/CEN+: if a BUS physical device is not found in Inbox during a Scan, activate the device to discover it

**v2.4.0-b8** - **EXPIRED** - 11/11/2018

- [FIX #25] added `Blinds` tag for shutter channels (Rollershutter items) 
- [FIX #17] now a disconnection from the gateway is detected within few minutes
- [FIX #10] now an automatic STOP command is sent when a new Position/UP/DOWN command is sent while already moving
- [FIX #18] at startup (for example after a power outage) the binding now tries periodically to connect to the BTicino Gateway
- [BUG] now a decimal setpointTemperature (21.5 °C) is sent as decimal to the thermostat and not as integer
- [FIX #20] added support for dimmerLevel100 levels when dimmers are changed from myhomescreen or touchscreen 
- [FIX #16] dimmers now save last state if switched off from openHAB
- [FIX] now Command Translation (1000#WHAT) is supported in Lighting
- [FIX] setpointTemperature now uses QuantityType (Number:Temperature item)
- updated README examples and added .sitemap example
- moved to new repository, forked from openhab/openhab2-addons

**v2.4.0-b7** - **EXPIRED** - 01/09/2018

- [FIX #5] Initial support for `WHO=4` Thermoregulation on BUS. Currently supported: zones room thermostats and external (wireless) temperature sensors. Both heating and cooling functions are supported
- The binding is now available on the [Eclipse IoT Marketplace](https://marketplace.eclipse.org/content/openwebnet-2x-binding-testing)
- [BUG] corrected a bad bug on the connection part (originally taken from the BTicino 1.x binding) that caused loosing many monitoring (feedback) messages from the gateway when several messages were received together
- [FIX #13] now commands sent close in a short time are sent re-using the same socket connection to the gateway. This should improve connection stability and speed in general and in particular with older BUS gateways (e.g. MH202)
- [FIX] improved Shutter management and position estimation, thanks to previous 2 enhancements
- minimum requirement is now openHAB 2.3, which is needed to support measurements units like °C

**v2.4.0-b6** - **EXPIRED** - 02/07/2018

- updated to openHAB 2.4.0 dev branch
- [FIX #7] added support for inverted UP/DOWN automation commands for older USB ZigBee dongles
- [BUG] some switches were wrongly discovered as dimmers (now use only commands for device discovery)
- [FIX] added support for SCS/ZIGBEE_SHUTTER_SWITCH (515/513) device types
- [FIX] added support for F455 gateways using `*99*0##` command session

**v2.3.0-b5** - **EXPIRED** - 26/05/2018

- [FIX #1] state monitoring from BUS (feedback) is no longer stopped if unsupported messages are received from BUS
- [FIX] automatic reconnect to BUS when connection is lost
- [NEW] support for Gateways with string passwords (HMAC authentication), like MyHOMEServer1 
- [NEW] support for `WHO=2` Automation (shutters), both on BUS and ZigBee, with position feedback and  goto Percent. It requires setting the shutter run-time in the thing configuration. Experimental auto-calibration of the run-time is also supported!

**v2.3.0-b4** - **EXPIRED** - 09/04/2018

- first public release