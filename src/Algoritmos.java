import java.util.HashSet;

/**
 * Algoritmos usados no vídeo "Complexidade de Algoritmos na prática".
 *
 * IDEIA CENTRAL: em vez de só cronometrar (o que depende do computador),
 * cada método CONTA quantas vezes faz sua operação principal
 * (uma comparação ou um acesso). Esse número é o "passo".
 * Assim dá para ver como o trabalho CRESCE quando n (o tamanho da entrada) cresce.
 */
public class Algoritmos {

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
