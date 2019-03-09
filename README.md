# Securing an API - From basics to beyond

This repository is used to accompany and illustrate some security vulnerabilities highlighted during the _**[Securing an API - From basics to beyond](https://lusoalex.github.io#talks)**_ talk.

![](https://lusoalex.github.io/assets/img/talk_secure_api.png)

## Requirements

 * [GIT](https://git-scm.com) - Seems obvious right... - 2.20.1+ 
 * [Maven](https://maven.apache.org/) - Dependency Management / Back - 3.3.9+ 
 * [Java](https://www.java.com) - Compilation & runtime / Back - Java 8+ 
 * [Npm](https://www.npmjs.com/) - Dependency Management / Front - 6.8.0+ 
 * [NodeJs](https://nodejs.org/en/) - Javascript runtime / Front - v10.7.0+
 * [Docker](https://docs.docker.com/engine/installation) - Docker Engine release - 17.12.0+
 * [Docker-compose](https://docs.docker.com/compose/install) - compose - 1.23+

## Important settings

If you run the full stack (gravitee api management & access management) in addition to the initial spring boot api & angular website,
then please make sure to follow these two below recommendations : 

_**#1:** Declare two domains into your /etc/hosts by running `sudo vi /etc/hosts` and add following value:_  
`127.0.0.1      apim.gravitee.io am.gravitee.io`


_**#2:** Upgrade default memory allocation to Docker For Mac (default 2, use at least 4 as described below)_  
![Alt text](docs/images/dockerForMacSettings.png "Docker for MAC settings")  

## Install & Run

For the first time run cmd `make init` else run cmd `make start` or have a look to command lines inside the Makefile.

## Play

 * API - Database access : [http://localhost:8880/console](http://localhost:8880/console)
 * API - Swagger specifications : [http://localhost:8880/swagger-ui.html](http://localhost:8880/swagger-ui.html)
 * Front - Single Page App Demo : [http://localhost:8888](http://localhost:8888)
 * Gravitee Access Management : [https://am.gravitee.io/ui](https://am.gravitee.io/ui)
 * Gravitee API Management : [https://apim.gravitee.io/portal/](https://apim.gravitee.io/portal/)

 * Call API through Gateway :  
`
curl -X POST \
  http://localhost:8082/confoo/authenticate/basic \
  -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989' \
  -H 'Authorization: Basic bHVzb2FsZXg6bHVzb2FsZXg='
`
## Contributing

Any contributions are more than welcome. But before please pay attention to :
 * [contributing.md](./contributing.md)
 * [code-of-conduct.md](./code-of-conduct.md)
 * [LICENSE](./LICENSE) applied by default unless specific sub folder license.

## Sessions

 * [2019 / Canada / Confoo.ca](https://confoo.ca/en/yul2019/session/securing-your-api-from-basics-to-beyond)
