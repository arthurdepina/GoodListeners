# GoodListeners

[![Java CI with Maven](https://github.com/arthurdepina/GoodListeners/actions/workflows/ci-cd-pipeline.yml/badge.svg)](https://github.com/arthurdepina/GoodListeners/actions/workflows/ci-cd-pipeline.yml)

Esse projeto utiliza Java (com Maven) e SQLite. Seguem os pré-requisitos para desenvolvimento local da aplicação:

### Macbook

1. Certifique-se de que o Homebrew está instalado.

```bash
brew --version
```

Caso não esteja, instale através do comando:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

E verifique se a instalação foi bem sucedida com `brew --version`.

2. Instalando Maven

No terminal, execute:

```bash
brew install maven
```

E verifique a instalação com:

```bash
mvn -v
```

3. Instalando o SQLite

No terminal execute:

```bash
brew install sqlite
```

Verifique a instalação com:

```bash
sqlite3 --version
```

3. Build do Projeto

Com Maven instalado, clone o projeto:

```bash
git clone https://github.com/arthurdepina/GoodListeners.git
```

E realize a build do projeto com:

```bash
mvn clean install
```

Isso deve baixar todas as dependências e compilar o projeto. Certifique-se de que a build esteja passando antes de prosseguir.

4. Testes

É obrigatório verificar se todos os testes estão passando antes de abrir um PR. Para rodar os testes utilize:

```bash
mvn test
```

5. Opcional

Se for utilizar o VSCode, recomendo utilizar a extensão SQLite, para interagir com o banco diretamente no editor.
