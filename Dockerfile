# 1. 実行環境としてTomcat（Java 11）を使用
FROM tomcat:10.1-jdk17-openjdk-slim

# 2. Eclipseで作成されるWARファイルをTomcatの公開ディレクトリにコピー
# ※通常、プロジェクト名.war という名前で書き出されます
COPY webapp.war /usr/local/tomcat/webapps/ROOT.war

# 3. ポート8080で待機
EXPOSE 8080

# 4. Tomcatを起動
CMD ["catalina.sh", "run"]