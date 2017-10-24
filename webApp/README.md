Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of a simple web service wrapping Palmetto.

<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://cs.uni-paderborn.de/ds/" property="cc:attributionName" rel="cc:attributionURL">DICE</a> is licensed under a <a rel="license" href="https://www.gnu.org/licenses/agpl.txt">AGPL v3.0 License</a>.

### Docker

Palmetto can be used as a docker container. The container can be build and run from the `webApp` directory.

```
docker build -t palmetto .
docker run -p 7777:8080 -d -m 4G palmetto`
```

After that there is a Tomcat listening on port 7777. The demo application can be accessed using `http://localhost:7777`.
