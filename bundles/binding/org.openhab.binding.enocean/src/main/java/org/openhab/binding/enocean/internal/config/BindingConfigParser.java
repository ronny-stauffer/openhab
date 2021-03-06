/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.enocean.internal.config;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to parse the key - value base config for an EnOcean item. Generic
 * enough to be used for other bindings as well.
 * 
 * The values are set into the attributes which match the keys in the config
 * lines. Leading / trailing brackets ({}) or quotes are removed.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 * @param <TYPE>
 *            The BindingConfig to parse into.
 * @since 1.3.0
 */
public class BindingConfigParser<TYPE extends BindingConfig> {

    private static final Logger logger = LoggerFactory.getLogger(BindingConfigParser.class);

    /**
     * Parse the configLine into the given config.
     * 
     * @param configLine
     * @param config
     * @throws BindingConfigParseException
     */
    public void parse(String configLine, TYPE config) throws BindingConfigParseException {
        configLine = removeFirstBrakets(configLine);
        configLine = removeLastBrakets(configLine);
        String[] entries = configLine.trim().split("[,]");
        for (String entry : entries) {
            String[] entryParts = entry.trim().split("[=]");
            if (entryParts.length != 2) {
                throw new BindingConfigParseException("Each entry must have a key and a value");
            }
            String key = entryParts[0];
            String value = entryParts[1];
            value = removeFirstQuotes(value);
            value = removeLastQuotes(value);
            try {
                config.getClass().getDeclaredField(key).set(config, value);
            } catch (Exception e) {
                logger.error("Could set value " + value + " to attribute " + key + " in class EnoceanBindingConfig");
            }
        }

    }

    private String removeLastBrakets(String configLine) {
        if (configLine.substring(configLine.length() - 1).equals("}")) {
            return configLine.substring(0, configLine.length() - 1);
        }
        return configLine;
    }

    private String removeFirstBrakets(String configLine) {
        if (configLine.substring(0, 1).equals("{")) {
            return configLine.substring(1);
        }
        return configLine;
    }

    private String removeLastQuotes(String value) {
        if (value.substring(value.length() - 1).equals("\"")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String removeFirstQuotes(String value) {
        if (value.substring(0, 1).equals("\"")) {
            return value.substring(1);
        }
        return value;
    }

}
