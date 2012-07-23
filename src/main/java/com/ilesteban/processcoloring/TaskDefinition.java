/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring;

/**
 *
 * @author esteban
 */
public class TaskDefinition {
    private String textItemId;
    private String backgroundItemId;
    private String frameItemId;
    private String taskName;

    public TaskDefinition(String textItemId, String backgroundItemId, String frameItemId) {
        this.textItemId = textItemId;
        this.backgroundItemId = backgroundItemId;
        this.frameItemId = frameItemId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    

    public String getTextItemId() {
        return textItemId;
    }

    public String getBackgroundItemId() {
        return backgroundItemId;
    }

    public String getFrameItemId() {
        return frameItemId;
    }

    
}
