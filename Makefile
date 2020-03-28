backend_dir := Backend/backend
frontend_dir := Frontend

.PHONY: serve ## Start server (default)
serve: build
	cd $(backend_dir); ./mvnw spring-boot:run

.PHONY: build ## Generate files from source
build: frontend_build backend_build

.PHONY: clean ## Remove generated files
clean: frontend_clean backend_clean

.PHONY: frontend_build
frontend_build:
	$(MAKE) -C $(frontend_dir)

.PHONY: backend_build
backend_build:
	$(MAKE) -C $(backend_dir)

.PHONY: frontend_clean
frontend_clean:
	$(MAKE) -C $(frontend_dir) clean

.PHONY: backend_clean
backend_clean:
	$(MAKE) -C $(backend_dir) clean

.PHONY: help ## Print this help
help:
	@echo Options
	@grep -E '^\.PHONY: [a-zA-Z_-]+ .*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = "(: |##)"}; {printf "\033[36m%-10s\033[0m %s\n", $$2, $$3}'
