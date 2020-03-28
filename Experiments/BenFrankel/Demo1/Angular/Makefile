.PHONY: help ## Print this help
 help:
	@echo Options
	@grep -E '^\.PHONY: [a-zA-Z_-]+ .*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = "(: |##)"}; {printf "\033[36m%-10s\033[0m %s\n", $$2, $$3}'

.PHONY: serve ## Run server on port 8080
serve:
	@ng serve --port 8080 --host 0.0.0.0 --disable-host-check > ng.log 2> ng.debug.log < /dev/null &
