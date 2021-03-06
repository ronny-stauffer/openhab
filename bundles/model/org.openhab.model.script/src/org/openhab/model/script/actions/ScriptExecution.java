/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.model.script.actions;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.joda.time.base.AbstractInstant;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.script.internal.ScriptActivator;
import org.openhab.model.script.internal.actions.TimerExecutionJob;
import org.openhab.model.script.internal.actions.TimerImpl;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The static methods of this class are made available as functions in the scripts.
 * This allows a script to call another script, which is available as a file.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class ScriptExecution {

	private static final Logger logger = LoggerFactory.getLogger(ScriptExecution.class);

	/**
	 * Calls a script which must be located in the configurations/scripts folder.
	 * 
	 * @param scriptName the name of the script (if the name does not end with
	 * the .script file extension it is added)
	 * 
	 * @return the return value of the script
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public static Object callScript(String scriptName) throws ScriptExecutionException {
		ModelRepository repo = ScriptActivator.modelRepositoryTracker.getService();
		if(repo!=null) {
			String scriptNameWithExt = scriptName;
			if (!StringUtils.endsWith(scriptName, Script.SCRIPT_FILEEXT)) {
				scriptNameWithExt = scriptName + "." + Script.SCRIPT_FILEEXT;
			}
			XExpression expr = (XExpression) repo.getModel(scriptNameWithExt);
			if(expr!=null) {
				ScriptEngine scriptEngine = ScriptActivator.scriptEngineTracker.getService();
				if(scriptEngine!=null) {
					Script script = scriptEngine.newScriptFromXExpression(expr);
					return script.execute();
				} else {
					throw new ScriptExecutionException("Script engine is not available.");
				}
			} else {
				throw new ScriptExecutionException("Script '" + scriptName + "' cannot be found.");
			}
		} else {
			throw new ScriptExecutionException("Model repository is not available.");
		}
	}
	
	/**
	 * Schedules a block of code for later execution.
	 * 
	 * @param instant the point in time when the code should be executed
	 * @param closure the code block to execute
	 * 
	 * @return a handle to the created timer, so that it can be canceled or rescheduled
	 * @throws ScriptExecutionException if an error occurs during the execution
	 */
	public static Timer createTimer(AbstractInstant instant, Procedure0 closure) {
		JobKey jobKey = new JobKey(instant.toString() + ": " + closure.toString());
        Trigger trigger = newTrigger().startAt(instant.toDate()).build();
		Timer timer = new TimerImpl(jobKey, trigger.getKey(), instant);
		try {
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("procedure", closure);
			dataMap.put("timer", timer);
	        JobDetail job = newJob(TimerExecutionJob.class)
	            .withIdentity(jobKey)
	            .usingJobData(dataMap)
	            .build();	
	        TimerImpl.scheduler.scheduleJob(job, trigger);
			logger.debug("Scheduled code for execution at {}", instant.toString());
			return timer;
		} catch(SchedulerException e) {
			logger.error("Failed to schedule code for execution.", e);
			return null;
		}
	}
}
