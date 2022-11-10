/**
 * Palmetto Web Application - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.webapp.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the properties needed by Palmetto. Implements the Singleton pattern.
 * 
 * @author m.roeder
 * 
 */
public class PalmettoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalmettoConfiguration.class);

    private static final String DEFAULT_PALMETTO_PROPERTIES_FILE_NAME = "palmetto.properties";

    private static Configuration instance = null;

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            CompositeConfiguration tempConfig = new CompositeConfiguration();
            // Add environmental variables first
            tempConfig.addConfiguration(new EnvironmentConfiguration());
            // Add default values for parameters to ensure that all parameters are set
            try {
                tempConfig.addConfiguration(new PropertiesConfiguration(DEFAULT_PALMETTO_PROPERTIES_FILE_NAME));
            } catch (ConfigurationException e) {
                LOGGER.error("Couldnt load Properties from the properties file (\"" + DEFAULT_PALMETTO_PROPERTIES_FILE_NAME
                        + "\"). This GERBIL instance won't work as expected.", e);
            }
            instance = tempConfig;
        }
        return instance;
    }
}
