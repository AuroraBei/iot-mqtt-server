FROM recallcode/jdk:8u172
COPY ./target/mqtt-broker-1.1.jar /opt/mqtt-broker/
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /opt/mqtt-broker/mqtt-broker-1.1.jar"]
EXPOSE 8883
EXPOSE 9993
EXPOSE 47500
