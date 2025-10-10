# AGIMOB 2025: Simulador e Guia de Financiamento Imobiliário

## 🏠 Visão Geral do Projeto

O **Agimob** é uma aplicação web robusta, desenvolvida para ser a sua principal ferramenta na jornada do financiamento imobiliário. Nosso objetivo é duplo:
1.  Oferecer um **simulador de financiamentos imobiliários** preciso, suportando os tipos **SAC** (Sistema de Amortização Constante) e **PRICE** (Tabela Price), baseando-se em uma taxa Agi.
2.  **Desmistificar o tema** de financiamento imobiliário, apresentando explicações claras e de fácil entendimento para usuários em todos os níveis de conhecimento.

Desenvolvido para ser escalável e agradável, o Agimob é a porta de entrada para quem busca clareza e simulações realistas antes de realizar o sonho da casa própria.

---

## 🎯 Objetivos

* **Democratizar o Acesso:** Tornar o tema de financiamento imobiliário acessível e compreensível para todos os usuários.
* **Simulação de Confiança:** Ser um simulador **com resultados corretos e úteis** para quem deseja planejar a compra de um imóvel.
* **Fidelização e Atração:** Apresentar os benefícios e as taxas competitivas do **Banco Agibank** para atrair e fidelizar usuários.

---

## 🛠️ Especificações Técnicas

### Tecnologias Utilizadas

A aplicação foi construída utilizando um *stack* moderno e eficiente, garantindo **escalabilidade** e uma excelente **experiência do usuário (UX)**.

| Área | Tecnologia | Propósito |
| :--- | :--- | :--- |
| **Backend** | **Java** + **Spring Boot** | Desenvolvimento da API e regras de negócio. |
| **Frontend** | **JavaScript** + **HTML** + **CSS** | Construção da interface web interativa (Mobile e Desktop). |
| **Banco de Dados** | **Aiven** / **DBeaver** | Gerenciamento e acesso aos dados. |
| **Documentação API** | **Swagger** | Documentação e testes da API. |
| **Ferramentas de Desenvolvimento** | **Git**, **Postman/Imnsonia** | Controle de versão e testes de API. |
| **Gestão do Projeto** | **JIRA** | Rastreamento e gestão de tarefas. |

### Metodologia de Projeto

Adotamos a metodologia **Scrum** para garantir um desenvolvimento **rápido**, **adaptável a mudanças** e com **comunicação frequente**. O projeto é dividido em *Sprints* curtas, focando na entrega contínua de valor e na melhoria iterativa do produto.

---

## 💡 Manual do Sistema e Funcionalidades

A aplicação está estruturada em colunas de fácil navegação, oferecendo tanto o guia quanto o simulador.

### 1. Coluna de Explicações - Financiamento Imobiliário

Conteúdo simples e didático para entender os termos essenciais:

* **Amortização:** Entenda como a dívida é paga ao longo do tempo.
* **Sistema PRICE:** Explicação sobre o sistema de **parcelas fixas** e como os juros se comportam.
* **Sistema SAC:** Explicação sobre o sistema de **amortização constante** e a redução das parcelas ao longo do tempo.
* **Funcionamento dos Juros:** Como os juros são calculados na sua prestação.

### 2. Coluna do Simulador (O Core da Aplicação)

O usuário pode efetuar simulações completas baseadas nas seguintes entradas:

#### Entradas do Usuário

* **Valor do Imóvel**
* **Valor da Entrada**
* **Prazo de Financiamento** (em anos)
* **Renda Bruta Mensal** (do proponente principal)
* **Incluir Participante:** Opção para adicionar a Renda Bruta Mensal de um segundo participante.
* **Sistema de Financiamento:** SAC ou PRIC.

#### Regras de Negócio Chave

* **Prazo Máximo:** 420 meses (35 anos).
* **Prazo Mínimo:** 380 meses (31 anos).
* **Taxas de Juros:**
    * **AgiBank:** 9% **(Sobre variação atráves do consumo de API do Score do Usuário)**
  

#### Saídas para o Usuário

* **Relatório Detalhado:** Tabela de Amortização completa para o sistema SAC e/ou PRICE.
* **Exportação:** Opção para **Exportar PDF** ou **Enviar por E-mail** a simulação.

### 3. Outras Colunas

* **Por Que Escolher o Simulador AGI:** Vantagens e diferenciais da nossa ferramenta.
* **Benefícios Agi:** Destaque para atrativos do Agibank, como **Seguro Incluído Gratuitamente**, **Parcelas Flexíveis**, **Possibilidade de Repactuação** e **Seguro Prestamista Embutido**.
* **Sobre:** Informações de contato (e-mail, telefone) e detalhes sobre a equipe.

---

## 🧑‍💻 Público Alvo

Nosso público são pessoas que buscam entender e planejar um financiamento imobiliário. Isso inclui desde o **primeiro comprador** sem experiência no assunto, até **investidores** que buscam a melhor taxa e sistema de amortização.
*(Detalhes completos das **Personas** estão disponíveis na documentação interna do projeto.)*

---

## 📄 Documentações e Artefatos

Aqui você encontrará links para os principais artefatos de documentação do projeto:

* **Diagramas:** [Link para Diagramas (em breve)]
* **Protótipo/Wireframe:** [Link para o Protótipo no Figma/Marvel (em breve)]
* **Documento de Requisitos:** [Link para o Documento (em breve)]
