/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author esteban
 */
public class TaskColorTransformationJob implements TransformationJob{

    private static final Logger LOG = Logger.getLogger(TaskColorTransformationJob.class.getName());
    
    private List<String> taskNames;
    private String color;

    public TaskColorTransformationJob(List<String> taskNames, String color) {
        this.taskNames = taskNames;
        this.color = color;
    }
    
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
