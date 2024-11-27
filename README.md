# Banco Malvader

Bem-vindo ao Banco Malvader! Este é um projeto de um sistema bancário desenvolvido em Java, utilizando SWIG, DAO, JDBC e MySQL. O objetivo deste projeto é fornecer uma solução simples e eficiente para gerenciamento de contas bancárias.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação principal.
- **SWIG**: Ferramenta para conectar programas em C/C++ com outras linguagens.
- **DAO (Data Access Object)**: Padrão de projeto para abstração de acesso a dados.
- **JDBC (Java Database Connectivity)**: API para conectar e executar consultas em bancos de dados.
- **MySQL**: Sistema de gerenciamento de banco de dados relacional.


## Repositório da SQL e Documentação em geral

O repositório com o script SQL para criação das tabelas do banco de dados pode ser encontrado [aqui](https://drive.google.com/drive/folders/1FvwPfoxxFtZUYC104MAScFR-12V0k3Ph?usp=sharing).

## Diagramas e Programa em .jar para Ser Executável

Comando para executar o programa: 
 ```java
   java -jar target/BancoMalvader-1.0-SNAPSHOT-jar-with-dependencies.jar
 ``

## Instalação

Para rodar o projeto em sua máquina, siga os passos abaixo:

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seuusuario/banco-malvader.git
   cd banco-malvader
   ```
2. **Importe o projeto**:
    - Abra o Eclipse.
    - Clique em `File > Import`.
    - Selecione `General > Existing Projects into Workspace`.
    - Clique em `Next`.
    - Selecione o diretório do projeto.
    - Clique em `Finish`.

3. **Configure o banco de dados**:
    - Certifique-se de ter o MySQL instalado e em execução.
    - Crie um banco de dados chamado banco_malvader.
    - Importe o arquivo de script SQL (se disponível) para criar as tabelas necessárias.

4. **Configuração do projeto**:
    - Altere as configurações de conexão no arquivo de configuração(ex: config.properties) para corresponder ao seu ambiente de banco de dados.

5. **Compile o projeto**:
    - Utilize seu IDE preferido ou um sistema de build como Maven ou Gradle.
6. **Execute o projeto**:
    - Execute a classe principal do projeto.

## Contribuidores

Agradecemos aos seguintes colaboradores que tornaram este projeto possível:

- [Luan Ferreira](https://github.com/luanferreiradev)
- [Adenilson Alves](https://github.com/eng-adenilson)
- [Victor Davidson](https://github.com/vctrdavidsom)

## Licença

Este projeto está licenciado sob a [MIT License](/LICENSE).

## Contato

Se você tiver alguma dúvida ou sugestão, sinta-se à vontade para entrar em contato com os criadores do projeto.
