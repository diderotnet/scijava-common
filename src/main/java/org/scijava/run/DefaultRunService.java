/*
 * #%L
 * SciJava Common shared library for SciJava software.
 * %%
 * Copyright (C) 2009 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.run;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.scijava.log.LogService;
import org.scijava.plugin.AbstractHandlerService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

/**
 * Default service for managing available {@link CodeRunner} plugins.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public class DefaultRunService extends
	AbstractHandlerService<Object, CodeRunner> implements RunService
{

	@Parameter
	private LogService log;

	// -- RunService methods --

	@Override
	public void run(final Object code, final Object... args)
		throws InvocationTargetException
	{
		for (final CodeRunner runner : getInstances()) {
			if (runner.supports(code)) {
				runner.run(code, args);
				return;
			}
		}
		throw new IllegalArgumentException("Unknown code type: " + code);
	}

	@Override
	public void run(final Object code, final Map<String, Object> inputMap)
		throws InvocationTargetException
	{
		for (final CodeRunner runner : getInstances()) {
			if (runner.supports(code)) {
				runner.run(code, inputMap);
				return;
			}
		}
		throw new IllegalArgumentException("Unknown code type: " + code);
	}

	// -- PTService methods --

	@Override
	public Class<CodeRunner> getPluginType() {
		return CodeRunner.class;
	}

	// -- Typed methods --

	@Override
	public Class<Object> getType() {
		return Object.class;
	}

}
