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

import com.ilesteban.processimage.transformation.TransformationJob;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGOMTSpanElement;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class that processes an SVG definition of a jBPM5 process.
 * Using this class you can apply different transformations to the image.
 * This class is only meant to be used for SVG representations coming from
 * jBPM's Web Designer: https://github.com/droolsjbpm/jbpm-designer
 * @author esteban
 */
public class ProcessImageProcessor {

    public final static String TEXT_NODE_SUFIX = "text_name";
    public final static String TEXT_BACKGROUND_NODE_SUFIX = "bg_frame";
    public final static String TEXT_FRAME_NODE_SUFIX = "text_frame";
    private ProcessContext context = new ProcessContext();
    private List<TransformationJob> transformationJobs = new ArrayList<TransformationJob>();
    private ProcessImageProcessorConfiguration configuration = new ProcessImageProcessorConfiguration();

    /**
     * Creates a new instance of <code>ProcessImageProcessor</code> from an
     * InputStream.
     * @param processDefinition an InputStream pointing to the SVG process image. 
     * @throws IOException 
     */
    public ProcessImageProcessor(InputStream processDefinition) throws IOException {
        this(processDefinition, null);
    }

    /**
     * Creates a new instance of <code>ProcessImageProcessor</code> from an
     * InputStream using a configuration object to control certain aspects of
     * the processing mechanism.
     * @param processDefinition an InputStream pointing to the SVG process image. 
     * @param configuration the configuration to apply.
     * @throws IOException 
     */
    public ProcessImageProcessor(InputStream processDefinition, ProcessImageProcessorConfiguration configuration) throws IOException {
        if (configuration != null) {
            this.configuration = configuration;
        }

        Map<String, TaskDefinition> taskDefinitions = new HashMap<String, TaskDefinition>();

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);

        //SVGDocument svgDocument = f.createSVGDocument("http://test", processDefinition);
        Document svgDocument = f.createDocument("http://test", processDefinition);

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
                                //System.out.println("tspan#" + j + " of '" + task.getTaskName() + "'");
                                if (processedTSpanElements < totalTSpanElements - 1 && totalTSpanElements > 1) {
                                    Float x = Float.parseFloat(spanElement.getAttribute("x"));
                                    Float xPad = this.configuration.getTaskSpecificTextPad(task.getTaskName());

                                    //System.out.printf("(%s,%s) Old x for tspan %s of task '%s'= %s. New x= %s \n", processedTSpanElements, totalTSpanElements, j, task.getTaskName(), x, String.valueOf((x - xPad)));
                                    spanElement.setAttribute("x", String.valueOf((x - xPad)));
                                }
                                processedTSpanElements++;
                                //System.out.println("\n\n");
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

    /**
     * Creates a new instance of <code>ProcessImageProcessor</code> from an
     * String.
     * @param processDefinition an String containing the SVG process image. 
     * @throws IOException 
     */
    public ProcessImageProcessor(String processDefinition) throws IOException {
        this(new ByteArrayInputStream(processDefinition.getBytes()));
    }

    /**
     * Creates a new instance of <code>ProcessImageProcessor</code> from an
     * String using a configuration object to control certain aspects of
     * the processing mechanism.
     * @param processDefinition an String containing the SVG process image. 
     * @param configuration the configuration to apply.
     * @throws IOException 
     */
    public ProcessImageProcessor(String processDefinition, ProcessImageProcessorConfiguration configuration) throws IOException {
        this(new ByteArrayInputStream(processDefinition.getBytes()), configuration);
    }

    public Map<String, TaskDefinition> getTaskDefinitions() {
        return this.context.getTaskDefinitions();
    }

    /**
     * Adds a new transformation job to be applied. Transformation jobs are applied
     * in the same order they are added to this class.
     * @param e
     * @return 
     */
    public boolean addTransformationJob(TransformationJob e) {
        return transformationJobs.add(e);
    }

    /**
     * Adds a set of transformation jobs to be applied. Transformation jobs are applied
     * in the same order they are added to this object.
     * @param e
     * @return 
     */
    public boolean addAllTransformationJobs(Collection<? extends TransformationJob> c) {
        return transformationJobs.addAll(c);
    }

    /**
     * Clears any transformation job previously added to this object.
     */
    public void clearTransformationJobs() {
        this.transformationJobs = new ArrayList<TransformationJob>();
    }

    /**
     * Applies all the transformations added to this object.
     * Transformation jobs are applied in the same order they are added to this 
     * object.
     * This method is the same as {@link #applyTransformationJobs(boolean) applyTransformationJobs(true)} 
     */
    public void applyTransformationJobs() {
        this.applyTransformationJobs(true);
    }

    /**
     * Applies all the transformations added to this object.
     * Transformation jobs are applied in the same order they are added to this 
     * object.
     * @param clearTransformationJobs clear all the transformation jobs after
     * they are applied.
     */
    public void applyTransformationJobs(boolean clearTransformationJobs) {
        for (TransformationJob transformationJob : transformationJobs) {
            transformationJob.transform(context);
        }

        if (clearTransformationJobs) {
            this.clearTransformationJobs();
        }
    }

    /**
     * Returns an XML representation of the SVG being processed.
     * @return 
     */
    public Reader toXML() {
        try {
            DOMSource domSource = new DOMSource(this.context.getSvgDocument());
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return new BufferedReader(new StringReader(writer.toString()));
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns a PNG representation of the SVG being processed.
     * @return 
     */
    public InputStream toPNG() {
        try {

            PNGTranscoder transcoder = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(this.toXML());
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
