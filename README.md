# WeDoo
Repositório destinado ao projeto final da disciplina de Desenvolvimento Android - Especialização Full Stack - UFG

O WeDoo têm o propósito de ajudar a organizar as tarefas e listas de deveres do dia-a-dia.
Crie seções de listas como: listas de estudos, lista de compras, atividades diárias, programações de itens a serem seguidos. Nós chamamos esses organizadores de "ToDoos".

Cada ToDoo pode ser composto por vários itens (ToDooItem). Você poderá associar a descrição desse item, adicionar uma imagem/fotografia e marcar o item como concluído/finalizado.

Para autenticação, utilizamos a plataforma do Firebase. Para armazenar imagens dos ToDooItens, utilizamos o Firebase File Storage.

Para armazenamento dos dados, usamos o SQLite. Sabemos que isso é um ponto negativo pois caso o aplicativo for desinstalado do dispositivo, os dados serão perdidos. Para sanar esse problema, podemos migrar para o armazenamento em nuvem, vinculando os dados com o usuário autenticado.

## Issues log, implementações futuras e pendentes
 - ~~Barra de busca de ToDoos;~~
 - Migrar a forma de armazenamento atual SQLite(stores data only on device) para Firebase Database(cloud database);
 - Notificar ToDooItem não concluído próximo ao prazo de expirar;
 - Permitir usuário criado via email e senha modificar sua foto de perfil da conta;
 - Agregar ToDoos para um grupo de usuários específicos;
 - Compartilhar ToDoos.
 - ~~About Activity.~~
