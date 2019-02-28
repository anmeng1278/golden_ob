FROM openjdk:8-jdk

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xmx800m"
ENV RUN_MODEL="--spring.profiles.active=dev"

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY /target/*.jar /app/app.jar

EXPOSE 80

CMD java -jar ${JAVA_OPTS} app.jar ${RUN_MODEL}
