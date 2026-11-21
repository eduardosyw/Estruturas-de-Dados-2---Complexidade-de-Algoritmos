# Complexidade de Algoritmos (Big-O, melhor/médio/pior caso)

Trabalho de **Estruturas de Dados 2** – **Unipê** – **Prof. Edkallenn**

**Integrantes:** Eduardo Ramalho Teixeira e Renan de Araújo Venturini Silva

**Vídeo:** [cole aqui o link do YouTube]

---

## Sobre o tema

Complexidade de algoritmos é o jeito de prever **como o trabalho de um algoritmo cresce quando a entrada cresce**, sem depender da velocidade do computador. Usamos a notação **Big-O** para descrever esse crescimento e analisamos o **melhor, o médio e o pior caso**.

Neste projeto, cada algoritmo **conta seus próprios passos** (comparações/acessos) e também mede o tempo, para comparar a teoria com o que acontece de verdade.

| Complexidade | Algoritmo no código | Analogia usada no vídeo |
|---|---|---|
| O(1) | `acessarMeio` | Abrir o armário pelo número |
| O(log n) | `buscaBinaria` | Jogo "é maior ou menor?" |
| O(n) | `buscaLinear` | Procurar alguém na fila, um por um |
| O(n²) | `temDuplicataLento` | Todo mundo da turma apertar a mão de todo mundo |
| O(n) em média | `temDuplicataRapido` (HashSet) | Lista de "já vi esse" |
| O(n) a O(n²) | `insertionSort` | Arrumar cartas de baralho na mão |

## Estrutura

```
ed2-complexidade-algoritmos/
├── README.md
└── src/
    ├── Algoritmos.java   ← os algoritmos, comentados, com contador de passos
    └── Main.java         ← menu com os 4 experimentos do vídeo
```

## Como compilar e executar

Requisito: **Java 11 ou superior** (testado no Java 21).

```bash
# 1. Clonar o repositório
git clone [link-do-repositorio]
cd ed2-complexidade-algoritmos

# 2. Compilar
javac -encoding UTF-8 -d bin src/*.java

# 3. Executar
java -cp bin Main
```

Também funciona abrindo a pasta no IntelliJ ou VS Code e rodando a classe `Main`.


## Experimentos do menu

1. **Corrida: O(1) × O(log n) × O(n) × O(n²)**: mesmo n para todos (1.000 até 1.000.000). Mostra passos e tempo. O O(n²) com 1.000.000 não é executado (levaria minutos); o valor é calculado.
2. **Melhor, médio e pior caso**: Insertion Sort com o array já ordenado, aleatório e invertido, para n = 10.000 e 20.000.
3. **Jogo da adivinhação**: você pensa em um número e o programa acerta usando busca binária (de 1 a 1.000.000 em no máximo 20 tentativas). Responda `+` (maior), `-` (menor) ou `=` (acertou).
4. **Consertando o app**: detectar matrículas repetidas com dois laços O(n²) e com HashSet O(n), para 40, 10.000 e 100.000 alunos.

## Exemplo de saída (experimento 1)

Os passos são sempre iguais; os tempos variam conforme o computador.

```
PASSOS (operações contadas) - pior caso: o valor procurado NÃO existe
        n |  O(1) | O(log n) |      O(n) |             O(n²)
----------+-------+----------+-----------+------------------
    1.000 |     1 |       10 |     1.000 |          499.500
   10.000 |     1 |       14 |    10.000 |       49.995.000
  100.000 |     1 |       17 |   100.000 |    4.999.950.000
1.000.000 |     1 |       20 | 1.000.000 |  499.999.500.000 *
```

## Conceitos principais

- **Big-O (O)**: limite superior do crescimento. "No máximo cresce assim".
- **Ω (Ômega)**: limite inferior. **Θ (Theta)**: limite justo (superior e inferior ao mesmo tempo).
- **Melhor/médio/pior caso** são *situações de entrada*; Big-O é *notação*. Dá para falar "o pior caso do Insertion Sort é O(n²)" e "o melhor caso é O(n)".
- Ignoramos constantes e termos menores: `3n² + 5n + 2` → **O(n²)**.
- Para n pequeno, as constantes podem pesar mais que o crescimento (no experimento 4, com 40 alunos, a versão O(n²) pode ser mais rápida).
- O HashSet troca **memória** por **tempo**: é mais rápido, mas guarda uma cópia dos valores.
