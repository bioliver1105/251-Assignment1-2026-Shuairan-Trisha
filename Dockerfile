FROM ubuntu:20.04

RUN apt-get --yes --force-yes update && \
    apt-get --yes --force-yes install software-properties-common && \
    add-apt-repository ppa:linuxuprising/java && \
    apt-get --yes --force-yes update && \
    apt install unzip && \
    apt install zip

RUN apt-get --yes --force-yes update
RUN apt-get --yes --force-yes install git
RUN apt-get --yes --force-yes install libxrender1
RUN apt-get --yes --force-yes install libxtst6
RUN apt-get --yes --force-yes install libxi6
RUN apt-get --yes --force-yes install fonts-dejavu fontconfig
RUN apt-get --yes install openjdk-17-jdk
RUN apt-get --yes --force-yes install maven
RUN apt-get --yes --force-yes install locales locales-all
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
RUN apt-get --yes --force-yes install libgtk-3-0

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/Text-editor-1.0-SNAPSHOT.jar"]