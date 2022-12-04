FROM openjdk:11
COPY "./out/artifacts/bank_jar/bank.jar" "app.jar"
EXPOSE 9000
ENTRYPOINT ["java","-jar","app.jar"]