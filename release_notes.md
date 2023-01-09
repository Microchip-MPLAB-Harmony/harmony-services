# Harmony Services Release Notes

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
