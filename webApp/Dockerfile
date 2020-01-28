FROM tomcat:9.0.30-jdk8-openjdk

VOLUME /usr/src/indexes/

RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/palmetto-webapp.war /usr/local/tomcat/webapps/ROOT.war
