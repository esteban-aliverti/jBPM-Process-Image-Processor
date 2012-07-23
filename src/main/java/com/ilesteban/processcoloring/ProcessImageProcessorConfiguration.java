/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author esteban
 */
public class ProcessImageProcessorConfiguration {
    private float defaultextPad = 0;
    private Map<String,Float> taskSpecificTextPad = new HashMap<String, Float>();

    public float getDefaultextPad() {
        return defaultextPad;
    }

    public void setDefaultextPad(float defaultextPad) {
        this.defaultextPad = defaultextPad;
    }

    public Float getTaskSpecificTextPad(String taskName) {
        if (this.taskSpecificTextPad.containsKey(taskName)){
            return this.taskSpecificTextPad.get(taskName);
        }
        return this.defaultextPad;
    }

    public void setTaskSpecificTextPad(Map<String, Float> taskSpecificTextPad) {
        this.taskSpecificTextPad = taskSpecificTextPad;
    }
    
}
