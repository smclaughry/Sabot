package org.sabot.server;

import java.io.StringWriter;
import java.io.Writer;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.sabot.shared.exception.ActionException;

/*
 * Library Sabot: a library for accelerating GWT and AppEngine development
 * 
 * Copyright (C) 2011  Phil Craven, Stephen McLaughry
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
public class VelocityRenderer {
	public interface ContextLoader{
		void loadContext(VelocityContext ctx);
	}
	
	private static final String TEMPLATE_DIR = "WEB-INF/templates/";
	private final Log logger;
	
	@Inject
	public VelocityRenderer(Log logger){
	    this.logger = logger;
		VelocityEngine ve = new VelocityEngine();
	    ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
	    ve.setProperty("runtime.log.logsystem.log4j.logger", VelocityRenderer.class.getName());
	    try {
			ve.init();
		} catch (Exception e) {
			logger.error("Failed to initialized VelocityEngine, bad things may happen with email notifications:", e);
		}

		java.util.Properties p = new java.util.Properties();
		p.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
		try {
		   Velocity.init(p);
		} catch (Exception e) {
			logger.error("Failed to initialized Velocity, bad things may happen with email notifications:", e);			
		}
	}
	
	public String renderTemplate(String templateName, ContextLoader contextLoader) throws ActionException {
		try{
			Template template = Velocity.getTemplate(TEMPLATE_DIR + templateName);
			VelocityContext ctx = new VelocityContext();
			contextLoader.loadContext(ctx);
			
			Writer writer = new StringWriter();
			template.merge(ctx, writer);
			
			return writer.toString();    	
		}catch(Exception e){
			logger.warn("Failed to render Velocity template "+templateName, e);
			throw new ActionException(e);
		}
	}
}
