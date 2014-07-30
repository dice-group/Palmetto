Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of coherence calculations for evaluating the quality of topics. A more detailed introduction is given in <a href="http://mimno.infosci.cornell.edu/nips2013ws/nips2013tm_submission_7.pdf">*"Evaluating topic coherence measures"*</a>.

<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://aksw.org" property="cc:attributionName" rel="cc:attributionURL">AKSW</a> is licensed under a <a rel="license" href="http://opensource.org/licenses/MIT">MIT License</a>.

## How can I use it

There are two usage scenarios for Palmetto.

1. You have created some topics and want to evaluate them using common coherence measures. Than you simply want to use Palmetto as a tool.
2. You want do some research on topic coherence and want to go deeper into the system. Than you want to use Palmetto as a library.

For both cases you will need to download a Lucene index containing the preprocessed Wikipedia from <a href="http://139.18.2.164/mroeder/Wikipedia_bd.zip">here</a>. By extracting the files you should get a "wikipedia_bd" directory and a "wikipedia_bd.histogramm" file. Note that the file has to be in the same directory as the wikipedia_bd directory.

## Use Palmetto as tool

1. Download and extract the index as described above.
2. Download the runable jar file from <a href="http://139.18.2.164/mroeder/palmetto-0.1.0.jar">here</a>.
3. Run the tool with
```
java -jar palmetto-0.1.0.jar <some-path>/wikipedia_bd UCI topics.txt
```

The two last parameters are the coherence type and a file containing your topics (see below).

### Coherence type

At the moment, there are 5 common coherence types that you can run directly with this jar.

* UMass
* UCI
* NPMI
* C_P
* C_V

### Topics file

The file containing you topics should have one single topic per line. In every line the top words of your topic are listed separated by a single space. Your file should look like this:

```
company sell corporation own acquire purchase buy business sale owner
age population household female family census live average median income
```

## Use Palmetto as library

Palmetto can be used as a library to try out other coherences or create your own coherence components. The class org.aksw.palmetto.Palmetto shows how a coherence can be defined and used.
