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
import org.w3c.dom.Document;

/**
 * Transformation context containing the document being processed and its
 * task definitions.
 * Note that the transformation jobs are applied sequentially and they directly 
 * modify the document present in this object.
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
