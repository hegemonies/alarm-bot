FROM openjdk:11.0.12-slim

ADD build/libs/alarm-bot.jar /app/

CMD java -jar /app/alarm-bot.jar
