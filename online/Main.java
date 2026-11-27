// VERSÃO DE ARQUIVO ÚNICO para compiladores online (OnlineGDB, Programiz, JDoodle).
// É o mesmo código de src/Main.java + src/Algoritmos.java, juntos em um arquivo só.
// Cole tudo no compilador online (linguagem: Java) e clique em Run.

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Programa de demonstração do vídeo "Complexidade de Algoritmos na prática".
 * Trabalho de Estruturas de Dados 2 - Unipê - Prof. Ed.
 *
 * Menu com 4 experimentos:
 *   1 - Corrida: como O(1), O(log n), O(n) e O(n²) crescem quando n aumenta
 *   2 - Melhor, médio e pior caso do Insertion Sort
 *   3 - Jogo: o computador adivinha seu número com busca binária
 *   4 - Consertando o app: detectar matrículas repetidas O(n²) x O(n)
 */
public class Main {

    // Formato brasileiro: 1.000.000 e 0,123
    static final Locale BR = Locale.forLanguageTag("pt-BR");
    static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        aquecerJVM();

        int opcao;
        do {
            System.out.println();
            System.out.println("============ COMPLEXIDADE DE ALGORITMOS NA PRÁTICA ============");
            System.out.println("1 - Corrida: O(1) x O(log n) x O(n) x O(n²)");
            System.out.println("2 - Melhor, médio e pior caso (Insertion Sort)");
            System.out.println("3 - Jogo: eu adivinho o seu número (busca binária)");
            System.out.println("4 - Consertando o app: matrículas repetidas O(n²) x O(n)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1: corrida(); break;
                case 2: casosInsertionSort(); break;
                case 3: jogoAdivinhacao(); break;
                case 4: consertandoApp(); break;
                case 0: System.out.println("Até mais!"); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    // =================================================================
    // EXPERIMENTO 1 - A CORRIDA
    // Mesmo n para todos. Procuramos um valor que NÃO existe (pior caso).
    // Observe: quando n fica 10x maior, quanto cresce cada coluna?
    // =================================================================
    static void corrida() {
        int[] tamanhos = {1_000, 10_000, 100_000, 1_000_000};

        long[][] passos = new long[tamanhos.length][4];
        double[][] tempos = new double[tamanhos.length][4];

        for (int k = 0; k < tamanhos.length; k++) {
            int n = tamanhos[k];
            int[] v = criarOrdenado(n);   // ordenado e sem repetidos
            // O array tem 0..n-1, então n nunca é encontrado. Como n é maior
            // que todos, a busca binária vai sempre para a direita: pior caso.
            int alvoInexistente = n;

            // O(1)
            Algoritmos.passos = 0;
            long t0 = System.nanoTime();
            Algoritmos.acessarMeio(v);
            tempos[k][0] = ms(System.nanoTime() - t0);
            passos[k][0] = Algoritmos.passos;

            // O(log n)
            Algoritmos.passos = 0;
            t0 = System.nanoTime();
            Algoritmos.buscaBinaria(v, alvoInexistente);
            tempos[k][1] = ms(System.nanoTime() - t0);
            passos[k][1] = Algoritmos.passos;

            // O(n)
            Algoritmos.passos = 0;
            t0 = System.nanoTime();
            Algoritmos.buscaLinear(v, alvoInexistente);
            tempos[k][2] = ms(System.nanoTime() - t0);
            passos[k][2] = Algoritmos.passos;

            // O(n²) - só roda até 100.000; para 1.000.000 demoraria minutos
            if (n <= 100_000) {
                System.out.printf(BR, "Rodando O(n²) com n = %,d...%n", n);
                Algoritmos.passos = 0;
                t0 = System.nanoTime();
                Algoritmos.temDuplicataLento(v);
                tempos[k][3] = ms(System.nanoTime() - t0);
                passos[k][3] = Algoritmos.passos;
            } else {
                // Estimativa: n 10x maior -> O(n²) fica 10² = 100x mais lento
                passos[k][3] = (long) n * (n - 1) / 2;   // fórmula exata do aperto de mãos
                tempos[k][3] = tempos[k - 1][3] * 100;
            }
        }

        System.out.println();
        System.out.println("PASSOS (operações contadas) - pior caso: o valor procurado NÃO existe");
        System.out.println("        n |  O(1) | O(log n) |      O(n) |             O(n²)");
        System.out.println("----------+-------+----------+-----------+------------------");
        for (int k = 0; k < tamanhos.length; k++) {
            String marca = tamanhos[k] > 100_000 ? " *" : "  ";
            System.out.printf(BR, "%,9d | %5d | %8d | %,9d | %,16d%s%n",
                    tamanhos[k], passos[k][0], passos[k][1], passos[k][2], passos[k][3], marca);
        }

        System.out.println();
        System.out.println("TEMPO (milissegundos)");
        System.out.println("        n |  O(1) | O(log n) |      O(n) |             O(n²)");
        System.out.println("----------+-------+----------+-----------+------------------");
        for (int k = 0; k < tamanhos.length; k++) {
            String marca = tamanhos[k] > 100_000 ? " *" : "  ";
            System.out.printf(BR, "%,9d | %5.3f | %8.3f | %9.3f | %,16.1f%s%n",
                    tamanhos[k], tempos[k][0], tempos[k][1], tempos[k][2], tempos[k][3], marca);
        }
        System.out.println("* O(n²) com n = 1.000.000 NÃO foi executado: valor CALCULADO/ESTIMADO.");
        System.out.printf(BR, "  Estimativa: ~%.1f minutos só para checar repetidos!%n",
                tempos[tamanhos.length - 1][3] / 60_000);
        System.out.println("  (Tempos muito pequenos oscilam a cada execução; os PASSOS não mudam.)");
    }

    // =================================================================
    // EXPERIMENTO 2 - MELHOR, MÉDIO E PIOR CASO
    // O MESMO algoritmo, com o MESMO n, só muda a ORDEM da entrada.
    // =================================================================
    static void casosInsertionSort() {
        int[] tamanhos = {10_000, 20_000};

        System.out.println();
        System.out.println("INSERTION SORT - mesma quantidade de dados, entradas diferentes");
        System.out.println("     n | entrada                  |      comparações |  tempo (ms)");
        System.out.println("-------+--------------------------+------------------+------------");

        for (int n : tamanhos) {
            rodarInsertion(n, "MELHOR caso: já ordenado", criarOrdenado(n));
            rodarInsertion(n, "caso MÉDIO: aleatório", criarAleatorio(n));
            rodarInsertion(n, "PIOR caso: ordem inversa", criarInvertido(n));
            System.out.println("-------+--------------------------+------------------+------------");
        }
        System.out.println("Repare: dobrando n, o MELHOR caso dobra (O(n)) e o PIOR caso quadruplica (O(n²)).");
    }

    static void rodarInsertion(int n, String descricao, int[] v) {
        Algoritmos.passos = 0;
        long t0 = System.nanoTime();
        Algoritmos.insertionSort(v);
        double tempo = ms(System.nanoTime() - t0);
        System.out.printf(BR, "%,6d | %-24s | %,16d | %,10.3f%n", n, descricao, Algoritmos.passos, tempo);
    }

    // =================================================================
    // EXPERIMENTO 3 - JOGO DA ADIVINHAÇÃO (busca binária "ao vivo")
    // Você pensa em um número e o programa chuta sempre o MEIO do
    // intervalo que sobrou. Cada resposta descarta metade das opções.
    // =================================================================
    static void jogoAdivinhacao() {
        System.out.print("Até que número você quer jogar? (ex.: 100 ou 1000000): ");
        int limite = lerInteiro();
        if (limite < 1) {
            System.out.println("O limite precisa ser pelo menos 1.");
            return;
        }

        // Máximo de tentativas = quantas vezes dá para dividir o limite por 2
        // até acabar (= parte inteira de log2(limite) + 1).
        int maximo = 0;
        int resto = limite;
        while (resto > 0) {
            maximo++;
            resto = resto / 2;
        }

        System.out.printf(BR, "Pense em um número de 1 a %,d.%n", limite);
        System.out.printf(BR, "Eu acerto em no máximo %d tentativas.%n", maximo);
        System.out.println("Responda: +  (é MAIOR)   -  (é MENOR)   =  (ACERTOU)");

        int inicio = 1;
        int fim = limite;
        int tentativas = 0;

        while (inicio <= fim) {
            int chute = inicio + (fim - inicio) / 2;   // sempre o meio
            tentativas++;
            System.out.printf(BR, "Tentativa %d: é %,d? ", tentativas, chute);
            String resposta = entrada.next().trim();

            if (resposta.equals("=")) {
                System.out.printf(BR, "Acertei em %d tentativas! (máximo possível: %d)%n", tentativas, maximo);
                return;
            } else if (resposta.equals("+")) {
                inicio = chute + 1;      // descarta a metade de baixo
            } else if (resposta.equals("-")) {
                fim = chute - 1;         // descarta a metade de cima
            } else {
                System.out.println("Use apenas +, - ou =");
                tentativas--;            // não conta tentativa com resposta inválida
            }
        }
        System.out.println("Hmm... não sobrou nenhum número. Alguém respondeu errado no caminho!");
    }

    // =================================================================
    // EXPERIMENTO 4 - CONSERTANDO O APP QUE TRAVOU
    // Verificar se há matrículas repetidas em uma lista de n alunos.
    // Pior caso: nenhuma repetida (precisa olhar tudo).
    // =================================================================
    static void consertandoApp() {
        int[] tamanhos = {40, 10_000, 100_000};

        System.out.println();
        System.out.println("CHECAR MATRÍCULAS REPETIDAS (nenhuma repetida = pior caso)");
        System.out.println("      n | versão                  |        passos |  tempo (ms)");
        System.out.println("--------+-------------------------+---------------+------------");

        for (int n : tamanhos) {
            int[] matriculas = criarMatriculas(n);

            Algoritmos.passos = 0;
            long t0 = System.nanoTime();
            Algoritmos.temDuplicataLento(matriculas);
            double tLento = ms(System.nanoTime() - t0);
            System.out.printf(BR, "%,7d | %-23s | %,13d | %,10.3f%n", n, "LENTA: 2 laços  O(n²)", Algoritmos.passos, tLento);

            Algoritmos.passos = 0;
            t0 = System.nanoTime();
            Algoritmos.temDuplicataRapido(matriculas);
            double tRapido = ms(System.nanoTime() - t0);
            System.out.printf(BR, "%,7d | %-23s | %,13d | %,10.3f%n", n, "RÁPIDA: HashSet  O(n)", Algoritmos.passos, tRapido);
            System.out.println("--------+-------------------------+---------------+------------");
        }
        System.out.println("Com 40 alunos (uma turma) as duas são instantâneas - a 'lenta' pode até ganhar:");
        System.out.println("para n pequeno, as constantes pesam mais que o crescimento.");
        System.out.println("Com 100.000 alunos, o crescimento manda. Isso é análise assintótica!");
    }

    // =================================================================
    // FUNÇÕES AUXILIARES
    // =================================================================

    // Array 0, 1, 2, ..., n-1 (ordenado e sem repetidos)
    static int[] criarOrdenado(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) v[i] = i;
        return v;
    }

    // Array n-1, ..., 2, 1, 0 (ordem inversa)
    static int[] criarInvertido(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) v[i] = n - 1 - i;
        return v;
    }

    // Array com os valores 0..n-1 embaralhados (semente fixa = mesmo resultado sempre)
    static int[] criarAleatorio(int n) {
        int[] v = criarOrdenado(n);
        Random rnd = new Random(42);
        for (int i = n - 1; i > 0; i--) {        // embaralhamento de Fisher-Yates
            int j = rnd.nextInt(i + 1);
            int tmp = v[i];
            v[i] = v[j];
            v[j] = tmp;
        }
        return v;
    }

    // Matrículas no estilo 2026100000, 2026100001, ... embaralhadas e sem repetidas
    static int[] criarMatriculas(int n) {
        int[] v = criarAleatorio(n);
        for (int i = 0; i < n; i++) v[i] = 2026100000 + v[i];
        return v;
    }

    // Converte nanossegundos em milissegundos
    static double ms(long nanos) {
        return nanos / 1_000_000.0;
    }

    // Lê um número inteiro do teclado sem quebrar se a pessoa digitar letras
    static int lerInteiro() {
        while (!entrada.hasNextInt()) {
            entrada.next();
            System.out.print("Digite um número: ");
        }
        return entrada.nextInt();
    }

    // O Java fica mais rápido depois que um trecho roda algumas vezes
    // (o compilador JIT otimiza o código "quente"). Rodamos tudo antes,
    // sem mostrar nada, para as medições ficarem mais justas.
    static void aquecerJVM() {
        int[] v = criarOrdenado(2_000);
        for (int r = 0; r < 200; r++) {
            Algoritmos.acessarMeio(v);
            Algoritmos.buscaBinaria(v, -1);
            Algoritmos.buscaLinear(v, -1);
            Algoritmos.temDuplicataLento(v);
            Algoritmos.temDuplicataRapido(v);
            Algoritmos.insertionSort(criarAleatorio(500));
        }
        Algoritmos.passos = 0;
    }
}



/**
 * Algoritmos usados no vídeo "Complexidade de Algoritmos na prática".
 *
 * IDEIA CENTRAL: em vez de só cronometrar (o que depende do computador),
 * cada método CONTA quantas vezes faz sua operação principal
 * (uma comparação ou um acesso). Esse número é o "passo".
 * Assim dá para ver como o trabalho CRESCE quando n (o tamanho da entrada) cresce.
 */
class Algoritmos {

    // Contador global de passos. O Main zera ele (passos = 0) antes de cada teste.
    public static long passos = 0;

    // ------------------------------------------------------------------
    // O(1) - TEMPO CONSTANTE
    // Acessar uma posição do array é direto (como abrir o armário nº 50).
    // Não importa se o array tem 10 ou 10 milhões de elementos: 1 passo.
    // ------------------------------------------------------------------
    public static int acessarMeio(int[] v) {
        passos++;
        return v[v.length / 2];
    }

    // ------------------------------------------------------------------
    // O(n) - TEMPO LINEAR
    // Olha os elementos um por um, do início ao fim, até achar o alvo.
    //   Melhor caso: o alvo é o 1º elemento  -> 1 passo        -> O(1)
    //   Pior caso:   é o último ou não existe -> n passos       -> O(n)
    //   Caso médio:  em média olha metade      -> n/2 passos     -> O(n)
    // ------------------------------------------------------------------
    public static int buscaLinear(int[] v, int alvo) {
        for (int i = 0; i < v.length; i++) {
            passos++;                    // 1 comparação
            if (v[i] == alvo) {
                return i;                // achou: devolve a posição
            }
        }
        return -1;                       // percorreu tudo e não achou
    }

    // ------------------------------------------------------------------
    // O(log n) - TEMPO LOGARÍTMICO
    // ATENÇÃO: só funciona se o array estiver ORDENADO.
    // A cada passo olha o meio e DESCARTA METADE do que sobrou
    // (igual ao jogo "pensei num número, é maior ou menor?").
    // n = 1.000.000 -> no máximo 20 passos.
    // ------------------------------------------------------------------
    public static int buscaBinaria(int[] v, int alvo) {
        int inicio = 0;
        int fim = v.length - 1;

        while (inicio <= fim) {          // enquanto ainda houver onde procurar
            passos++;                    // 1 "olhada" no meio
            // Jeito seguro de achar o meio: (inicio + fim) / 2 pode
            // estourar o limite do int quando os índices são enormes.
            int meio = inicio + (fim - inicio) / 2;

            if (v[meio] == alvo) {
                return meio;             // achou
            } else if (v[meio] < alvo) {
                inicio = meio + 1;       // alvo é maior: joga fora a metade esquerda
            } else {
                fim = meio - 1;          // alvo é menor: joga fora a metade direita
            }
        }
        return -1;                       // o intervalo ficou vazio: não existe
    }

    // ------------------------------------------------------------------
    // O(n²) - TEMPO QUADRÁTICO
    // Verifica se há valores repetidos comparando CADA elemento com
    // TODOS os que vêm depois (como todo mundo da turma apertar a mão
    // de todo mundo). Dois laços, um dentro do outro.
    // Pior caso (sem repetidos): n*(n-1)/2 comparações -> O(n²)
    // ------------------------------------------------------------------
    public static boolean temDuplicataLento(int[] v) {
        for (int i = 0; i < v.length; i++) {
            for (int j = i + 1; j < v.length; j++) {   // j começa em i+1: não compara 2 vezes o mesmo par
                passos++;                // 1 comparação
                if (v[i] == v[j]) {
                    return true;         // achou um repetido
                }
            }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // O(n) EM MÉDIA - a "correção" do app usando HashSet
    // O HashSet responde "já vi esse valor?" em O(1) no caso médio.
    // Percorremos o array uma vez só -> O(n) no caso médio.
    // TROCA: gastamos mais MEMÓRIA (o HashSet) para economizar TEMPO.
    // ------------------------------------------------------------------
    public static boolean temDuplicataRapido(int[] v) {
        HashSet<Integer> vistos = new HashSet<>();
        for (int i = 0; i < v.length; i++) {
            passos++;                    // 1 consulta no HashSet
            if (vistos.contains(v[i])) {
                return true;             // já tinha visto: é repetido
            }
            vistos.add(v[i]);            // guarda para as próximas consultas
        }
        return false;
    }

    // ------------------------------------------------------------------
    // INSERTION SORT - o MESMO algoritmo com casos muito diferentes
    // Funciona como arrumar cartas de baralho na mão: pega a próxima
    // carta e vai empurrando para a esquerda até achar o lugar dela.
    //   Melhor caso (já ordenado):  ~n comparações     -> O(n)
    //   Caso médio (aleatório):     ~n²/4 comparações  -> O(n²)
    //   Pior caso (ordem inversa):  ~n²/2 comparações  -> O(n²)
    // ------------------------------------------------------------------
    public static void insertionSort(int[] v) {
        for (int i = 1; i < v.length; i++) {
            int atual = v[i];            // a "carta" que vamos encaixar
            int j = i - 1;               // começa comparando com a da esquerda

            while (j >= 0) {
                passos++;                // 1 comparação
                if (v[j] > atual) {
                    v[j + 1] = v[j];     // empurra a maior para a direita
                    j--;
                } else {
                    break;               // achou o lugar: para de comparar
                }
            }
            v[j + 1] = atual;            // encaixa a carta no lugar certo
        }
    }
}
