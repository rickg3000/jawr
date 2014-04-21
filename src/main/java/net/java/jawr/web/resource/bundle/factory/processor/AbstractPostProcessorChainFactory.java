/**
 * Copyright 2007 Jordi Hern�ndez Sell�s
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.java.jawr.web.resource.bundle.factory.processor;

import java.util.StringTokenizer;

import net.java.jawr.web.resource.bundle.postprocess.AbstractChainedResourceBundlePostProcessor;
import net.java.jawr.web.resource.bundle.postprocess.EmptyResourceBundlePostProcessor;
import net.java.jawr.web.resource.bundle.postprocess.ResourceBundlePostProcessor;
import net.java.jawr.web.resource.bundle.postprocess.impl.LicensesIncluderPostProcessor;

/**
 * Abstract implementation of the PostProcessorChainFactory with functionalities common to js and css resources.  
 * 
 * @author Jordi Hern�ndez Sell�s
 *
 */
public abstract class AbstractPostProcessorChainFactory implements	PostProcessorChainFactory {

	protected static final String LICENSE_INCLUDER = "license";
	protected static final String NO_POSTPROCESSING_KEY = "none";
	
	

	/* (non-Javadoc)
	 * @see net.java.jawr.web.resource.bundle.factory.processor.PostProcessorChainFactory#buildPostProcessorChain(java.lang.String)
	 */
	public ResourceBundlePostProcessor buildPostProcessorChain(String processorKeys) {
		if(null == processorKeys)
			return null;
		else if(NO_POSTPROCESSING_KEY.equals(processorKeys))
			return new EmptyResourceBundlePostProcessor();
		
		StringTokenizer tk = new StringTokenizer(processorKeys,",");
		
		AbstractChainedResourceBundlePostProcessor chain = null;
		while(tk.hasMoreTokens())
			chain = addOrCreateChain(chain,tk.nextToken());
		
		return chain;
	}
	
	/**
	 * Creates an AbstractChainedResourceBundlePostProcessor. If the supplied chain is null, the new chain is returned. Otherwise it
	 * is added to the existing chain.  
	 * @param chain
	 * @param key
	 * @return
	 */
	private AbstractChainedResourceBundlePostProcessor addOrCreateChain(AbstractChainedResourceBundlePostProcessor chain, String key) {
		AbstractChainedResourceBundlePostProcessor toAdd = buildProcessorByKey(key);
		if(null == chain)
			chain = toAdd;
		else chain.setNextProcessor(toAdd);
		return chain;
	}
	
	/**
	 * Builds an AbstractChainedResourceBundlePostProcessor based on the supplied key. If the key doesn't match
	 * any PostProcessor (as defined in the documentation), an IllegalArgumentException is thrown. 
	 * @param key
	 * @return
	 */
	protected abstract AbstractChainedResourceBundlePostProcessor buildProcessorByKey(String key);
	

	/**
	 * Builds an instance of LicensesIncluderPostProcessor. 
	 * @return LicensesIncluderPostProcessor
	 */
	protected LicensesIncluderPostProcessor buildLicensesProcessor() {
		return new LicensesIncluderPostProcessor();
	}

}