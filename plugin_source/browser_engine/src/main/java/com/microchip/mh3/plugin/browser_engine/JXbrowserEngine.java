/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.browser_engine;

import com.microchip.mcc.core.service.ServiceFactory;
import com.microchip.mh3.log.Log;
import org.openide.util.Lookup;
import com.teamdev.jxbrowser.browser.Browser;

import com.microchip.mplab.browser.libs.spi.BrowserService;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.Language;
import com.teamdev.jxbrowser.engine.RenderingMode;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class JXbrowserEngine {

    String[] mccJXBrowserLicenseKeys = {"1BNDIEOFAYZM808B87BLSR7SM305VUHKEDWRYFF0E60GWUZPK3O6UXUVK8T7RLAESEAGM5", 
        "XX76YAAC00SJXZWIK48B4LF3II254JQ35936J950M7UTCXMLAVM5DVA9X3LRJ5N8UT"}; // This will be used only in MCC Standalone mode
    boolean IsMPLABXMode = false;
    Engine standAloneEngine;

    static ArrayList<Path> usedJXBrowserDirectories = new ArrayList<>();
    static String parentPath = Paths.get(System.getProperty("user.home"), ".harmony_jxbrowser").toFile().getAbsolutePath();
    static String folderName = ".jxbrowser";

    public Path getPath() {
        int i = 1;
        String str = folderName;
        Path path = null;
        while (i > 0) {
            path = Paths.get(parentPath, str + i);
            if (path.toFile().exists() && usedJXBrowserDirectories.contains(path)) {
                i++;
            } else {
                break;
            }
        }
        usedJXBrowserDirectories.add(path);
        return path;
    }

    private int getDebugPort() {
        return 9222;
    }

    public Browser getBrowserInstance(String pluginID, boolean bDebugStatus) {
        try {
            if (ServiceFactory.getDevelopementEnvironmentService().isPresent()) {
                BrowserService svc = Lookup.getDefault().lookup(BrowserService.class);

                if (svc != null) {
                    folderName = ".jxbrowser_mplabx";
                    Path path = getPath();
                    IsMPLABXMode = true;
                    if (bDebugStatus) {
                        EngineOptions.Builder builder = EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                                // The language used on the default error pages and GUI.
                                .language(Language.ENGLISH_US)
                                .remoteDebuggingPort(getDebugPort())
                                .userDataDir(getPath());
                        return svc.getBrowser(pluginID, path, builder, Optional.of((event, browser) -> {
                            Log.write("JXBrowser", Log.Severity.Error, "Browser {0} crashed for reason {1}", Log.Level.USER);
                        }));
                    } else {
                        return svc.getBrowser(pluginID, path, Optional.of((event, browser) -> {
                            Log.write("JXBrowser", Log.Severity.Error, "Browser {0} crashed for reason {1}", Log.Level.USER);
                        }));
                    }

                }
            } else {
            }
            return getBrowserInstanceInMCCStandAlone(bDebugStatus);
        } catch (Exception ex) {
            Log.write(pluginID, Log.Severity.Error, "Unable to access MPLABX JXbrowsers APIs. Please install latest MPLAB X version.", Log.Level.USER);
            Log.printException(ex);
            return getBrowserInstanceInMCCStandAlone(bDebugStatus);
        }
    }

    public void disPoseBrowserEvent(String PluginID) {
        if (IsMPLABXMode) {
            BrowserService svc = Lookup.getDefault().lookup(BrowserService.class);
            svc.disposeBrowser(PluginID);
        } else if (standAloneEngine != null) {
            standAloneEngine.close();
        }
    }

    private Browser getBrowserInstanceInMCCStandAlone(boolean bDebugStatus) {
        folderName = ".jxbrowser_standalone";
        Log.write("MCCHarmony", Log.Severity.Info, "MCC Harmony Standloane Mode", Log.Level.USER);

        EngineOptions.Builder builder = EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
                // The language used on the default error pages and GUI.
                .language(Language.ENGLISH_US)
                .userDataDir(getPath());
        if (bDebugStatus) {
            builder.remoteDebuggingPort(getDebugPort());
        }

        for (String licenseKey : mccJXBrowserLicenseKeys) {
            builder.licenseKey(licenseKey);
            try {
                standAloneEngine = Engine.newInstance(
                        builder
                                .build());
                if (standAloneEngine != null) {
                    return standAloneEngine.newBrowser();
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static void deleteBrowserTempFiles() {
        usedJXBrowserDirectories.clear();
        File fileLocation = Paths.get(parentPath).toFile();
        if (fileLocation.exists()) {
            File[] files = fileLocation.listFiles();
            for (File file : files) {
                if (checkName(file.getName())) {
                    continue;
                }
                deleteDirectory(file);
            }
        }
    }

    public static boolean checkName(String targetName) { // Always ignore 5 directories to load plugins faster...
        for (int i = 1; i < 5; i++) {
            String tempName = folderName + i;
            if (tempName.equals(targetName)) {
                return true;
            }
        }
        return false;
    }

    private static void deleteDirectory(File directory) {
        try {
            if (directory.exists()) {
                File[] files = directory.listFiles();

                if (null != files) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteDirectory(file);
                        }
                        file.delete();
                    }
                }
                directory.delete();
            }
        } catch (Exception ex) {
            Log.write("MCCHarmony", Log.Severity.Error, "Failed to delete jxbrowser temp files.", Log.Level.USER);
            Log.printException(ex);
        }
    }

    public void removeUsedUserDirectory(Path dir) {
        usedJXBrowserDirectories.remove(dir);
    }
}
