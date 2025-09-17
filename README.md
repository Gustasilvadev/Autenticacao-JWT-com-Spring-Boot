# 🔐 Sistema de Autenticação JWT com Spring Boot

## 📋 DESCRIÇÃO DO PROJETO
<div class="center">
  Sistema completo de autenticação desenvolvido em **Java Spring Boot** utilizando **JSON Web Tokens (JWT)** para segurança de APIs RESTful. O sistema oferece controle de acesso granular baseado em roles com endpoints protegidos para diferentes níveis de permissão, garantindo segurança e flexibilidade na administração de usuários.
</div>

---

## 🚀 FUNCIONALIDADES PRINCIPAIS

<table>
  <tr>
    <td align="center" width="25%">
      <strong>👥 CADASTRO DE USUÁRIOS</strong><br>
      <small>Suporte a múltiplos níveis de acesso com roles pré-definidas</small>
    </td>
    <td align="center" width="25%">
      <strong>🔐 AUTENTICAÇÃO JWT</strong><br>
      <small>Tokens seguros com expiração configurável e assinatura digital</small>
    </td>
    <td align="center" width="25%">
      <strong>🎯 CONTROLE DE ACESSO</strong><br>
      <small>Autorização baseada em roles (USUARIO/ADMINISTRADOR)</small>
    </td>
    <td align="center" width="25%">
      <strong>🛡️ SPRING SECURITY</strong><br>
      <small>Proteção completa com filtros personalizados e políticas de segurança</small>
    </td>
  </tr>
</table>

---

## 🛠️ TECNOLOGIAS UTILIZADAS


<div align="center">

| **Tecnologia** | **Função Principal** |
|----------------|----------------------|
| ![Java](https://img.shields.io/badge/Java-21-orange) | Linguagem de programação principal |
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green) | Framework backend |
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue) | Autenticação e autorização |
| ![Auth0 Java JWT](https://img.shields.io/badge/Auth0%20Java%20JWT-4.4.0-purple) | Tokens de autenticação |
| ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue) | Banco de dados relacional |
| ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-3.x-lightgrey) | Persistência de dados |

</div>


---

## 🔌 ENDPOINTS DA API

### 🔓 ENDPOINTS PÚBLICOS (Sem autenticação)

<table>
  <tr>
    <th>Método</th>
    <th>Endpoint</th>
    <th>Descrição</th>
    <th>Body Request</th>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/usuario/criar</code></td>
    <td>Cria um novo usuário no sistema</td>
    <td>JSON com dados do usuário</td>
  </tr>
  <tr>
    <td>POST</td>
    <td><code>/usuario/autenticar</code></td>
    <td>Autentica usuário e retorna token JWT</td>
    <td>Credenciais (email/senha)</td>
  </tr>
</table>

### 🔐 ENDPOINTS PROTEGIDOS (Requerem autenticação)

<table>
  <tr>
    <th>Método</th>
    <th>Endpoint</th>
    <th>Role Requerida</th>
    <th>Descrição</th>
  </tr>
  <tr>
    <td>GET</td>
    <td><code>/usuario/teste/cliente</code></td>
    <td><code>ROLE_USUARIO</code></td>
    <td>Endpoint exclusivo para usuários comuns</td>
  </tr>
  <tr>
    <td>GET</td>
    <td><code>/usuario/teste/autenticacao</code></td>
    <td>Qualquer role autenticada</td>
    <td>Endpoint para qualquer usuário logado</td>
  </tr>
  <tr>
    <td>GET</td>
    <td><code>/usuario/teste/administrador</code></td>
    <td><code>ROLE_ADMINISTRADOR</code></td>
    <td>Endpoint exclusivo para administradores</td>
  </tr>
</table>

---

## ⚙️ CONFIGURAÇÃO

Antes de executar a aplicação, é ESSENCIAL popular o banco de dados com as roles necessárias.

## ⚠️ Configuração Inicial do Banco de Dados
Este sistema de autenticação requer que as roles sejam inseridas manualmente no banco de dados antes da primeira execução, pois a API não permite a criação de roles através de endpoints públicos.

## 🗂️ Script SQL para Popular o Banco
# Conectar ao MySQL (substitua com suas credenciais)
cd bin
cd mysql
mysql -u seu_usuario(root) -p

# Selecionar o banco de dados correto
USE bdautenticacao;

# Inserir as roles necessárias
INSERT INTO role (role_id, role_name) VALUES
(1, 'ROLE_USUARIO'),
(2, 'ROLE_ADMINISTRADOR');
