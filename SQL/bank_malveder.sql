CREATE TABLE bank_malvader
USE bank_malvader;
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    cpf VARCHAR(11),
    data_nascimento DATE,
    telefone VARCHAR(15),
    tipo_usuario ENUM('FUNCIONARIO', 'CLIENTE'),
    senha VARCHAR(50)
);

CREATE TABLE funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    codigo_funcionario VARCHAR(20),
    cargo VARCHAR(50),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE endereco (
    id_endereco INT AUTO_INCREMENT PRIMARY KEY,
    cep VARCHAR(10),
    local VARCHAR(100),
    numero_casa INT,
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE conta (
    id_conta INT AUTO_INCREMENT PRIMARY KEY,
    numero_conta VARCHAR(20),
    agencia VARCHAR(10),
    saldo DECIMAL(15, 2),
    tipo_conta ENUM('POUPANCA', 'CORRENTE'),
    id_cliente INT,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE conta_corrente (
    id_conta_corrente INT AUTO_INCREMENT PRIMARY KEY,
    limite DECIMAL(15, 2),
    data_vencimento DATE,
    id_conta INT,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

CREATE TABLE conta_poupanca (
    id_conta_poupanca INT AUTO_INCREMENT PRIMARY KEY,
    taxa_rendimento DECIMAL(5, 2),
    id_conta INT,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

CREATE TABLE transacao (
    id_transacao INT AUTO_INCREMENT PRIMARY KEY,
    tipo_transacao ENUM('DEPOSITO', 'SAQUE', 'TRANSFERENCIA'),
    valor DECIMAL(15, 2),
    data_hora TIMESTAMP,
    id_conta INT,
    FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
);

CREATE TABLE relatorio (
    id_relatorio INT AUTO_INCREMENT PRIMARY KEY,
    tipo_relatorio VARCHAR(50),
    data_geracao TIMESTAMP,
    conteudo TEXT,
    id_funcionario INT,
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario)
);
