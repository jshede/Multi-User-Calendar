frontend_dir := ../../Frontend

.PHONY: build ## Generate files from source (default)
build:
	mkdir -p src/main/resources/{META-INF/resources/{js,css,img},templates}
	find $(frontend_dir)/dist -name '*.js' -exec cp {} src/main/resources/META-INF/resources/js \;
	find $(frontend_dir)/dist -name '*.css' -exec cp {} src/main/resources/META-INF/resources/css \;
	find $(frontend_dir)/dist \( -name '*.png' -o -name '*.jpg' -o -name '*.ico' -o -name '*.svg' \) -exec cp {} src/main/resources/META-INF/resources/img \;
	find $(frontend_dir)/dist -name '*.html' -exec cp {} src/main/resources/templates \;
	find src/main/resources/templates -name '*.html' -exec sh -c 'sed -e '\''s/"\([^"\/]*\.js\)"/"js\/\1"/g'\'' {} >{}.new && mv -- {}.new {}' \;
	find src/main/resources/templates -name '*.html' -exec sh -c 'sed -e '\''s/"\([^"\/]*\.css\)"/"css\/\1"/g'\'' {} >{}.new && mv -- {}.new {}' \;
	find src/main/resources/templates -name '*.html' -exec sh -c 'sed -e '\''s/"\([^"\/]*\.(png|jpg|ico|svg)\)"/"img\/ \1"/g'\'' {} >{}.new && mv -- {}.new {}' \;
	./mvnw -N io.takari:maven:wrapper
	./mvnw package

.PHONY: serve ## Start server
serve: build
	./mvnw spring-boot:run

.PHONY: clean ## Remove generated files
clean:
	rm -rf src/main/resources/META-INF
	rm -rf src/main/resources/templates
	rm -rf target

.PHONY: help ## Print this help
help:
	@echo Options
	@grep -E '^\.PHONY: [a-zA-Z_-]+ .*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = "(: |##)"}; {printf "\033[36m%-10s\033[0m %s\n", $$2, $$3}'
