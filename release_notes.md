# MPLAB® Harmony Services Release Notes

## MPLAB® Harmony Services Release v1.3.4

### Bug fixes
- Fixed temporarily package.yml file which is causing issue with MCC Content Manager at the moment.

### Known Issues
- None

### Development Tools
- [MPLAB® X IDE v6.20 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.5.1, MPLAB® X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.5.1 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## MPLAB® Harmony Services Release v1.3.3

### Bug fixes
- MPLAB® Harmony plugin launch issue in MCC Standalone mode fixed.
- Added package.yml file to harmony-services repository.

### Known Issues
- None

### Development Tools
- [MPLAB® X IDE v6.20 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.5.1, MPLAB® X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.5.1 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## Harmony Services Release v1.3.2

### Bug fixes
- Reverted harmony-services getSymbolData api return type Object to String to support old html plugins.
- Added new API to getSymbolInfo as Object type to support new html plugins.

### Known Issues
- None

### Development Tools
- [MPLAB® X IDE v6.10 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.3.7, MPLAB® X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.3.2 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## Harmony Services Release v1.3.1

### Bug fixes
- Fixed bugs.

### Known Issues
- None

### Development Tools
- [MPLAB X IDE v6.10 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.3.7, MPLAB X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.3.2 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## Harmony Services Release v1.3.0

### Bug fixes
- Added component activation/deactivation and sendMessage API support to generic plugin.
- Added support to get getSymbolDescription and getSymbolDefaultValue.
- Added support to update multiple symbols and skipping symbol update if existing symbol value and update value is same.
- Fixed minor bugs.

### Known Issues
- None

## Harmony Services Release v1.2.1

### Bug fixes
- Fixed minor bugs.

### Known Issues
- None

## Harmony Services Release v1.2.0

### Bug fixes
- Implemented log APIs to show browser console messages to mcc logging window.
- Introduced APIs to export init args for the plugin config.
- Added support to debug the MPLABX JxBrowser browser instance by providing debug attribute in plugin init args.
- Added APIs to get trustzone enable status and symbol readonly status.
- Fixed security issues reported by github security dependabot.
- Fixed minor bugs.

### Known Issues
- None

### Development Tools
- [MPLAB X IDE v6.05 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.2.2, MPLAB X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.2.0 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## Harmony Services Release v1.1.1

### Bug fixes
- Fixed backslahes in Harmony package plugin paths (configuration file).

### Known Issues
- None

### Development Tools
- [MPLAB X IDE v6.00 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [MPLAB® Code Configurator (MCC) v5.1.4, MPLAB X plugin](https://www.microchip.com/en-us/tools-resources/configure/mplab-code-configurator)
- [Harmony3Librabry v1.1.1 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony). Obtained from MCC Content Manager.

## Harmony Services Release v1.1.0

### New Features
- Added symbol listener support to update HTML Plugin managers dynamically based on Harmony component state or Tree view changes.
- Added support to help users to identify if any required resources like file server, jxbrowser binaries and dependent react files are missing from local framework.

### Known Issues
- None

### Development Tools
- [MPLAB X IDE v6.00 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [Harmony3Librabry 1.1.0 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony)

## Harmony Services Release v1.0.0

### New Features
- Harmony Generic Plugin implemented to launch package/component HTML based Plugins.
- Implemented HTTP file server to host harmony framework files.

### Known Issues
- None

### Development Tools
- [MPLAB X IDE v6.00 or Later](https://www.microchip.com/mplab/mplab-x-ide)
- [Harmony3Librabry 1.0.8 or Later](https://www.npmjs.com/package/@mchp-mcc/harmony)
