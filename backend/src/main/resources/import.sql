INSERT INTO TB_CATEGORY (name, created_at) VALUES('Books', NOW() );
INSERT INTO TB_CATEGORY (name, created_at) VALUES('Electronics', NOW() );
INSERT INTO TB_CATEGORY (name, created_at) VALUES('Computers', NOW() );

/* Quando o atributo for do tipo camelCase o Spring cria uma coluna no BD com o nome separado por underline( _ ) */
/* Ex.: (name, createdAt) VALUES('Books', NOW() );  */
/* Erro na execucao: */
/* org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL "INSERT INTO TB_CATEGORY (name, createdAt) VALUES('Computers', NOW() )" via JDBC Statement */