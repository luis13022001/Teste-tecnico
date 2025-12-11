Exemplo 1 – Propósito e correção
SELECT u.username, COUNT(a.id) AS total_logins
FROM usuarios u
LEFT JOIN auditoria_login a ON a.usuario_id = u.id AND a.sucesso = true
WHERE u.perfil = 'ADMIN'
GROUP BY u.username
HAVING COUNT(a.id) > 5
ORDER BY total_logins DESC;
Perguntas: 
1. Qual o propósito dessa consulta?
2. Há erros lógicos ou sintáticos?
3. Que tipo de cenário de teste você derivaria a partir dela?
Resposta: 
1- A consulta tem o proposito de listar todos os usuarios com perfil 'ADMIN' que fizeram mais de 5 logins com sucesso, mostrando o número total de logins bem-sucedidos de cada um, e ordenando do maior para o menor.
2- Nao há erro sintático a consulta esta valida. Erro logico eu escolheira trocar o LEFT JOIN pelo INNER JOIN, pois o uso de LEFT JOIN + HAVING COUNT(a.id) > 5 faz com que a cláusula LEFT JOIN se comporte mais como um INNER JOIN.
3- Ao todo pensei em 6 cenários:
    1-Usuários com logins suficientes: Um usuário ADMIN com 6 logins bem-sucedidos → deve aparecer na lista.
    2-Usuários com logins insuficientes: Um usuário ADMIN com 5 ou menos logins bem-sucedidos → não deve aparecer.
    3-Usuários sem logins: Um usuário ADMIN sem logins → não deve aparecer (confirma lógica do HAVING).
    4-Usuários com logins malsucedidos: Um usuário ADMIN com 10 logins, mas todos malsucedidos (sucesso = false) → não deve aparecer.
    5-Usuários de perfil diferente: Um usuário com perfil USER com 10 logins bem sucedidos → não deve aparecer.
    6-Ordem de exibição: Garantir que o resultado esteja em ordem decrescente de total_logins.


Exemplo 2 – Identificação de erro lógico
SELECT * FROM usuarios WHERE bloqueado = 'false';
Perguntas: 
1. O que há de errado nesta consulta no PostgreSQL?
2. Como corrigir?
3. Como um teste automatizado poderia detectar essa falha?
Resposta: 
1- O tipo boolean não deve ser comparado com uma string.
2- De forma direta apenas tirando as aspas: SELECT * FROM usuarios WHERE bloqueado = false;
3- Um teste automatizado poderia executar a consulta em dados de teste e verificar se os usuários retornados correspondem corretamente ao critério bloqueado = false; ou poderia validar o tipo da coluna e detectar comparações incorretas com string dentro do programa.


Exemplo 3 – Verificação de bloqueio
SELECT u.username, u.tentativas,
 CASE WHEN u.tentativas >= 3 THEN 'BLOQUEADO' ELSE 'ATIVO' END AS status
FROM usuarios u;
Perguntas: 
1. Descreva um cenário que resulte em “BLOQUEADO”;
2. Como validar a consistência após o teste;
3. Como limpar a base após o teste?
Resposta: ___

1- Qualquer usuário com tentativas >= 3 resultará em status = 'BLOQUEADO'.
2- Validar consistência comparando os valores de tentativas com o status esperado, usuários com tentativas <3 devem aparecer como ATIVO, ≥3 como BLOQUEADO.
3- Limpar a base após o teste revertendo alterações, deletando usuários de teste ou resetando os contadores de tentativas. O ideal é realizar os testes em uma base isolada.

Exemplo 4 – Erro conceitual em junção
SELECT u.username, a.data_evento
FROM usuarios u, auditoria_login a
WHERE u.id = a.usuario_id(+);
Perguntas: 
1. Qual o erro na sintaxe?
2. Como reescrever corretamente?
3. Que erro o PostgreSQL retornaria?
Resposta: 
1- O operador (+) não é suportado pelo PostgreSQL.
2- Vou utilizar o LEFT JOIN: 
SELECT u.username, a.data_evento
FROM usuarios u
LEFT JOIN auditoria_login a ON u.id = a.usuario_id;
3- PostgreSQL retornaria um erro de sintaxe: ERROR: syntax error at or near "(+)"


Exemplo 5 – Integridade e dados órfãos
SELECT a.id, a.usuario_id
FROM auditoria_login a
WHERE a.usuario_id NOT IN (SELECT id FROM usuarios);
Perguntas: 
1. Qual o propósito da consulta?
2. Como ela contribui para testes de integração?
3. Que cenário de teste validaria isso?
4. Como evitar o problema no banco?
Resposta:
1- O propósito é identificar registros em auditoria_login que não têm usuário correspondente.
2- Contribui para testes de integração ao validar a consistência entre tabelas relacionadas e detectar problemas de integridade referencial.
3- Deletar um usuário que possui logins e executar a query para confirmar que os registros órfãos aparecem.
4- Evitar o problema criando uma foreign key em auditoria_login.usuario_id referenciando usuarios.id, com ON DELETE CASCADE ou RESTRICT.



AUTOMAÇÃO-
OBS: Nao tinha API para abordar os erros -
- 403: Acesso negado;
- 423: Usuário bloqueado.
e nao tinha front para realizar os testes de login com sucesso no curto prazo de tempo.
Para rodar a automação separadamente 'mvn test -Dtest=LoginApiTests' para rodas os testes de API e 'mvn test -Dtest=LoginUITest' para rodas os cenários do front.