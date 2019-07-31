# OpenWebNet2-OpenHab-PRE
This repository starts from the OpenWebNet-OpenHab v2.5.0.M2-2

### Index
- [Bus and Channels](#bus-and-channels)
- [Changelog](#changelog)
- [Example Group Command](#example-group-command)
- [Example Bus Command](#example-bus-command)
- [Example Bus Motion Detector](#example-bus-motion-detector)
- [Example Bus Lighting with parameter what](#example-bus-lighting-with-parameter-what)
- [Test Lighting group addresses](#test-lighting-group-addresses)
- [Test Automation group addresses](#test-automation-group-addresses)

### Bus and Channels

Devices support some of the following channels:

| Bus Name          |  Description bus       |Parameter</br>Type</br>Description       |Channel</br>Type (Read/Write)</br>Description | 
|-------------------|------------------------|-------------------------|--------------------------|
|`bus_on_off_switch`   |WHO=1 Lightning |`where`</br>String</br>Value where OWN</br>`what`</br>Integer (default = 0)</br>Value what OWN</br>`addrtype`</br>Integer (default = 1)</br>Address type 1= Point to Point, 2= Area, 3= Group, 4= General</br>`hour`</br>Integer (default = 0)</br>Timer in hour</br>`minute`</br>Integer (default = 0)</br>Timer in minute</br>`second`</br>Integer (default = 10)</br>Timer in second| `switch`</br>Switch (R/W)</br>To switch the device ON/OFF                |
|`bus_automation`   |WHO=2 Automation |`where`</br>String</br>Value where OWN</br>`addrtype`</br>Integer (default = 1)</br>Address type 1= Point to Point, 2= Area, 3= Group, 4= General| `shutterPosition`</br>Rollershutter (R/W)</br>To activate roller shutters (UP, DOWN, STOP, Percent)</br>`shutterMotion`</br>Integer (R)</br>Roller shutter movement 0= stop, 1= up, 2= down               |
|`bus_on_off_aux`   |WHO=9 command auxiliary |`where`</br>String</br>Value where OWN| `switch`</br>Switch (R/W)</br>To switch the device ON/OFF                |  
|`bus_motion_detector`   |WHO=1 motion decector</br>(movement and lux value detected) |`where`</br>String</br>Value where OWN</br>`automaticToOff`</br>Boolean</br>Automatic `OFF` after 2 seconds (default=false)| `switch`</br>Switch (R/W)</br>To switch the device ON/OFF</br>`value`</br>Integer (R)</br>Detected lux value |
|`bus_command`   |command management |`who`</br>String</br>Value who OWN</br>`what`</br>String</br>Value what OWN for ON</br>`whatOff`</br>String</br>Value what OWN for OFF</br>`compare`</br>String</br>Code OWN for comparison|  `switch`</br>Switch (R/W)</br>To switch the device ON/OFF</br>`contact`</br>Switch (R/W)</br>To switch the device OPEN/CLOSE</br>`what`</br>String (W)</br>Set the value what OWN               |  

### Changelog

**v2.5.0.M3.pre12** 31/07/2019

- Bug fix
  - [FIX [#80](https://github.com/mvalla/openhab2-addons/issues/80)] thermo: setmode in MANUAL not work
  - FIX bug `normalizeWhere` for group command
  - FIX bug Lib 
  - Temporary modification to the `message.Lighting`, correction for group command management for different `Lighting.Type`
  - Temporary modification to the `message.Automation`, correction for group command management for different `Automation.Type`

- New features
  - [FIX [#12](https://github.com/mvalla/openhab2-addons/issues/12) and [#32](https://github.com/mvalla/openhab2-addons/issues/32)] Add bus AUX for managing auxiliary controls (WHO = 9)
  - [FIX [#63](https://github.com/mvalla/openhab2-addons/issues/63)] Rollershutter items do not track changes in movement from external commands
    - *[BREAKING CHANGE]* channel `shutter` changed the name to `shutterPosition`
    - New channel `shutterMotion` (`0`= stop, `1`= up, `2`= down) 
  - [FIX [#69](https://github.com/mvalla/openhab2-addons/issues/69)] Updating of article statuses, lighting and automation, with the received AMB-GR-GEN commands.
  - [Issue [#35](https://github.com/mvalla/openhab2-addons/issues/35)] Support for group addresses (WHERE=#n)
    - For lighting and automation
    - New parameter `addrtype` (`1`= Point to Point, `2`= Area, `3`= Group, `4`= General)
    - See the tables [Lighting group addresses](#test-lighting-group-addresses) and [Automation group addresses](#test-automation-group-addresses)
  - Temporary modification to the `message.Lighting`
    - Added management for new timed WHAT
    - Added management for `bus_motion_detector`
  - [Issue [#79](https://github.com/mvalla/openhab2-addons/issues/79)] Add what parameter to lighting for `bus_on_off_switch` and `zb_on_off_switch`
    - Added parameters `what` (default `what`= 0), `hour`, `minute` and `second`
  - Add bus `bus_motion_detector` for motion decector (movement and lux value detected)
    - Tested with type `BMSE-3003`, `BMSE-1001` and `048834` 
    - Request lux value
    - Parameter `where` and `automaticToOff`
    - Channel `switch` state `ON` to the movement and after 2 seconds automatically `OFF`
    - Channel `value` numeric with detected value lux
  - [FIX [#52](https://github.com/mvalla/openhab2-addons/issues/52) and [#22](https://github.com/mvalla/openhab2-addons/issues/22)] Add bus `bus_command`
  - Translation Italian

### Example Group Command

**openwebnet.things:**

```xtend
Bridge openwebnet:bus_gateway:mybridge "MyHOMEServer1" [host="192.168.1.35" , passwd="abcde" , port=20000 , discoveryByActivation=true]
{  
      bus_on_off_switch  MygroupArea    "Area 2"	 [where="2" , what=0 , addrtype=2]
      bus_on_off_switch  MygroupGroup   "Group 2"	 [where="#2" , what=0 , addrtype=3]
      bus_on_off_switch  MygroupGen     "General"  [where="0" , what=0 , addrtype=4]
}
``` 

**openwebnet.items:**

```xtend
Switch   ILR_testArea1   "Area 2"   {channel="openwebnet:bus_on_off_switch:mybridge:MygroupArea:switch"}
Switch   ILR_testGroup1  "Group 2"  {channel="openwebnet:bus_on_off_switch:mybridge:MygroupGroup:switch"}
Switch   ILR_testGen1    "General"  {channel="openwebnet:bus_on_off_switch:mybridge:MygroupGen:switch"}

```

**openwebnet.sitemap**

```xtend
sitemap openwebnet label="OpenWebNet Binding Example Sitemap"
{
   Frame label="Test AMB Lighing" 
   {     
      Switch item=ILR_testArea1     label="Area 2"     mappings=[ON="ON"]
      Switch item=ILR_testArea1     label="Area 2"     mappings=[OFF="OFF"]		   
   }
   Frame label="Test GROUP Lighing" 
   {     
      Switch item=ILR_testGroup1    label="Group 2"    mappings=[ON="ON"]
      Switch item=ILR_testGroup1    label="Group 2"    mappings=[OFF="OFF"]		   
   }
   Frame label="Test GEN Lighing"
   {
      Switch item=ILR_testGen1      label="General"    mappings=[ON="ON"]
      Switch item=ILR_testGen1      label=General"     mappings=[OFF="OFF"]		
   }
}

```

### Example Bus Command

**openwebnet.things:**

```xtend
Bridge openwebnet:bus_gateway:mybridge "MyHOMEServer1" [host="192.168.1.35" , passwd="abcde" , port=20000 , discoveryByActivation=true]
{  
      bus_command   Mycomm     "Command"       [who="1" , what="1" , where="22" , whatOff="0" , compare=""]
      bus_command   Mycomm1    "Command 1"     [who="1" , what="1" , where="21" , whatOff="0" , compare="*1*1*23##"]
      bus_command   Mycomm2    "Command 2"     [who="2" , what="1" , where="81" , whatOff="0" , compare="*1*1*23##"]
}
``` 

**openwebnet.items:**

```xtend
String       iMyCommand       {channel="openwebnet:bus_command:mybridge:Mycomm:what"}
Switch       iMyCommand1      {channel="openwebnet:bus_command:mybridge:Mycomm:switch"}
Contact      iMyCommand2      {channel="openwebnet:bus_command:mybridge:Mycomm:contact"}

```

**openwebnet.sitemap**

```xtend
sitemap openwebnet label="OpenWebNet Binding Example Sitemap"
{
   Frame label="Command" 
   {
     Text 	  item=iMyCommand   label="What [%s]"       icon="door"
     Switch   item=iMyCommand1  label="Switch [%s]"     icon="light"
     Text 	  item=iMyCommand2  label="Contact [%s]"    icon="door"
     Switch   item=iMyCommand1  label="da Button [%s]"  icon="light"  mappings=[ON="ON" , OFF="OFF"]     
   }
}

```

**openwebnet.rules**

```xtend
// Scenario: Setting channel what
rule "testCommand"
when
        Item Luce_entrata changed 
then
        switch(Luce_entrata.state ) {
            case ON: {
               logInfo("Test rules testCommand", "success! ON" )
               iMyCommand.sendCommand("11")
            }
            case OFF: {
               logInfo("Test rules testCommand", "success! OFF" )
               iMyCommand.sendCommand("0")
            }
        }
           
end

```

### Example Bus Motion Detector

**openwebnet.things:**

```xtend
Bridge openwebnet:bus_gateway:mybridge "MyHOMEServer1" [host="192.168.1.35" , passwd="abcde" , port=20000 , discoveryByActivation=true]
{  
      bus_motion_detector   Mymotion     "Motion"       [where="22" , automaticToOff=false]
}
``` 

**openwebnet.items:**

```xtend
Switch       iMymotion      {channel="openwebnet:bus_command:mybridge:Mymotion:switch"}
Number       iMymotion      {channel="openwebnet:bus_command:mybridge:Mymotion:value"}

```

### Example Bus Lighting with parameter what

**openwebnet.things:**

```xtend
Bridge openwebnet:bus_gateway:mybridge "MyHOMEServer1" [host="192.168.1.35" , passwd="abcde" , port=20000 , discoveryByActivation=true]
{  
      bus_on_off_switch   Mylight    "Light"   [where="22" , what=17 , addrtype=1]
      bus_on_off_switch   Mylight1   "Light"   [where="23" , what=99 , hour=0 , minute=3 , second=30 , addrtype=1]
}
``` 

**openwebnet.items:**

```xtend
Switch       iMylight      {channel="openwebnet:bus_command:mybridge:Mylight:switch"}
Switch       iMylight1     {channel="openwebnet:bus_command:mybridge:Mylight1:switch"}
``` 

| what   |Description               |  
| ------ |--------------------------| 
|0       | Turn on/off (default) |
|11      | ON timed 1 Min |
|12      | ON timed 2 Min |
|13      | ON timed 3 Min |
|14      | ON timed 4 Min |
|15      | ON timed 5 Min |
|16      | ON timed 15 Min |
|17      | ON timed 30 Sec |
|18      | ON timed 0.5 Sec |
|20      | Blinking on 0.5 Sec |
|21      | Blinking on 1 Sec |
|22      | Blinking on 1.5 Sec | 
|23      | Blinking on 2 Sec |
|24      | Blinking on 2.5 Sec |
|25      | Blinking on 3 Sec |
|26      | Blinking on 3.5 Sec |
|27      | Blinking on 4 Sec |
|28      | Blinking on 4.5 Sec |
|29      | Blinking on 5 Sec |
|99      | Custom |

### Test Lighting group addresses
last test upgrade pre7

| Group Command | Discovery Bus Principale | Bus Principal      | Discovery Bus Local | Bus Local      | Note                    | 
| ------------- |:------------------------:|:------------------:|:-------------------:| :------------: | ----------------------- |
| Area          |`OK`                      | `OK`               | `OK`                | `Problem (1)`  | **`(1)`**  requestTurnOn and requestTurnOff invalid integer format      |
| Group         |`OK`                      | `Problem (1)`      | `OK`                | `Problem (1)`  | **`(1)`**  requestTurnOn and requestTurnOff invalid integer format      |
| General       |`OK`                      | `OK (2)`           | `OK`                | `Problem (1)`  | **`(1)`**  requestTurnOn and requestTurnOff allowed value [0]</br>**`(2)`** By sending the command, the group and area handlers are also called, `correct ???`

### Test Automation group addresses
last test upgrade pre7

| Group Command | Discovery Bus Principale | Bus Principal    | Discovery Bus Principale | Bus Local      | Note                      | 
| ------------- |:------------------------:| :--------------: |:------------------------:| :------------: | ------------------------- |
| Area          | `OK`                     | `OK`             | `OK`                     | `Problem (1)`  | **`(1)`**  requestMoveUp and requestMoveDown invalid integer format                           |
| Group         | `OK`                     | `Problem (1)`    | `OK`                     | `Problem (1)`  | **`(1)`**  requestMoveUp and requestMoveDown invalid integer format   |
| General       | `OK`                     | `OK (2)`         | `OK`                     | `Problem (1)`  | **`(1)`**  requestMoveUp and requestMoveDown allowed value [0]</br>**`(2)`** By sending the command, the group and area handlers are also called, `correct ???`   |

