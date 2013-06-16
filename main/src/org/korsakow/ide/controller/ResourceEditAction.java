/**
 * 
 */
package org.korsakow.ide.controller;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.ui.ResourceEditor;

public interface ResourceEditAction
{
	ResourceEditor run(IResource resource) throws Exception;
}