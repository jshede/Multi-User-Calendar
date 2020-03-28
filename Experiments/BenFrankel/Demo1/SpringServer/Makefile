.PHONY: help ## Print this help
 help:
	@echo Options
	@grep -E '^\.PHONY: [a-zA-Z_-]+ .*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = "(: |##)"}; {printf "\033[36m%-10s\033[0m %s\n", $$2, $$3}'

.PHONY: serve ## Run server in the background
serve:
	@nohup mvn spring-boot:run > spring.log 2> spring.debug.log < /dev/null &
