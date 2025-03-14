FROM ubuntu:latest
LABEL authors="TBG"

ENTRYPOINT ["top", "-b"]