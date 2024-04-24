#!/usr/bin/env bash

cd /home/ubuntu/app/bhr
# Docker Compose를 사용하여 컨테이너를 시작합니다.
sudo docker-compose -f docker-compose.yml up -d --build --force-recreate
