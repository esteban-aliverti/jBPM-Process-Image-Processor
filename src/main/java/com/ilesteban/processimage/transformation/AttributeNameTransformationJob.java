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
import org.apache.batik.dom.GenericAttr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Changes the name of one of more attributes in one or more elements.
 * For example, you can modify 'href' attribute of this image 
 * <pre>{@code 
 * <image href="..."/>
 * }</pre>to add a namespace
 * <pre>{@code 
 * <image xlink:href="..."/>
 * }</pre>
 * @author esteban
 */
public class AttributeNameTransformationJob implements TransformationJob {

    private Map<String,Map<String,String>> tagsToProcess = new HashMap<String, Map<String, String>>();
    
    public void transform(ProcessContext context) {
        for (Map.Entry<String, Map<String, String>> tag : tagsToProcess.entrySet()) {
            NodeList elementsByTagName = context.getSvgDocument().getElementsByTagName(tag.getKey());
            if (elementsByTagName != null){
                for (int i = 0; i < elementsByTagName.getLength(); i++) {
                    Node element = elementsByTagName.item(i);
                    if (element != null){
                        for (Map.Entry<String, String> attribute : tag.getValue().entrySet()) {
                            Node attr = element.getAttributes().getNamedItem(attribute.getKey());
                            if (attr != null){
                                Node clonedNode = attr.cloneNode(true);
                                ((GenericAttr)clonedNode).setNodeName(attribute.getValue());
                                element.getAttributes().removeNamedItem(attribute.getKey());
                                element.getAttributes().setNamedItem(clonedNode);
                            }
                        }
                    }   
                }
            }
        }
    }

    /**
     * Adds a new attribute to be processed.
     * @param tagName the name of the tag you want to process. I.e: image, text, etc.
     * @param attributeOriginalName the name of the attribute you want to modify.
     * @param attributeNewName the new name you want to use for the attribute.
     */
    public void addAttributeToProcess(String tagName, String attributeOriginalName, String attributeNewName){
        if (!this.tagsToProcess.containsKey(tagName)){
            this.tagsToProcess.put(tagName, new HashMap<String, String>());
        }
        this.tagsToProcess.get(tagName).put(attributeOriginalName, attributeNewName);
    }
    
}
