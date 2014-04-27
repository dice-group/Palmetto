/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.vector;

import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilitySupplier;

public interface VectorCreator {

    public SubsetVectors[] getVectors(String wordsets[][], SubsetDefinition definitions[]);

    public void setProbabilitySupplier(ProbabilitySupplier supplier);

    public String getProbabilityModelName();

    public String getVectorSpaceName();

    public String getVectorCreatorName();

    public void setMinFrequency(int minFrequency);
}
