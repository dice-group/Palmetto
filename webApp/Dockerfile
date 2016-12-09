FROM tomcat:8.0.36-jre8

RUN apt-get update && \
        apt-get install -y maven openjdk-8-jdk

#RUN wget -O tmp.zip http://139.18.2.164/mroeder/palmetto/Wikipedia_bd.zip;  unzip tmp.zip -d /usr/src/indexes/; rm tmp.zip
VOLUME /usr/src/indexes/

ADD src /usr/src/palmetto/src
ADD LICENSE /usr/src/palmetto/LICENSE
ADD pom.xml /usr/src/palmetto/pom.xml
ADD README.md /usr/src/palmetto/README.md
WORKDIR /usr/src/palmetto

RUN mvn clean package
RUN rm -rf /usr/local/tomcat/webapps/*
RUN cp /usr/src/palmetto/target/palmetto-webapp.war /usr/local/tomcat/webapps/ROOT.war