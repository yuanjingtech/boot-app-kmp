#!/bin/sh
brew install mise
brew --prefix mise
MISE_PATH=$(brew --prefix mise)
export PATH="$MISE_PATH/bin/:$PATH"
mise --version
mise install java@21

brew install openjdk@21
echo 'export PATH="/usr/local/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
