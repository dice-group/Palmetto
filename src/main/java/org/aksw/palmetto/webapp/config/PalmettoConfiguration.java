/**
 * This file is part of Palmetto Web Application.
 *
 * Palmetto Web Application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto Web Application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.webapp.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
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
            instance = new CompositeConfiguration();
            loadAdditionalProperties(DEFAULT_PALMETTO_PROPERTIES_FILE_NAME);
        }
        return instance;
    }

    public static synchronized void loadAdditionalProperties(String fileName) {
        try {
            ((CompositeConfiguration) getInstance()).addConfiguration(new PropertiesConfiguration(fileName));
        } catch (ConfigurationException e) {
            LOGGER.error("Couldnt load Properties from the properties file (\"" + fileName
                    + "\"). This GERBIL instance won't work as expected.", e);
        }
    }
}
