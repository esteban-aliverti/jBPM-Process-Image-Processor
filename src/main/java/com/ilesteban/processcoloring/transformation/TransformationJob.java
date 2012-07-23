/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ilesteban.processcoloring.transformation;

import com.ilesteban.processcoloring.ProcessContext;

/**
 *
 * @author esteban
 */
public interface TransformationJob {
    public void transform(ProcessContext context);
}
