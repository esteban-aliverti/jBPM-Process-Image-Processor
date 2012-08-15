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
 * Changes the background color of one or more tasks.
 * @author esteban
 */
public class TaskColorTransformationJob implements TransformationJob{

    private static final Logger LOG = Logger.getLogger(TaskColorTransformationJob.class.getName());
    
    private List<String> taskNames;
    private String color;

    /**
     * Creates a new transformation job to change the background color of a set
     * of tasks.
     * @param taskNames The names of the tasks.
     * @param color The desired background color in hexadecimal. I.e: #AA00DD
     */
    public TaskColorTransformationJob(List<String> taskNames, String color) {
        this.taskNames = taskNames;
        this.color = color;
    }
    
    /**
     * Creates a new transformation job to change the background color of a task.
     * @param taskNames The name of the task.
     * @param color The desired background color in hexadecimal. I.e: #AA00DD
     */
    public TaskColorTransformationJob(String taskName, String color) {
        this.taskNames = new ArrayList<String>();
        this.taskNames.add(taskName);
        this.color = color;
    }
    
    
    public void transform(ProcessContext context) {
        Document svgDocument = context.getSvgDocument();
        
        for (String taskName : taskNames) {
            TaskDefinition task = context.getTaskDefinitions().get(taskName);
            if (task != null){
                Element backgroundElement = svgDocument.getElementById(task.getBackgroundItemId());
                backgroundElement.setAttribute("fill", this.color);
            }else{
                LOG.log(Level.WARNING, "{0} not found in process definition", taskName);
            }
        }
    }
    
    
}
