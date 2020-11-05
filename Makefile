SHELL=/bin/bash -x

.PHONY: default
default: | help

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: update-all
update-all: ## Checkout/pull code modules
	@./git-update.sh

.PHONY: status-all
status-all: ## Check local changes
	@./git-status.sh

.PHONY: build-asciidoc
build-asciidoc: ## Build asciidoc
	@asciidoctor -a allow-uri-read -b html5 -a linkcss README.adoc -D target/
	@mkdir -p ./target/images/
	@cp -r ./docs/images/* target/images/

.PHONY: open-asciidoc
open-asciidoc: ## Open asciidoc in browser
	@open target/README.html
