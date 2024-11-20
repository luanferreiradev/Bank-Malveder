CREATE DATABASE bank_malvader;
USE bank_malvader;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    documento VARCHAR(14), -- CPF ou CNPJ
    data_nascimento DATE,
    telefone VARCHAR(15),
    tipo_usuario ENUM('FUNCIONARIO', 'CLIENTE'),
    senha VARCHAR(50)
);

CREATE TABLE funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    codigo_funcionario VARCHAR(20),
    cargo VARCHAR(50),
    data_nascimento DATE,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    data_nascimento DATE,
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
    status ENUM('APROVADO', 'PENDENTE', 'REPROVADO'),
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

CREATE TABLE status_solicitacao (
    id_status INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50)
);

CREATE TABLE solicitacao (
    id_solicitacao INT AUTO_INCREMENT PRIMARY KEY,
    documento VARCHAR(14),
    senha VARCHAR(50),
    is_pessoa_fisica BOOLEAN,
    endereco VARCHAR(100),
    numero_casa VARCHAR(10),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    data_nascimento DATE,
    nome VARCHAR(100),
    telefone VARCHAR(15),
    tipo_conta ENUM('CORRENTE', 'POUPANCA'),
    id_status INT,
    FOREIGN KEY (id_status) REFERENCES status_solicitacao(id_status)
);

-- Procedure para criar um cliente
DELIMITER //
CREATE PROCEDURE criarCliente(
    IN p_nome VARCHAR(100),
    IN p_documento VARCHAR(14),
    IN p_data_nascimento DATE,
    IN p_telefone VARCHAR(15),
    IN p_senha VARCHAR(50),
    IN p_cep VARCHAR(10),
    IN p_local VARCHAR(100),
    IN p_numero_casa INT,
    IN p_bairro VARCHAR(50),
    IN p_cidade VARCHAR(50),
    IN p_estado VARCHAR(2)
)
BEGIN
    DECLARE v_id_usuario INT;
    INSERT INTO usuario (nome, documento, data_nascimento, telefone, tipo_usuario, senha)
    VALUES (p_nome, p_documento, p_data_nascimento, p_telefone, 'CLIENTE', p_senha);
    SET v_id_usuario = LAST_INSERT_ID();
    INSERT INTO cliente (data_nascimento, id_usuario) VALUES (p_data_nascimento, v_id_usuario);
    INSERT INTO endereco (cep, local, numero_casa, bairro, cidade, estado, id_usuario)
    VALUES (p_cep, p_local, p_numero_casa, p_bairro, p_cidade, p_estado, v_id_usuario);
END //
DELIMITER ;

-- Procedure para remover um cliente
DELIMITER //
CREATE PROCEDURE removerCliente(IN p_documento VARCHAR(14))
BEGIN
    DECLARE v_id_usuario INT;
    SELECT id_usuario INTO v_id_usuario FROM usuario WHERE documento = p_documento;
    DELETE FROM endereco WHERE id_usuario = v_id_usuario;
    DELETE FROM cliente WHERE id_usuario = v_id_usuario;
    DELETE FROM usuario WHERE id_usuario = v_id_usuario;
END //
DELIMITER ;

-- Procedure para criar uma conta
DELIMITER //
CREATE PROCEDURE criarConta(
    IN p_numero_conta VARCHAR(20),
    IN p_agencia VARCHAR(10),
    IN p_saldo DECIMAL(15, 2),
    IN p_tipo_conta ENUM('POUPANCA', 'CORRENTE'),
    IN p_status ENUM('APROVADO', 'PENDENTE', 'REPROVADO'),
    IN p_documento_cliente VARCHAR(14)
)
BEGIN
    DECLARE v_id_cliente INT;
    SELECT id_cliente INTO v_id_cliente FROM cliente c
    JOIN usuario u ON c.id_usuario = u.id_usuario
    WHERE u.documento = p_documento_cliente;
    INSERT INTO conta (numero_conta, agencia, saldo, tipo_conta, status, id_cliente)
    VALUES (p_numero_conta, p_agencia, p_saldo, p_tipo_conta, p_status, v_id_cliente);
END //
DELIMITER ;

-- Procedure para remover uma conta
DELIMITER //
CREATE PROCEDURE removerConta(IN p_numero_conta VARCHAR(20))
BEGIN
    DECLARE v_id_conta INT;
    SELECT id_conta INTO v_id_conta FROM conta WHERE numero_conta = p_numero_conta;
    DELETE FROM conta_corrente WHERE id_conta = v_id_conta;
    DELETE FROM conta_poupanca WHERE id_conta = v_id_conta;
    DELETE FROM conta WHERE id_conta = v_id_conta;
END //
DELIMITER ;

-- Procedure para criar um funcionário
DELIMITER //
CREATE PROCEDURE criarFuncionario(
    IN p_nome VARCHAR(100),
    IN p_documento VARCHAR(14),
    IN p_data_nascimento DATE,
    IN p_telefone VARCHAR(15),
    IN p_senha VARCHAR(50),
    IN p_cep VARCHAR(10),
    IN p_local VARCHAR(100),
    IN p_numero_casa INT,
    IN p_bairro VARCHAR(50),
    IN p_cidade VARCHAR(50),
    IN p_estado VARCHAR(2),
    IN p_codigo_funcionario VARCHAR(20),
    IN p_cargo VARCHAR(50)
)
BEGIN
    DECLARE v_id_usuario INT;
    INSERT INTO usuario (nome, documento, data_nascimento, telefone, tipo_usuario, senha)
    VALUES (p_nome, p_documento, p_data_nascimento, p_telefone, 'FUNCIONARIO', p_senha);
    SET v_id_usuario = LAST_INSERT_ID();
    INSERT INTO funcionario (codigo_funcionario, cargo, data_nascimento, id_usuario)
    VALUES (p_codigo_funcionario, p_cargo, p_data_nascimento, v_id_usuario);
    INSERT INTO endereco (cep, local, numero_casa, bairro, cidade, estado, id_usuario)
    VALUES (p_cep, p_local, p_numero_casa, p_bairro, p_cidade, p_estado, v_id_usuario);
END //
DELIMITER ;

-- Procedure para remover um funcionário
DELIMITER //
CREATE PROCEDURE removerFuncionario(IN p_codigo_funcionario VARCHAR(20))
BEGIN
    DECLARE v_id_usuario INT;
    SELECT id_usuario INTO v_id_usuario FROM funcionario WHERE codigo_funcionario = p_codigo_funcionario;
    DELETE FROM endereco WHERE id_usuario = v_id_usuario;
    DELETE FROM funcionario WHERE id_usuario = v_id_usuario;
    DELETE FROM usuario WHERE id_usuario = v_id_usuario;
END //
DELIMITER ;

-- Procedure para depósito
DELIMITER //
CREATE PROCEDURE deposito(IN p_numero_conta VARCHAR(20), IN p_valor DECIMAL(15, 2))
BEGIN
    UPDATE conta SET saldo = saldo + p_valor WHERE numero_conta = p_numero_conta;
    INSERT INTO transacao (tipo_transacao, valor, data_hora, id_conta)
    VALUES ('DEPOSITO', p_valor, NOW(), (SELECT id_conta FROM conta WHERE numero_conta = p_numero_conta));
END //
DELIMITER ;

-- Procedure para saque
DELIMITER //
CREATE PROCEDURE saque(IN p_numero_conta VARCHAR(20), IN p_valor DECIMAL(15, 2))
BEGIN
    UPDATE conta SET saldo = saldo - p_valor WHERE numero_conta = p_numero_conta;
    INSERT INTO transacao (tipo_transacao, valor, data_hora, id_conta)
    VALUES ('SAQUE', p_valor, NOW(), (SELECT id_conta FROM conta WHERE numero_conta = p_numero_conta));
END //
DELIMITER ;

-- Procedure para transferência
DELIMITER //
CREATE PROCEDURE transferencia(
    IN p_numero_conta_origem VARCHAR(20),
    IN p_numero_conta_destino VARCHAR(20),
    IN p_valor DECIMAL(15, 2)
)
BEGIN
    CALL saque(p_numero_conta_origem, p_valor);
    CALL deposito(p_numero_conta_destino, p_valor);
    INSERT INTO transacao (tipo_transacao, valor, data_hora, id_conta)
    VALUES ('TRANSFERENCIA', p_valor, NOW(), (SELECT id_conta FROM conta WHERE numero_conta = p_numero_conta_origem));
END //
DELIMITER ;

-- Procedure para ver saldo
DELIMITER //
CREATE PROCEDURE verSaldo(IN p_numero_conta VARCHAR(20))
BEGIN
    SELECT saldo FROM conta WHERE numero_conta = p_numero_conta;
END //
DELIMITER ;

-- Procedure para mostrar o limite
DELIMITER //
CREATE PROCEDURE mostrarLimite(IN p_numero_conta VARCHAR(20))
BEGIN
    SELECT limite FROM conta_corrente WHERE id_conta = (SELECT id_conta FROM conta WHERE numero_conta = p_numero_conta);
END //
DELIMITER ;

-- Procedure para mostrar o tipo de uma conta
DELIMITER //
CREATE PROCEDURE mostrarTipoConta(IN p_numero_conta VARCHAR(20))
BEGIN
    SELECT tipo_conta FROM conta WHERE numero_conta = p_numero_conta;
END //
DELIMITER ;

-- Procedure para alterar o tipo de uma conta
DELIMITER //
CREATE PROCEDURE alterarTipoConta(IN p_numero_conta VARCHAR(20), IN p_novo_tipo ENUM('POUPANCA', 'CORRENTE'))
BEGIN
    UPDATE conta SET tipo_conta = p_novo_tipo WHERE numero_conta = p_numero_conta;
END //
DELIMITER ;

-- Procedure para listar funcionários
DELIMITER //
CREATE PROCEDURE listarFuncionarios()
BEGIN
    SELECT * FROM funcionario;
END //
DELIMITER ;

-- Procedure para listar solicitações
DELIMITER //
CREATE PROCEDURE listarSolicitacoes()
BEGIN
    SELECT * FROM solicitacao;
END //
DELIMITER ;

-- Procedure para aprovar solicitação
DELIMITER //
CREATE PROCEDURE aprovarSolicitacao(IN p_id_solicitacao INT)
BEGIN
    UPDATE solicitacao SET id_status = (SELECT id_status FROM status_solicitacao WHERE descricao = 'APROVADO') WHERE id_solicitacao = p_id_solicitacao;
END //
DELIMITER ;

-- Procedure para rejeitar solicitação
DELIMITER //
CREATE PROCEDURE rejeitarSolicitacao(IN p_id_solicitacao INT)
BEGIN
    UPDATE solicitacao SET id_status = (SELECT id_status FROM status_solicitacao WHERE descricao = 'REPROVADO') WHERE id_solicitacao = p_id_solicitacao;
END //
DELIMITER ;

-- Procedure para buscar informações para gerar o extrato da conta
DELIMITER //
CREATE PROCEDURE buscarExtratoConta(IN p_numero_conta VARCHAR(20))
BEGIN
    SELECT * FROM transacao WHERE id_conta = (SELECT id_conta FROM conta WHERE numero_conta = p_numero_conta);
END //
DELIMITER ;

-- Procedure para buscar informações para o relatório de movimentações
DELIMITER //
CREATE PROCEDURE buscarRelatorioMovimentacoes(IN p_id_funcionario INT)
BEGIN
    SELECT * FROM relatorio WHERE id_funcionario = p_id_funcionario;
END //
DELIMITER ;