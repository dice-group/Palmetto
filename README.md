Palmetto
========
<i><b>P</b>almetto is a qu<b>al</b>ity <b>me</b>asuring <b>t</b>ool for <b>to</b>pics</i>

This is the implementation of the coherence calculations presented on <a href="https://sites.google.com/site/nips2013topicmodels/home">NIPS 2013 topic models workshop</a>. A detailed introduction is given in <a href="http://mimno.infosci.cornell.edu/nips2013ws/nips2013tm_submission_7.pdf">*"Evaluating topic coherence measures"*</a> for this workshop.

<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/deed.de"><img alt="Creative Commons Lizenzvertrag" style="border-width:0" src="http://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Palmetto</span> from <a xmlns:cc="http://creativecommons.org/ns#" href="http://aksw.org" property="cc:attributionName" rel="cc:attributionURL">AKSW</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/deed.de">Creative Commons Attribution-NonCommercial 4.0 International Public License</a>.

## Dependencies 

The project uses the following two dependencies that could cause some problems.

```XML
        <!-- Mallet -->
        <dependency>
            <groupId>cc.mallet</groupId>
            <artifactId>mallet</artifactId>
            <version>2.0.7-SNAPSHOT</version>
        </dependency>
        <!-- Apache Commons CLI -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-cli</artifactId>
            <version>1.2</version>
        </dependency>
```

It is possible that these dependencies can't be found by maven in one of the common repositories. But you can easely solve this problem by downloading Mallet from http://mallet.cs.umass.edu/download.php and Commons CLI from http://commons.apache.org/proper/commons-cli/download_cli.cgi

All other dependencies shouldn't cause problems.

## Usage

For the coherence calculation of a topic you need the top words of the topic and a Lucene index containing the corpus (This should be the corpus on which the topic has been calculated. If you already have a Lucene 4.4.0 index which contains your corpus you can simply use this. Otherwise the program will create a new index.

The main class of this project is `org.aksw.palmetto.Palmetto`. It called using the following command:

`java org.aksw.palmetto.Palmetto --calcType <calcuation> --indexDir <directory> --inputFile <file> [OPTIONS...]`

| Parameter/Option | Meaning |
| ------------- | ------------- |
| `--calcType <calcuation>` | The coherence calculation type. Take a look on the list below for details. |
| `--corpusFile <file>` | The file containing the corpus (one document per line, words seperated by whitespaces). If this option is added, the lucene index will be created using this file. |
| `--help` | The program prints a short help message. |
| `--indexDir <directory>` | The directory of the lucene index. |
| `--indexFieldName <name>` | The field name of the lucene index containing the words. |
| `--inputFile <file>` | The file containing the top words of the topics (one topic per line, words seperated by whitespaces).|
| `--malletImport` | The corpus file is a mallet import file. This option is only read if the corpusFile is present. |
| `--minFreq <min-frequency>` | The minimum frequency a word must have. The default value is 10.|

## Coherences

Until now the most important coherences which are implemented are the one-all, one-any, any-any and the UMass coherences that are described in the workshop paper. The calculation type parameter which is needed to run the program comprises of a definition of top word subsets and a calculation. Until now the only calculation implemented is the difference calcultaion (except the UMass coherence). The possible values of the calculation type parameter are:

* oneone-diff
* onepreceding-diff
* oneall-diff
* oneany-diff
* anyany-diff
* UMass
 
In this first version the UCI coherence is not implemented, because we still have to transform the wikipedia statistics that are needed for this coherence.
