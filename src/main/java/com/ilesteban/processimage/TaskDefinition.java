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
