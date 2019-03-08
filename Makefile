#############################################################################################
###############################		        VARIABLES			   		###############################
#############################################################################################

# Retrieve current file name, must be done before doing "include .env" ...
makefile := $(MAKEFILE_LIST)

# include environment variable file (in order to avoid to duplicate them...)
include docker/.env

#############################################################################################
###############################		        COMMANDS			   		###############################
#############################################################################################

help: ## Print this message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(makefile) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

init: start restoreMongo ## Init project for the first time (else run make start)

start: ## Start all containers
	cd docker ; docker-compose up -d;

stop: ## Stop all containers
	cd docker; docker-compose stop;

status: ## Show status
	cd docker ; docker-compose ps;

down: ## Stop and remove containers, network...
	cd docker ; docker-compose down;

connectMongo: ## Connect to mongo repository on gravitee-am database
	@docker exec -ti ${GIO_MONGODB} mongo gravitee-am

restoreMongo: ## Restore mongodb data
	@docker cp docker/backup/mongodb ${GIO_MONGODB}:/tmp/dump_mongodb
	@docker exec ${GIO_MONGODB} mongorestore /tmp/dump_mongodb

dumpMongo: ## Dump mongodb data
	@docker exec -t ${GIO_MONGODB} rm -rf /tmp/dump_mongodb
	@docker exec -t ${GIO_MONGODB} mongodump --out /tmp/dump_mongodb
	@docker cp ${GIO_MONGODB}:/tmp/dump_mongodb docker/backup/mongodb





install: install-back install-front ## Install only

run: start-back start-front ## Run (without install)

install-back:
	cd back ; mvn clean package

install-front:
	cd front ; npm install

start-back:
	java -jar back/target/api-security-*.jar &

start-front:
	cd front ; npm start



.DEFAULT_GOAL := help
.PHONY: all test clean build version
