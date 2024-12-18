# GoodListeners

[![Java CI with Maven](https://github.com/arthurdepina/GoodListeners/actions/workflows/ci-cd-pipeline.yml/badge.svg)](https://github.com/arthurdepina/GoodListeners/actions/workflows/ci-cd-pipeline.yml)

Disponível em: https://goodlisteners.onrender.com/home.html (WIP)

Esse projeto utiliza Java Springboot (com Maven) e SQLite.
O deploy foi feito no [Render](https://render.com/), utilizando Docker.

Seguem os pré-requisitos para desenvolvimento local da aplicação:

## MacOS

### 1. Certifique-se de que o Homebrew está instalado.

```bash
brew --version
```

Caso não esteja, instale através do comando:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

E verifique se a instalação foi bem sucedida com `brew --version`.

### 2. Instalando Maven

No terminal, execute:

```bash
brew install maven
```

E verifique a instalação com:

```bash
mvn -v
```

### 3. Instalando o SQLite

No terminal execute:

```bash
brew install sqlite
```

Verifique a instalação com:

```bash
sqlite3 --version
```

### 4. Build do Projeto

Com Maven instalado, clone o projeto:

```bash
git clone https://github.com/arthurdepina/GoodListeners.git
```

E realize a build do projeto com:

```bash
mvn clean install
```

Isso deve baixar todas as dependências e compilar o projeto. Certifique-se de que a build esteja passando antes de prosseguir.

### 5. Docker

Comandos úteis:

```bash
# Parar e remover container atual
docker rm -f goodlisteners

# Construir nova imagem
docker build --platform linux/amd64 -t goodlisteners-app .

# Rodar novo container com variáveis de ambiente definidas explicitamente
docker run -d --name goodlisteners \
  -p 8080:8080 \
  -v "$(pwd)/data:/data" \
  -e DATABASE_PATH=/data/goodlisteners.db \
  --platform linux/amd64 \
  goodlisteners-app

# Verificar o filesystem do container.
docker exec goodlisteners ls -la /data
docker exec goodlisteners ls -la /app/data
```

### 6. Opcional

Se for utilizar o VSCode, recomendo utilizar a extensão SQLite, para interagir com o banco diretamente no editor.

## Windows

### 1. Instalando Maven

Baixe o Maven em: https://maven.apache.org/download.cgi

Extraia o conteúdo e adicione o caminho para o diretório bin do Maven nas variáveis de ambiente do sistema:

* Vá para Configurações do Sistema → Variáveis de Ambiente.
* Na seção Variáveis do Sistema, edite a variável Path e adicione o caminho para a pasta bin do Maven.

Verifique a instalação abrindo o Prompt de Comando e rodando:

```bash
mvn -v
```

### 2. Instalando o SQLite

Baixe o SQLite de: https://www.sqlite.org/download.html

Adicione o caminho para o executável sqlite3.exe nas variáveis de ambiente, similar ao Maven.

Verifique a instalação com:

```bash
sqlite3 --version
```

### 3. Build do Projeto

Clone o repositório com

```bash
git clone https://github.com/arthurdepina/GoodListeners.git
```

Realize a build executando na raiz do projeto:

```bash
mvn clean install
```

### 4. Docker

Comandos úteis:

```bash
# Parar e remover container atual
docker rm -f goodlisteners

# Construir nova imagem
docker build --platform linux/amd64 -t goodlisteners-app .

# Rodar novo container com variáveis de ambiente definidas explicitamente
docker run -d --name goodlisteners \
  -p 8080:8080 \
  -v "$(pwd)/data:/data" \
  -e DATABASE_PATH=/data/goodlisteners.db \
  --platform linux/amd64 \
  goodlisteners-app

# Verificar o filesystem do container.
docker exec goodlisteners ls -la /data
docker exec goodlisteners ls -la /app/data
```
### 5. Opcional

Se for utilizar o VSCode, recomendo utilizar a extensão SQLite, para interagir com o banco diretamente no editor.

## Linux

### 1. Instalando o Maven

Utilize o gerenciador de pacotes do seu sistema:

No Ubuntu/Debian:

```bash
sudo apt update
sudo apt install maven
```

Verifique a instalação com:

```bash
mvn -v
```

### 2. Instalando o SQLite

Para instalar:

```
sudo apt update
sudo apt install sqlite3
```

Teste com:

```
sqlite3 --version
```

### 3. Build do projeto

Clone o projeto com:

```
git clone https://github.com/arthurdepina/GoodListeners.git
```

Realize a build executando o seguinte comando na raiz do projeto.

```
mvn clean install
```

### 4. Docker

Comandos úteis:

```bash
# Parar e remover container atual
docker rm -f goodlisteners

# Construir nova imagem
docker build --platform linux/amd64 -t goodlisteners-app .

# Rodar novo container com variáveis de ambiente definidas explicitamente
docker run -d --name goodlisteners \
  -p 8080:8080 \
  -v "$(pwd)/data:/data" \
  -e DATABASE_PATH=/data/goodlisteners.db \
  --platform linux/amd64 \
  goodlisteners-app

# Verificar o filesystem do container.
docker exec goodlisteners ls -la /data
docker exec goodlisteners ls -la /app/data
```
