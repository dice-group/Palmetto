FROM maven:3-jdk-7-onbuild
EXPOSE 7777
CMD ["mvn", "clean",  "tomcat:run",  "-Dmaven.tomcat.port=7777"]

