/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.microchip.mh3.plugin.generic_plugin.gui.tools;


public class Utils {
     public static StringBuilder getArrayValuesAsString(StringBuilder builder, String[] comboValues) {
        for (int i = 0; i < comboValues.length; i++) {
            builder.append(comboValues[i]);
            if (i != comboValues.length - 1) {
                builder.append("M*C");
            }
        }
        return builder;
    }
}
