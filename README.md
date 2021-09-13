[![Maven Build](https://github.com/dice-group/Palmetto/actions/workflows/maven.yml/badge.svg)](https://github.com/dice-group/Palmetto/actions/workflows/maven.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/0b0a42e905454c7cacb61243c76316a0)](https://www.codacy.com/gh/dice-group/Palmetto/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dice-group/Palmetto&amp;utm_campaign=Badge_Grade) [![Codacy Badge](https://app.codacy.com/project/badge/Coverage/0b0a42e905454c7cacb61243c76316a0)](https://www.codacy.com/gh/dice-group/Palmetto/dashboard?utm_source=github.com&utm_medium=referral&utm_content=dice-group/Palmetto&utm_campaign=Badge_Coverage)

Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of coherence calculations for evaluating the quality of topics. If you want to learn more about coherence calculations and their meaning for topic evaluation, take a look at the <a href="http://palmetto.aksw.org/">project homepage</a> - especially at the publications.

<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://cs.uni-paderborn.de/ds/" property="cc:attributionName" rel="cc:attributionURL">DICE</a> is licensed under a <a rel="license" href="https://www.gnu.org/licenses/agpl.txt">AGPL v3.0 License</a>.

Please take a look at the the wikipage to read <a href="https://github.com/AKSW/Palmetto/wiki/How-Palmetto-can-be-used">how Palmetto can be used</a>.

If you are using Palmetto for an experiment or something similar that leads to a publication, please cite the paper "Exploring the Space of Topic Coherence Measures" that you can find on the project website. A link to the project website is welcome as well :)

### Applicability

The coherence measures implemented with Palmetto mainly built on a reference index. This index is used to derive counts for the calculation of the coehrence values. These values can be used to measure the human interpretability of topics based on the topics' top words. It should be noted that the preprocessing of the index has an influence on the results. 

_It is highly suggested to use an index that fits to the preprocessing that has been applied to the corpus on which the topics have been generated._

We use an English Wikipedia which has been preprocessed using a Lemmatizer. In practice, this means that word groups with non-lemmatized words may lead to unintuitive results simply because these word forms are underrepresented or even missing in our index (e.g., #57). In these cases, it is recommended to [generate an own index](https://github.com/dice-group/Palmetto/wiki/How-to-create-a-new-index).

### Directories

The `palmetto` directory contains the Palmetto library.

The `webApp` directory contains a web application offering a small demo as well as a web service API for using Palmetto.

### Docker

Palmetto can be used as a docker container.

[The index](https://hobbitdata.informatik.uni-leipzig.de/homes/mroeder/palmetto/Wikipedia_bd.zip) should be downloaded and extracted to some path (for example, `/path/to/indexes`). After extraction, the directory should contain the `wikipedia_bd` directory and the `wikipedia_bd.histogram` file.
```
path
+- to
  +- indexes
    +- wikipedia_bd
    +- wikipedia_bd.histogram
```
After that, the container can be run the following way:
```
docker run -p 7777:8080 -d -v /path/to/indexes/:/usr/local/indexes/:ro dicegroup/palmetto-service
```
After that the demo application can be accessed using `http://localhost:7777/`.

#### Adapted Docker image

In case the Palmetto code has been adapted locally, the Docker image can be build with the following command:
```
make build dockerize
```

