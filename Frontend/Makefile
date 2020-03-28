src_dirs := $(shell find src -type d)
src_files := $(shell find src -type f -name '*')

.PHONY: build ## Generate files from source (default)
build: dist

dist: $(src_dirs) $(src_files)
	ng build --prod --optimization

.PHONY: serve ## Start server
serve:
	ng serve

.PHONY: clean ## Remove generated files
clean:
	rm -rf dist

.PHONY: help ## Print this help
help:
	@echo Options
	@grep -E '^\.PHONY: [a-zA-Z_-]+ .*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = "(: |##)"}; {printf "\033[36m%-10s\033[0m %s\n", $$2, $$3}'
