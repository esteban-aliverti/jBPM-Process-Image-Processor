/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring;

import com.ilesteban.processcoloring.transformation.TaskColorTransformationJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author esteban
 */
public class SimpleTest {
    
    
    @BeforeClass
    public static void setup() throws IOException{
    }
    
    @Test
    public void taskDefinitionParser() throws Exception{
        ProcessImageProcessor processor = new ProcessImageProcessor(SimpleTest.class.getResourceAsStream("/test1.svg"));
        Assert.assertEquals(5, processor.getTaskDefinitions().size());
    }
    
    @Test
    public void changeTaskColorTest() throws Exception{

        //Create a new Configuration object to set the padd to use in the label of the tasks
        ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
        config.setDefaultextPad(20.0f);
        
        //Create a processor instance for test1.svg
        ProcessImageProcessor processor = new ProcessImageProcessor(SimpleTest.class.getResourceAsStream("/test1.svg"), config);
        
        //write the original svg to disk
        File f = this.toTmpFile(processor.toPNG(), "original", ".png");
        
        //Add 3 transformation for some of the tasks we have in the process
        processor.addTransformationJob(new TaskColorTransformationJob("Assign Bed", "#00ff00"));
        processor.addTransformationJob(new TaskColorTransformationJob("Coordinate Staff", "#00ff00"));
        processor.addTransformationJob(new TaskColorTransformationJob("Check In Patient", "#ff0000"));
        
        //apply the transformations
        processor.applyTransformationJobs(true);

        //write the modified file to disk
        f = this.toTmpFile(processor.toPNG(), "modified", ".png");
        
    }
    
    private File toTmpFile(InputStream is, String prefix, String suffix) throws IOException{
        File tmpFile = File.createTempFile(prefix, suffix);
        
        IOUtils.copy(is, new FileOutputStream(tmpFile));
        
        return tmpFile;
    }
    
}
