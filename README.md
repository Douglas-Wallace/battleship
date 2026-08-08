# ⚓ Batalha Naval: Multiplayer em Java

Jogo de Batalha Naval multiplayer (2 jogadores, via rede local) desenvolvido em Java como projeto acadêmico. Suporta interface em **Swing** (gráfica) ou **Console**, comunicação em tempo real via **TCP Sockets**, e persistência de histórico de partidas em arquivo.

> Projeto desenvolvido em dupla, aplicando arquitetura MVC, programação concorrente com Threads e protocolo de rede próprio para sincronizar o estado do jogo entre cliente e servidor.

---

## 🎮 Sobre o projeto

Cada jogador posiciona sua frota em um tabuleiro próprio e tenta afundar a frota do adversário atacando coordenadas às cegas. O jogo roda em duas instâncias conectadas por socket: uma atua como servidor e a outra como cliente, trocando mensagens serializadas a cada jogada até que um dos jogadores afunde toda a frota inimiga.

### Funcionalidades

- 🌐 **Multiplayer via TCP Sockets**, com uma instância hospedando (servidor) e outra se conectando (cliente)
- 🖥️ **Duas interfaces intercambiáveis**: gráfica (Swing) ou console (texto)
- 🚢 **Posicionamento de frota** com validação de sobreposição e limites do tabuleiro, incluindo suporte a 4 direções (Norte/Sul/Leste/Oeste)
- 🎯 **Sistema de turnos** com feedback de acerto, água e afundamento
- 📜 **Histórico de partidas** persistido em `historico.txt`, exibido ao abrir o jogo
- ⚠️ **Tratamento de exceções de domínio** (ex: `PosicionamentoInvalidoException`) para posicionamentos inválidos

---

## 🏗️ Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)**, com o controller centralizando toda a orquestração da partida, incluindo negociação de rede, alternância de turnos e sincronização entre os dois jogadores.

```
src/main/java/com/batalhanaval/
├── app/            → Ponto de entrada (Main)
├── controller/      → Orquestração do jogo (JogoController)
├── model/
│   ├── entities/     → Jogo, Jogador, Tabuleiro, Navio, Celula, Parte
│   ├── enums/        → Direcao, StatusCelula, TipoNavio
│   ├── exceptions/    → Exceções de domínio
│   └── util/         → CoordenadaParser, HistoricoPartida
├── network/          → Protocolo de comunicação (sockets)
└── view/             → BatalhaView, PosicionamentoView, ConsoleView, TabuleiroRenderer
```

Um dos maiores focos do desenvolvimento foi **manter a lógica de negócio fora das Views**: validações e regras de posicionamento que inicialmente estavam na camada gráfica foram refatoradas para o Model, mantendo a separação de responsabilidades do MVC mesmo em telas com bastante interação do usuário (Swing).

### Protocolo de rede

A comunicação usa `ObjectOutputStream`/`ObjectInputStream` para trocar objetos `Mensagem` serializáveis entre os dois jogadores:

```java
public class Mensagem implements Serializable {
    private final TipoMensagem tipo;   // INICIO, ATAQUE, RESULTADO, DERROTA, DESCONECTADO
    private Integer linha;
    private Integer coluna;
    private StatusCelula resultado;    // AGUA, ACERTOU, AFUNDOU, JA_ATACADO
}
```

Cada tipo de mensagem carrega apenas os dados necessários (uma coordenada de ataque, ou um resultado + coordenada de origem), mantendo o protocolo simples e explícito entre as pontas.

**Desafio técnico resolvido:** um deadlock de rede causado por leituras e escritas bloqueantes nos streams. A correção foi garantir `flush()` no stream de saída antes de qualquer leitura bloqueante do lado oposto, evitando que ambas as pontas ficassem esperando dados uma da outra indefinidamente.

---

## 🚀 Como rodar

### Pré-requisitos

- Java 25+ (JDK)
- Maven

### Compilar e executar

```bash
git clone https://github.com/<seu-usuario>/battleship.git
cd battleship
mvn compile exec:java -Dexec.mainClass="com.batalhanaval.app.Main"
```

### Jogando em dupla

1. Um jogador inicia o jogo e escolhe hospedar a partida (servidor)
2. O outro jogador inicia sua instância e se conecta ao IP do host (cliente)
3. Ambos posicionam sua frota
4. A partida começa: jogadores se alternam atacando coordenadas até um afundar toda a frota do outro

> **Nota:** para jogar entre máquinas em redes diferentes, pode ser necessário liberar a porta usada no firewall/antivírus (softwares como Avast podem bloquear a conexão de VPNs de rede como Radmin).

---

## 🧠 Principais aprendizados

Este projeto foi uma aplicação prática de conceitos que vão além da sintaxe da linguagem:

- **MVC na prática**: entender que "MVC" não é só organizar pastas, mas garantir que a View nunca tome decisões de regra de negócio. Um refatoramento real foi necessário para mover validações que "vazaram" para a camada gráfica de volta ao Model
- **Programação concorrente**: uso de Threads para que a escuta de mensagens de rede não bloqueasse a interface do jogo, e os cuidados necessários para evitar deadlocks em comunicação bidirecional por sockets
- **Design de protocolos**: criar um protocolo de mensagens (`Mensagem`/`TipoMensagem`) simples, tipado e extensível para sincronizar estado entre dois processos independentes
- **Debugging de rede real**: diagnosticar problemas de conectividade causados por fatores externos ao código (antivírus, VPN), não só bugs de lógica
- **Trabalho em dupla com Git**: fluxo de branches por integrante, revisão de decisões de design entre os desenvolvedores (ex: convenções de parsing de coordenadas, preferência por lambdas explícitas sobre method references)

---

## 🛠️ Tecnologias

- **Java 25**
- **Swing**: interface gráfica
- **TCP Sockets** (`java.net`): comunicação multiplayer
- **Threads** (`java.lang.Thread`): concorrência entre UI e rede
- **Java I/O** (`ObjectOutputStream`/`ObjectInputStream`, `BufferedReader`/`Writer`): protocolo de rede e persistência de histórico
- **Maven**: build e gerenciamento de dependências

---

## 👥 Autores

Projeto desenvolvido em dupla como trabalho acadêmico.

- [Douglas](https://github.com/Douglas-Wallace)
- [Isabella](https://github.com/IsabellaLimaa)

---

## 📄 Licença

Projeto acadêmico, sem licença de uso comercial definida.
