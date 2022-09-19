FROM tomcat:9.0.65-jre8

VOLUME /usr/src/indexes/

RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/palmetto-webapp.war /usr/local/tomcat/webapps/ROOT.war
