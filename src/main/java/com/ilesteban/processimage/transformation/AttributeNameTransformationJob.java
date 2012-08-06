/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processimage.transformation;

import com.ilesteban.processimage.ProcessContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.dom.GenericAttr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


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

    public void addAttributeToProcess(String tagName, String attributeOriginalName, String attributeNewName){
        if (!this.tagsToProcess.containsKey(tagName)){
            this.tagsToProcess.put(tagName, new HashMap<String, String>());
        }
        this.tagsToProcess.get(tagName).put(attributeOriginalName, attributeNewName);
    }
    
}
