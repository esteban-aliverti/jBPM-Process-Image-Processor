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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Modifies the value of one or more attributes of one or more elements if 
 * its value is 'undefined'.
 * @author esteban
 */
public class UndefinedValueForAttributeTransformationJob implements TransformationJob {

    private Map<String,Map<String,String>> tagsToProcess = new HashMap<String, Map<String, String>>();
    
    public void transform(ProcessContext context) {
        for (Entry<String, Map<String, String>> tag : tagsToProcess.entrySet()) {
            NodeList elementsByTagName = context.getSvgDocument().getElementsByTagName(tag.getKey());
            if (elementsByTagName != null){
                for (int i = 0; i < elementsByTagName.getLength(); i++) {
                    Node element = elementsByTagName.item(i);
                    if (element != null){
                        for (Entry<String, String> attribute : tag.getValue().entrySet()) {
                            Node attr = element.getAttributes().getNamedItem(attribute.getKey());
                            if (attr != null && attr.getNodeValue().equalsIgnoreCase("UNDEFINED")){
                                attr.setNodeValue(attribute.getValue());
                            }
                        }
                    }   
                }
            }
        }
    }

    /**
     * Adds a new attribute to be processed.
     * @param tagName The tag name we are interested in. I.e: image, text, etc.
     * @param attributeName The name of the attribute we want to analyze. If the
     * value of this attribute is 'undefined' it is going to be replaced by the
     * value specified in 'value' parameter.
     * @param value The value you want to set to 'attributeName' if its value
     * is 'undefined'.
     */
    public void addAttributeToProcess(String tagName, String attributeName, String value){
        if (!this.tagsToProcess.containsKey(tagName)){
            this.tagsToProcess.put(tagName, new HashMap<String, String>());
        }
        this.tagsToProcess.get(tagName).put(attributeName, value);
    }
    
    
}
