# Harmony Services

Harmony Services repository is required to launch  Harmony **package**/**component**  HTML based plugins. It contains common **Harmony generic plugin** and **Harmony file server** java libraries required for HTML based plugins.

This package contains both harmony service binaries and along with its implementation source code.

## Harmony Services Package Setup

- Install/Update MPLAB X 6.00 or later
- Install/Update MCC MPLAB X Plugin
- Update MCC Harmony3Library to its latest release using Content Manager
- Download/Update the harmony-services from Content Manager
- After configuring above setup, now Harmony Services is ready to launch package/component HTML based plugins.

## Contents Summary

| File/Folder           | Description                                               |
|---|---|
| docs                   | Help documentation and licenses for libraries used        |
| plugin_source          | Binaries source code available            |
| plugin/browser_engine             | This binary helps to provide HTML Engine/Browser instances to view HTML Content.                 |
| plugin/generic_plugin               | This binary is generic and used to lauch multiple package/component plugins dynamically through external input package configuration yml file.                |
| plugin/http_file_server                 | This binary serves Harmony Framework files.                      |
| plugin/lib               | Lib folder contains all required dependency jars.          |

## Open source Libraries

Harmony Services binaries uses following open sources libraries:

| Library Name                                  | Version                    | License                                                                                               |
|-----------------------------------------------|---------------------------|-------------------------------------------------------------------------------------------------------|
|[jetty-http-9.4.8.v20171121.jar](https://mvnrepository.com/artifact/org.eclipse.jetty/jetty-server/9.4.8.v20171121)                     | 9.4.8.v20171121             |             Apache 2.0, EPL 1.0 [http://www.apache.org/licenses/LICENSE-2.0.txt](http://www.apache.org/licenses/LICENSE-2.0.txt)

____

[![License](https://img.shields.io/badge/license-Harmony%20license-orange.svg)](https://github.com/Microchip-MPLAB-Harmony/harmony-services/blob/master/mplab_harmony_license.md)
[![Latest release](https://img.shields.io/github/release/Microchip-MPLAB-Harmony/harmony-services.svg)](https://github.com/Microchip-MPLAB-Harmony/harmony-services/releases/latest)
[![Latest release date](https://img.shields.io/github/release-date/Microchip-MPLAB-Harmony/harmony-services.svg)](https://github.com/Microchip-MPLAB-Harmony/harmony-services/releases/latest)
[![Commit activity](https://img.shields.io/github/commit-activity/y/Microchip-MPLAB-Harmony/harmony-services.svg)](https://github.com/Microchip-MPLAB-Harmony/harmony-services/graphs/commit-activity)
[![Contributors](https://img.shields.io/github/contributors-anon/Microchip-MPLAB-Harmony/harmony-services.svg)]()
____

[![Follow us on Youtube](https://img.shields.io/badge/Youtube-Follow%20us%20on%20Youtube-red.svg)](https://www.youtube.com/user/MicrochipTechnology)
[![Follow us on LinkedIn](https://img.shields.io/badge/LinkedIn-Follow%20us%20on%20LinkedIn-blue.svg)](https://www.linkedin.com/company/microchip-technology)
[![Follow us on Facebook](https://img.shields.io/badge/Facebook-Follow%20us%20on%20Facebook-blue.svg)](https://www.facebook.com/microchiptechnology/)
[![Follow us on Twitter](https://img.shields.io/twitter/follow/MicrochipTech.svg?style=social)](https://twitter.com/MicrochipTech)

[![](https://img.shields.io/github/stars/Microchip-MPLAB-Harmony/mhc.svg?style=social)]()
[![](https://img.shields.io/github/watchers/Microchip-MPLAB-Harmony/mhc.svg?style=social)]()
