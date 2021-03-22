//package com.microchip.mh3.plugin.generic_plugin_manager;
//
//import com.microchip.mh3.Core;
//import com.microchip.mh3.log.Log;
//import com.microchip.mh3.plugin.generic_plugin_manager.gui.MainScreen;
//import com.microchip.mh3.plugin.generic_plugin_manager.javafx.FxSupport;
//import com.microchip.mh3.windowmanager.WindowManager;
//import java.awt.event.ActionEvent;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.stage.WindowEvent;
//import javax.swing.JMenuItem;
//import org.yaml.snakeyaml.Yaml;
//
//public class Plugin extends com.microchip.mh3.plugin.Plugin {
//
//    private Path pluginJarPath = null;
//    private JMenuItem menuItem = null;
//    
//    public static String settingsYMlPath = null;
//    public Map<String, Object> settingsMap;
//    public static String pluginManagerName;
//    public String pluginVersion;
//    public static String mainHtmlPath;
//
//    public static String COMPONENT_ID ;
//    
//    MainScreen jfxBrowserStage;
//
//    @Override
//    public String getName() {
//        return pluginManagerName;
//    }
//
//    @Override
//    public String getVersion() {
//        return pluginVersion;
//    }
//
//    @Override
//    public boolean load(String pluginJarPath) {
//        if (Core.getInstance().getType() == Core.Type.Headless) {
//            return true;
//        }
//
//        FxSupport.enable();
//
//        this.pluginJarPath = Paths.get(pluginJarPath);
//        
//        settingsYMlPath = getSettingsYMLPath(pluginJarPath);
//        updateInputData(settingsYMlPath);
//
//        menuItem = new JMenuItem(pluginManagerName);
//        menuItem.addActionListener(this::openManagerAsWindow);
//        WindowManager.getInstance().addPluginLauncher(menuItem);
//
//        Log.write(pluginManagerName, Log.Severity.Info, "loading "+pluginManagerName+" plugin", Log.Level.USER);
//        return true;
//    }
//
//    @Override
//    public boolean unload() {
//        if (Core.getInstance().getType() == Core.Type.Headless) {
//            return true;
//        }
//        
//        Platform.runLater(() -> {
//            if (jfxBrowserStage != null) {
//                jfxBrowserStage.clearObjects();
//            }
//        });
//
//        WindowManager.getInstance().removePluginLauncher(menuItem);
//
//        Log.write(pluginManagerName, Log.Severity.Info, "unloading "+pluginManagerName+" plugin", Log.Level.USER);
//        return true;
//    }
//    
//    private String getSettingsYMLPath(String path){
//        path = path.substring(0,path.lastIndexOf(File.separator));
//        File[] files = (new File(path)).listFiles();
//        for(File file: files){
//            if(file.getName().equalsIgnoreCase("settings.yml")){
//                try {
//                    return file.getCanonicalPath();
//                } catch (IOException ex) {
//                    logError();
//                }
//            }
//        }
//        return null;
//    }
//    
//    private void updateInputData(String ymlFilePath){
//         InputStream inputStream = null;
//        try {
//            Yaml yaml = new Yaml();
//            inputStream = new FileInputStream(Paths.get(ymlFilePath).toFile());
//            settingsMap = yaml.load(inputStream);
//            pluginManagerName = getSettingsValue("plugin_name", settingsMap);
//            pluginVersion = getSettingsValue("plugin_version", settingsMap);
//            COMPONENT_ID = getSettingsValue("component_id", settingsMap);
//            mainHtmlPath = getSettingsValue("main_html_path", settingsMap);
//        } catch (FileNotFoundException ex) {
//            logError();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException ex) {
//                logError();
//            }
//        }
//    }
//    
//    private String getSettingsValue(String key, Map<String, Object> map){
//        if(map.containsKey(key)){
//            return (String)map.get(key);
//        }
//        return null;
//    }
//    
//    private void logError(){
//        Log.write(pluginManagerName, Log.Severity.Warning, 
//                        "Failed to open "+pluginManagerName+". Load plugin before opening "+pluginManagerName, Log.Level.DEBUG);
//    }
//
//    private void openManagerAsWindow(ActionEvent event) {
//        Platform.runLater(this::createAndShowStage);
//    }
// 
//    private Stage stage = null;
//
//    private void createAndShowStage() {
//        if (stage == null) {
//            stage = new Stage();
//            stage.setScene(createDashboard());
//            stage.setTitle(pluginManagerName);
//            stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
//                this.stage = null;
//            });
//        }
//        stage.show();
//        stage.toFront();
//    }
//
//    private Scene createDashboard() {
//        jfxBrowserStage = new MainScreen(stage, settingsMap);
//        return jfxBrowserStage.getScene(mainHtmlPath);
//    }
//}
