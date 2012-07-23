/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring;

import com.ilesteban.processcoloring.transformation.TransformationJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMTSpanElement;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author esteban
 */
public class ProcessImageProcessor {

    public final static String TEXT_NODE_SUFIX = "text_name";
    public final static String TEXT_BACKGROUND_NODE_SUFIX = "bg_frame";
    public final static String TEXT_FRAME_NODE_SUFIX = "text_frame";
    private ProcessContext context = new ProcessContext();
    private List<TransformationJob> transformationJobs = new ArrayList<TransformationJob>();
    private ProcessImageProcessorConfiguration configuration = new ProcessImageProcessorConfiguration();

    public ProcessImageProcessor(InputStream processDefinition) throws IOException {
        this(processDefinition, null);
    }
    
    public ProcessImageProcessor(InputStream processDefinition, ProcessImageProcessorConfiguration configuration) throws IOException {
        if (configuration != null){
            this.configuration = configuration;
        }
        
        Map<String, TaskDefinition> taskDefinitions = new HashMap<String, TaskDefinition>();

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        
        SVGDocument svgDocument = f.createSVGDocument("http://test", processDefinition);
        
        NodeList texts = svgDocument.getElementsByTagName("text");

        for (int i = 0; i < texts.getLength(); i++) {
            Node textNode = texts.item(i);
            String textNodeId = textNode.getAttributes().getNamedItem("id").getFirstChild().getNodeValue();

            if (textNodeId.endsWith(TEXT_NODE_SUFIX)) {
                String genericId = textNodeId.replace(TEXT_NODE_SUFIX, "");

                Element bgNode = svgDocument.getElementById(genericId + TEXT_BACKGROUND_NODE_SUFIX);
                if (bgNode != null) {
                    String bgNodeId = bgNode.getAttributeNode("id").getValue();

                    Element frameNode = svgDocument.getElementById(genericId + TEXT_FRAME_NODE_SUFIX);
                    if (frameNode != null) {
                        String frameNodeId = frameNode.getAttributeNode("id").getValue();
                        TaskDefinition task = new TaskDefinition(textNodeId, bgNodeId, frameNodeId);

                        StringBuilder taskLabel = new StringBuilder();
                        int totalTSpanElements = 0;
                        for (int j = 0; j < textNode.getChildNodes().getLength(); j++) {
                            if (textNode.getChildNodes().item(j) instanceof SVGOMTSpanElement) {
                                SVGOMTSpanElement spanElement = (SVGOMTSpanElement) textNode.getChildNodes().item(j);
                                taskLabel.append(spanElement.getFirstChild().getNodeValue());
                                totalTSpanElements++;
                            }
                        }

                        task.setTaskName(taskLabel.toString());
                        int processedTSpanElements = 0;
                        //now that we know the task label let's apply the padding to the text
                        for (int j = 0; j < textNode.getChildNodes().getLength(); j++) {
                            if (textNode.getChildNodes().item(j) instanceof SVGOMTSpanElement) {
                                SVGOMTSpanElement spanElement = (SVGOMTSpanElement) textNode.getChildNodes().item(j);
                                System.out.println("tspan#"+j+" of '"+task.getTaskName()+"'");
                                if (processedTSpanElements < totalTSpanElements-1 && totalTSpanElements > 1){
                                    Float x = Float.parseFloat(spanElement.getAttribute("x"));
                                    Float xPad = this.configuration.getTaskSpecificTextPad(task.getTaskName());
                                    
                                    System.out.printf("(%s,%s) Old x for tspan %s of task '%s'= %s. New x= %s \n",processedTSpanElements, totalTSpanElements, j,task.getTaskName(),x,String.valueOf((x-xPad)));
                                    spanElement.setAttribute("x", String.valueOf((x-xPad)));
                                }
                                processedTSpanElements++;
                                System.out.println("\n\n");
                            }
                            
                        }
                        
                        //TODO: problems with duplicated names
                        taskDefinitions.put(task.getTaskName(), task);

                    }

                }

            }

        }

        this.context.setSvgDocument(svgDocument);
        this.context.setTaskDefinitions(taskDefinitions);
    }

    public ProcessImageProcessor(String processDefinition) throws IOException {
        this(new ByteArrayInputStream(processDefinition.getBytes()));
    }
    
    public ProcessImageProcessor(String processDefinition, ProcessImageProcessorConfiguration configuration) throws IOException {
        this(new ByteArrayInputStream(processDefinition.getBytes()), configuration);
    }

    public Map<String, TaskDefinition> getTaskDefinitions() {
        return this.context.getTaskDefinitions();
    }

    public boolean addTransformationJob(TransformationJob e) {
        return transformationJobs.add(e);
    }

    public boolean addAllTransformationJobs(Collection<? extends TransformationJob> c) {
        return transformationJobs.addAll(c);
    }

    public void clearTransformationJobs() {
        this.transformationJobs = new ArrayList<TransformationJob>();
    }

    public void applyTransformationJobs() {
        this.applyTransformationJobs(true);
    }

    public void applyTransformationJobs(boolean clearTransformationJobs) {
        for (TransformationJob transformationJob : transformationJobs) {
            transformationJob.transform(context);
        }

        if (clearTransformationJobs) {
            this.clearTransformationJobs();
        }
    }

    public InputStream toSVG() {
        if (true){
           // throw new UnsupportedOperationException("Not yet implemented!");
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
             
             SVGGraphics2D svggd = new SVGGraphics2D(this.context.getSvgDocument());
             svggd.stream(new OutputStreamWriter(baos),true);
             baos.close();
             return new ByteArrayInputStream(baos.toByteArray());
             

            // Use a Transformer for output
//            TransformerFactory tFactory =
//                    TransformerFactory.newInstance();
//            Transformer transformer =
//                    tFactory.newTransformer();
//
//            DOMSource source = new DOMSource(this.context.getSvgDocument());
//            StreamResult result = new StreamResult(baos);
//            transformer.transform(source, result);

//            return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public InputStream toPNG() {
        try {
            
            PNGTranscoder transcoder = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(this.context.getSvgDocument());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(baos);
            transcoder.transcode(input, output);
            baos.close();
            
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
