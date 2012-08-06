/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processimage.transformation;

import com.ilesteban.processimage.ProcessContext;

/**
 *
 * @author esteban
 */
public interface TransformationJob {
    public void transform(ProcessContext context);
}
