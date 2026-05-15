# Tomcatのイメージをベースにする
FROM tomcat:9.0-jdk11-openjdk

# Eclipseで書き出したWARファイルをTomcatの公開ディレクトリにコピー
COPY target/your-app.war /usr/local/tomcat/webapps/ROOT.war

# ポート8080を解放
EXPOSE 8080
CMD ["catalina.sh", "run"]