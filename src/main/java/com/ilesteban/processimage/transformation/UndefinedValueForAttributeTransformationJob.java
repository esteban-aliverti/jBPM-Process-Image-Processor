/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processimage.transformation;

import com.ilesteban.processimage.ProcessContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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

    public void addAttributeToProcess(String tagName, String attributeName, String value){
        if (!this.tagsToProcess.containsKey(tagName)){
            this.tagsToProcess.put(tagName, new HashMap<String, String>());
        }
        this.tagsToProcess.get(tagName).put(attributeName, value);
    }
    
    
}
