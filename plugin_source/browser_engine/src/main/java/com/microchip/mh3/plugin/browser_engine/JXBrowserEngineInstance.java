/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.browser_engine;

import com.microchip.mh3.log.Log;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JXBrowserEngineInstance {

    private static Engine engine;
    static String parentPPath = Paths.get(System.getProperty("user.home"), ".harmony_jxbrowser").toFile().getAbsolutePath();

    public static Path getPath() {
        int i = 1;
        String str = ".jxbrowser";
        Path path;
        while (i > 0) {
            path = Paths.get(parentPPath, str + i);
            if (path.toFile().exists()) {
                i++;
            } else {
                return path;
            }
        }
        return Paths.get(parentPPath, str);
    }

    public static Engine getEngine() {
        if (engine == null) {
           createEngineInstatnce();
        }
        return engine;
    }
    
    public static void createEngineInstatnce() {
        File path = new File(parentPPath);
        if (!path.exists()) {
            path.mkdir();
        }
        engine = Engine.newInstance(
                EngineOptions.newBuilder(HARDWARE_ACCELERATED)
                        .chromiumDir(getPath())
                        .licenseKey("1BNDIEOFAZ382DH0EH1COZD7XH83RSKTK98G22WYHUUKZLUFZ8GUSOGTBXXK15GPLQLO02")
                        .build());
    }

    public static void CloseEngine() {
        if (engine != null) {
            engine.close();
            Log.write("JXbrowser Engine", Log.Severity.Warning,
                    "JXbrowser Engine close status : " + engine.isClosed(), Log.Level.DEBUG);
            engine = null;
        }
        deleteDirectory(Paths.get(parentPPath).toFile());
    }

    public static void deleteDirectory(File directory) {
//        new Thread(() -> {
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
                }
            } catch (Exception ex) {
                //
            }
//        }).start();
    }
}
