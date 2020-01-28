[![Build Status](https://travis-ci.org/dice-group/Palmetto.svg?branch=master)](https://travis-ci.org/dice-group/Palmetto) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/e692cb15385f4529803d35d1267c63bc)](https://www.codacy.com/app/MichaelRoeder/Palmetto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dice-group/Palmetto&amp;utm_campaign=Badge_Grade)

Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of coherence calculations for evaluating the quality of topics. If you want to learn more about coherence calculations and their meaning for topic evaluation, take a look at the <a href="http://palmetto.aksw.org/">project homepage</a> - especially at the publications.

<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://cs.uni-paderborn.de/ds/" property="cc:attributionName" rel="cc:attributionURL">DICE</a> is licensed under a <a rel="license" href="https://www.gnu.org/licenses/agpl.txt">AGPL v3.0 License</a>.

Please take a look at the the wikipage to read <a href="https://github.com/AKSW/Palmetto/wiki/How-Palmetto-can-be-used">how Palmetto can be used</a>. If you would like to use a different index than the one we are providing, you can <a href="https://github.com/dice-group/Palmetto/wiki/How-to-create-a-new-index">create your own index</a>.

If you are using Palmetto for an experiment or something similar that leads to a publication, please cite the paper "Exploring the Space of Topic Coherence Measures" that you can find on the project website. A link to the project website is welcome as well :)

### Directories

The `palmetto` directory contains the Palmetto library.

The `webApp` directory contains a web application offering a small demo as well as a web service API for using Palmetto.

### Docker

Palmetto can be used as a docker container.

[The index](https://hobbitdata.informatik.uni-leipzig.de/homes/mroeder/palmetto/Wikipedia_bd.zip) should be downloaded and extracted to some path (for example, `/path/to/indexes`).
The container can be built and run the following way:
```
make build dockerize
docker run -p 7777:8080 -d -v /path/to/indexes/:/usr/src/indexes/:ro dicegroup/palmetto-service`
```

After that the demo application can be accessed using `http://localhost:7777/`.
