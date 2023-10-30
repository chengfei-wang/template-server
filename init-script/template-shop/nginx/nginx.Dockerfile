FROM nginx:latest

RUN apt-get update && apt-get install -y nano git sudo

RUN chsh -s /bin/bash
ENV SHELL=/bin/bash

RUN adduser --gecos '' --disabled-password template && echo "template ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/nopasswd

ADD html /usr/share/nginx/html

EXPOSE 80
USER template
WORKDIR /home/template
ENTRYPOINT [ "sudo", "nginx-debug", "-g", "daemon off;" ]