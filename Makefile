#############################################################################################
###############################		        VARIABLES			   		###############################
#############################################################################################

# Retrieve current file name, must be done before doing "include .env" ...
makefile := $(MAKEFILE_LIST)


#############################################################################################
###############################		        COMMANDS			   		###############################
#############################################################################################

help: ## Print this message
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(makefile) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

start: install start ## Install project then run (back & front)

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
