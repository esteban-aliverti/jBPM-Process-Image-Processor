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
package com.ilesteban.processimage.transformation;

import com.ilesteban.processimage.ProcessContext;
import com.ilesteban.processimage.TaskDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Modifies the border of one or more Tasks.
 * @author esteban
 */
public class TaskBorderTransformationJob implements TransformationJob{

    private static final Logger LOG = Logger.getLogger(TaskBorderTransformationJob.class.getName());
    
    private List<String> taskNames;
    private Integer strokeWidth = null;
    private String strokeColor = null;

    /**
     * Creates a new transformation job for modify the stroke color and with of
     * the border of a set of tasks.
     * @param taskNames The names of the tasks.
     * @param strokeColor the stroke color in hexadecimal. I.e: #AA00DD
     * @param strokeWidth the stroke width to use.
     */
    public TaskBorderTransformationJob(List<String> taskNames, String strokeColor, int strokeWidth) {
        this.taskNames = taskNames;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }
    
    /**
     * Creates a new transformation job for modify the stroke color of
     * the border of a set of tasks.
     * @param taskNames The names of the tasks.
     * @param strokeColor the stroke color in hexadecimal. I.e: #AA00DD
     */
    public TaskBorderTransformationJob(List<String> taskNames, String strokeColor) {
        this.taskNames = taskNames;
        this.strokeColor = strokeColor;
    }

    /**
     * Creates a new transformation job for modify the stroke color and with of
     * the border of a task.
     * @param taskName The name of the task.
     * @param strokeColor the stroke color in hexadecimal. I.e: #AA00DD
     * @param strokeWidth the stroke width to use.
     */
    public TaskBorderTransformationJob(String taskName, String strokeColor, int strokeWidth) {
        this.taskNames = new ArrayList<String>();
        this.taskNames.add(taskName);
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }
    
    /**
     * Creates a new transformation job for modify the stroke color of
     * the border of a task.
     * @param taskName The name of the task.
     * @param strokeColor the stroke color in hexadecimal. I.e: #AA00DD
     * @param strokeWidth the stroke width to use.
     */
    public TaskBorderTransformationJob(String taskName, String strokeColor) {
        this.taskNames = new ArrayList<String>();
        this.taskNames.add(taskName);
        this.strokeColor = strokeColor;
    }
    
    
    public void transform(ProcessContext context) {
        Document svgDocument = context.getSvgDocument();
        
        for (String taskName : taskNames) {
            TaskDefinition task = context.getTaskDefinitions().get(taskName);
            if (task != null){
                Element backgroundElement = svgDocument.getElementById(task.getBackgroundItemId());
                if (this.strokeWidth != null){
                    backgroundElement.setAttribute("stroke-width", String.valueOf(this.strokeWidth));
                }
                if (this.strokeColor != null){
                    backgroundElement.setAttribute("stroke", this.strokeColor);
                }
            }else{
                LOG.log(Level.WARNING, "{0} not found in process definition", taskName);
            }
        }
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }
    
}
