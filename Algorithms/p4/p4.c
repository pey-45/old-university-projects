//Algoritmos P4
//Autores:
//Pablo Manzanares López        pablo.manzanares.lopez@udc.es
//Alejandro Rodríguez Franco    alejandro.rodriguezf@udc.es
//Carlos Pérez Cambeiro         carlos.perez.cambeiro@udc.es

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <sys/time.h>
#include <math.h>
#include <stdbool.h>

#define TAM_MAX 1000

typedef int **matrix;

//rand
double microsegundos();
void dijkstra(matrix M, matrix distancias, int n);
void printMatrix(matrix m, int n);
int **createMatrix(int n);
void iniMatrix(matrix m, int n);
void freeMatrix(matrix m, int n);
double getTimes(int n);
void tableTimes(double n1, double n2, double n3, double theoric);


//rand
double microsegundos()
{
    struct timeval t;

    if (gettimeofday(&t, NULL) < 0) return 0.;
    return (t.tv_usec + t.tv_sec * 1000000.);
}

void dijkstra(matrix M, matrix distancias, int n)
{
    int i, j, k, nodo;
    bool noVisitados[n];

    for (i = 0; i < n; i++)
    {
        for (j = 0; j < n; j++)
        {
            distancias[i][j] = M[i][j];
            noVisitados[j] = true;
        }
        noVisitados[i] = false;

        for (j = 0; j < n - 2; j++)
        {
            nodo = -1;
            for (k = 0; k < n; k++)
            {
                if (noVisitados[k] && (nodo == -1 ||
                    distancias[i][k] < distancias[i][nodo]))
                    nodo = k;
            }
            if (nodo != -1)
                noVisitados[nodo] = false;

            for (k = 0; k < n; k++)
            {
                if (noVisitados[k] &&
                    distancias[i][k] > distancias[i][nodo] + M[nodo][k])
                    distancias[i][k] = distancias[i][nodo] + M[nodo][k];
            }
        }
    }
}

void printMatrix(matrix m, int n)
{
    int i, j;

    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++)
            printf("%d ", m[i][j]);
        printf("\n");
    }
}

matrix createMatrix(int n) {
    int i, **aux;
    if ((aux = malloc(n * sizeof(int *))) == NULL)
        return NULL;
    for (i = 0; i < n; i++)
        if ((aux[i] = malloc(n * sizeof(int))) == NULL)
            return NULL;
    return aux;
}

void iniMatrix(matrix m, int n) {
    int i, j;
    for (i = 0; i < n; i++)
        for (j = i + 1; j < n; j++)
            m[i][j] = rand() % TAM_MAX + 1;
    for (i = 0; i < n; i++)
        for (j = 0; j <= i; j++)
            m[i][j] = i==j? 0 : m[j][i];
}

void freeMatrix(matrix m, int n)
{
    int i;
    for (i = 0; i < n; i++)
        free(m[i]);
    free(m);
}

double getTimes(int n)
{
    double t, t_init;
    const int K = 1000, min_t = 500;
    int i;
    matrix m = createMatrix(n);
    matrix d = createMatrix(n);

    iniMatrix(m, n);
    iniMatrix(d, n);

    t = microsegundos();
    dijkstra(m, d, n);
    t = microsegundos() - t;

    if (t < min_t) {
        t = microsegundos();
        for (i = 0; i < K; i++)
        {
            iniMatrix(m, n);
            iniMatrix(d, n);
            dijkstra(m, d, n);
        }
        t = microsegundos() - t;

        t_init = microsegundos();
        for (i = 0; i < K; i++)
            dijkstra(m, d, n);
        t_init = microsegundos() - t_init;

        t = (t - t_init) / K;
    }

    return t;
}

void tableTimes(double n1, double n2, double n3, double theoric)
{
    int i, j;
    const int n_times = 5, init = 32, limit =
            (int) (init * pow(2, n_times - 1)), alg_repeats = 3;
    long double t;

    printf("%9s %17s %11s%6.4f %11s%6.4f %11s%6.4f %11s%6.4f\n", "n", "t(n)",
           "t/n^", n1, "t/n^", n2, "t/n^", n3, "t/n^", theoric);
    for (i = 0; i < alg_repeats; i++)
    {
        for (j = init; j <= limit; j *= 2)
        {
            t = getTimes(j);
            printf("%3s %5d %17.6Lf %17.6Lf %17.6Lf %17.6Lf %17.6Lf\n",
                   t < 500 ? "(*)" : "", j, t, t / pow(j, n1),
                   t / pow(j, n2), t / pow(j, n3), t/pow(j, theoric));
        }
        printf("\n");
    }
}

void testDijkstra()
{
    //asigno memoria a las matrices
    matrix m1 = createMatrix(5), m2 = createMatrix(4), d1 = createMatrix(5),
    d2 = createMatrix(4);

    //creo matrices auxiliares con los valores
    int i, j, aux_m1[5][5] =
            {{0, 1, 8, 4, 7},{1, 0, 2, 6, 5},{8, 2, 0, 9, 5},
             {4, 6, 9, 0, 3},{7, 5, 5, 3, 0}},
             aux_m2[4][4] =
            {{0, 1, 4, 7},{1, 0, 2, 8},{4, 2, 0, 3},{7, 8, 3, 0}};

    //copio los valores en las matrices del heap
    for (i = 0; i < 5; i++) for (j = 0; j < 5; j++)
    { m1[i][j] = aux_m1[i][j]; d1[i][j] = 0; }

    for (i = 0; i < 4; i++) for (j = 0; j < 4; j++)
    { m2[i][j] = aux_m2[i][j]; d2[i][j] = 0; }

    dijkstra(m1, d1, 5);
    dijkstra(m2, d2, 4);

    printf("-----------------\n  TEST DIJKSTRA\n-----------------\n");
    printf("Matriz de adyacencia 1:\n");
    printMatrix(m1, 5);
    printf("Distancias minimas 1:\n");
    printMatrix(d1, 5);

    printf("Matriz de adyacencia 2:\n");
    printMatrix(m2, 4);
    printf("Distancias minimas 2:\n");

    printMatrix(d2, 4);
    freeMatrix(m1, 5);
    freeMatrix(m2, 4);
    freeMatrix(d1, 5);
    freeMatrix(d2, 4);
}

void testComplexities()
{
    printf("\n----------------------\n  TEST COMPLEJIDADES\n"
           "----------------------\n");
    tableTimes(2.75, 2.95, 3.15, 3);
}


int main()
{
    testDijkstra();
    testComplexities();
}