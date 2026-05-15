# Tomcat 10.1 (jakarta対応) かつ Java 21 (今のEclipseに合わせる)
FROM tomcat:10.1-jdk21-openjdk-slim

# デフォルトのアプリを削除
RUN rm -rf /usr/local/tomcat/webapps/*

# webapp.warをROOT.warとしてコピー
COPY webapp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]