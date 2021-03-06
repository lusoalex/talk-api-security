<h1>
  <p align="center">
    Securing an API - From basics to beyond
  </p>
</h1>
<p align="center">
  This repository is used to accompany and illustrate some security vulnerabilities highlighted during the <a href="https://alexandrefaria.net#talks" target="_blank"><b><i>Securing an API - From basics to beyond</i></b></a> talk.<br>
  <i>It has been archived as it only serve the talk purpose and nothing else.</i>
  <br><br>
</p>


**Table of Contents**

  - [Requirements](#requirements)
  - [Architecture](#architecture)
  - [Important settings](#important-settings)
  - [Install & Run](#install--run)
  - [Usefull links](#usefull-links)
  - [Play with API & login page](#play-with-api--login-page)
    - [SQL injection](#sql-injection)
    - [JWT Brute force](#jwt-brute-force)
  - [Play with Oauth2](#play-with-oauth2)
    - [redirect_uri control](#redirect_uri-control)
  - [Play with API Management](#play-with-api-management)
    - [Security](#security)
    - [Rate limiting & quota](#rate-limiting--quota)
      - [Install wrk command](#install-wrk-command)
      - [Run wrk command](#run-wrk-command)
    - [Circuit breaker](#circuit-breaker)
  - [Contributing](#contributing)
  - [Talk sessions](#talk-sessions)


## Requirements

 * [GIT](https://git-scm.com) - Seems obvious right... - 2.20.1+
 * [Docker](https://docs.docker.com/engine/installation) - Docker Engine release - 17.12.0+
 * [Docker-compose](https://docs.docker.com/compose/install) - compose - 1.23+


 * [Maven](https://maven.apache.org/) - Dependency Management / Back - 3.3.9+
 * [Java](https://www.java.com) - Compilation & runtime / Back - Java 8+
 * [Npm](https://www.npmjs.com/) - Dependency Management / Front - 6.8.0+
 * [NodeJs](https://nodejs.org/en/) - Javascript runtime / Front - v10.7.0+

## Architecture
We have four stacks for this demo :
 * A web application - _[Angular](https://angular.io/)_
 * An API - _[Spring boot](https://spring.io/projects/spring-boot)_
 * An Authorization Server - _[Gravitee Acces Management](https://gravitee.io)_
 * An API Management - _[Gravitee API Management](https://gravitee.io)_

![Alt text](docs/images/architecture_overview.png "Architecture overview")

## Important settings

If you run the full stack (gravitee api management & access management) in addition to the initial spring boot api & angular website,
then please make sure to follow these two below recommendations :

_**#1:** Declare two domains into your /etc/hosts by running `sudo vi /etc/hosts` and add following value:_  
`127.0.0.1      apim.gravitee.io am.gravitee.io`


_**#2:** Upgrade default memory allocation to Docker For Mac (default 2, use at least 4 as described below)_  

![Alt text](docs/images/dockerForMacSettings.png "Docker for MAC settings")  

## Install & Run

For the first time run cmd `make init` else run cmd `make start` or have a look to command lines inside the Makefile.

## Usefull links

 * Front - Single Page App Demo : [http://localhost:8888](http://localhost:8888) - lusoalex/lusoalex
 * API - Swagger specifications : [http://localhost:8880/swagger-ui.html](http://localhost:8880/swagger-ui.html)
 * API - Database access : [http://localhost:8880/console](http://localhost:8880/console) - _jdbc:h2:mem:testdb_
 * Gravitee Access Management : [https://am.gravitee.io/ui](https://am.gravitee.io/ui) - admi/adminadmin
 * Gravitee API Management : [https://apim.gravitee.io/portal/](https://apim.gravitee.io/portal/) - admin/admin
 * Traefik Dashboard : [http://localhost:8080/dashboard/](http://localhost:8080/dashboard/)

## Play with API & login page

You can import this into your postman (collection v2.1) : _(click on below link)_  
[![Postman collection](https://www.getpostman.com/img/logos/postman/header-treatment.svg "Postman collection")](docs/postman/talk_api_security.postman_collection.json)

### SQL injection

Target here is only to show an example of injection which is currently the number 1 in the [OWASP](https://owasp.org) top 10. (latest owasp report was 2017)  

We will inject some sql in the form field (password field). A common injection is to input `something' or 1=1 --`, in our case, for the demo effect, we will use instead `something' or username='admin' --`  

### JWT Brute force

By exploring the browser console, we can see that a JWT token is saved on the local storage.  
By decoding (BASE64) the token _or do it thanks to https://www.jwt.io_, we see that the token use HS256 encryption.  
HS256 is symetric encryption, i will then use a tool to try to get the shared secret. I use here [brendan rius jwt-cracker](https://github.com/brendan-rius/c-jwt-cracker), this tool can find secret when they are too small, which is our case.   
You can find others tool with different way of finding the shared secret, like trying all password included in a file instead of doing brut force

![Alt text](docs/images/jwtCrack.png "brute force shared secret on Json Web Token")

Once we have the secret we can generate by ourself a token, use it to replace the current local storage token.  
Just need to refresh and see what happens...

## Play with Oauth2

Current webapp is using implicit or code flow oauth2 flow.  
There's plenty of oauth2 hacks that happens in past years (facebook, google, github...), i will focus on redirect_uri.  

### redirect_uri control

My expectations here is only to show that we must always do exact matching on redirect, avoiding regex, wildcard & so on.  
The client settings has a https://localhost:8888/login* redirect_uri, you can then change the redirect_uri with http://localhost:8888/login/../hack  


## Play with API Management

By restoring current mongo settings, the above API will be exposed through the API Management gateway on port 8082 and path /confoo  

 * Call API through Gateway :  
`
curl -X GET http://localhost:8082/confoo/users -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989'
`

 * Call API through Gateway :  
`
curl -X GET http://localhost:8082/confoo/users/f90df750-3069-48e6-8df7-50306988e691 -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989'
`

### Security

I used a white list on the API endpoint to allow only **/users** and **/users/** endpoint access and **GET http verb** only and forbid all others paths.  
If you try to access to confoo/authenticate/basic, you'll get a 403 with api key, and 401 without api key.

 * Path not white listed API endpoint :  
`
curl -X POST http://localhost:8082/confoo/authenticate/basic -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989' -H 'Authorization: Basic bHVzb2FsZXg6bHVzb2FsZXg='
`

 * Path ok but not the http verb :
`
curl -X DELETE http://localhost:8082/confoo/users/f90df750-3069-48e6-8df7-50306988e691 -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989'
`

### Rate limiting & quota

Target here is to see what happens if we exceed the number of allowed api calls.  
We'll run a wrk command to trigger many request in a short time and see what happens.  
_**this is not a benchmark...**_

#### Install [wrk](https://github.com/wg/wrk) command

 * On Linux : [documentation](https://github.com/wg/wrk/wiki/Installing-Wrk-on-Linux)
 * On Mac OS : [documentation](https://github.com/wg/wrk/wiki/Installing-wrk-on-OS-X)

#### Run wrk command

Generate many requests for 10 seconds, using 12 threads, and keeping 400 HTTP connections open.  

``
wrk -t12 -c400 -d10s http://localhost:8082/confoo/users -H 'X-Gravitee-Api-Key: 4d58b5f6-88dc-4253-8f80-a70aca9d2989'
``  
![Alt text](docs/images/wrk.png "rate limit excedeed")

Most of the request will fall in 429 due to _Rate limit exceeded ! You reach the limit of 10 requests per 1 seconds"_,
we have 6791 requests for 6692 failures, mean 100 request succeed.  
_I select an asynchronous rate limit to reduce the latency overhead, but we can enable it in synchronous..._

### Circuit breaker

We will stop the API container to see what happens when we try to access to the api through the api management gateway.  
Run cmd `docker stop TALK_SECURITY_BACK_DEMO`  
As a consequence, the health check of the API will show an unavailability.  
![Alt text](docs/images/health_check.png "API Management API status")  
By doing the curl, you'll get a 503 (Service Unavailable) response. The gateway will not send the request to the API until the healh check is healthy.  
That means it let the API try to reboot and reply to the request immediately (better than a timeout).


## Contributing

Any contributions are more than welcome. But before please pay attention to :
 * [contributing.md](./contributing.md)
 * [code-of-conduct.md](./code-of-conduct.md)
 * [LICENSE](./LICENSE) applied by default unless specific sub folder license.

## Talk sessions

 * [2019 / Canada - Montréal / Confoo.ca](https://confoo.ca/en/yul2019/session/securing-your-api-from-basics-to-beyond)
 * 2019 / France - Lille / Club Tech @Euratech
 * [2019 / France - Lille / Ch'ti JUG](https://www.meetup.com/fr-FR/ChtiJUG/events/264501689)
 * [2019 / France - Nancy / Lorraine JUG](https://www.lorrainejug.org/annonce/2019/09/30/secure-your-API-from-basics-to-beyond.html)
