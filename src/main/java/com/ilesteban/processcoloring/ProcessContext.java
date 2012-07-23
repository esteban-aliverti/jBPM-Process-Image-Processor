/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author esteban
 */
public class ProcessContext {
    
    private Document svgDocument;
    private Map<String, TaskDefinition> taskDefinitions = new HashMap<String, TaskDefinition>();

    public Document getSvgDocument() {
        return svgDocument;
    }

    public void setSvgDocument(Document svgDocument) {
        this.svgDocument = svgDocument;
    }

    public Map<String, TaskDefinition> getTaskDefinitions() {
        return taskDefinitions;
    }

    public void setTaskDefinitions(Map<String, TaskDefinition> taskDefinitions) {
        this.taskDefinitions = taskDefinitions;
    }
    
}
