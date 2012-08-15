/*
 * Copyright 2012 esteban.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilesteban.processimage;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class that may be used to modify certain aspects of the 
 * image processing.
 * @author esteban
 */
public class ProcessImageProcessorConfiguration {
    private float defaultextPad = 0;
    private Map<String,Float> taskSpecificTextPad = new HashMap<String, Float>();

    public float getDefaultextPad() {
        return defaultextPad;
    }

    /**
     * Sets the default pad to be used in the label of the tasks.
     * The SVG files created by JBPM Web Process Designer have a wrong pad in
     * the task names if the are multi-line. You can use this value to correct
     * that problem.
     * @param defaultextPad the default pad to use in the label of all the tasks.
     * The pad correction is applied starting from the second line of the label.
     */
    public void setDefaultextPad(float defaultextPad) {
        this.defaultextPad = defaultextPad;
    }

    public Float getTaskSpecificTextPad(String taskName) {
        if (this.taskSpecificTextPad.containsKey(taskName)){
            return this.taskSpecificTextPad.get(taskName);
        }
        return this.defaultextPad;
    }

    /**
     * Same as {@link #setDefaultextPad(float) } but for discriminating by task name.
     * @param taskSpecificTextPad
     * @return 
     */
    public void setTaskSpecificTextPad(Map<String, Float> taskSpecificTextPad) {
        this.taskSpecificTextPad = taskSpecificTextPad;
    }
    
}
