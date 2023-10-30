FROM openjdk:11

RUN chsh -s /bin/bash
ENV SHELL=/bin/bash

RUN adduser --gecos '' --disabled-password template

EXPOSE 8082
USER template
WORKDIR /home/template

ADD --chown=template bin /home/template
ADD --chown=template image-bed /home/template/image-bed

ENTRYPOINT [ "java", "-jar", "/home/template/template-shop-1.0.0.jar", "--spring.profiles.active=prod" ]