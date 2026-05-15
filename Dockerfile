FROM tomcat:10.1-jdk21-openjdk-slim
RUN rm -rf /usr/local/tomcat/webapps/*
COPY webapp.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]