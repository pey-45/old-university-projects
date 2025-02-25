//Algoritmos P3
//Autores:
//Pablo Manzanares López        pablo.manzanares.lopez@udc.es
//Alejandro Rodríguez Franco    alejandro.rodriguezf@udc.es
//Carlos Pérez Cambeiro         carlos.perez.cambeiro@udc.es

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <math.h>
#include <stdbool.h>

#define TAM 256000
#define MALLOC malloc(TAM*sizeof(int))

typedef struct monticulo
{
    int size;
    int vector[TAM];
} 
*pmonticulo;

//rand
void inicializar_semilla();
double microsegundos();
//initializations
void initializeRandom(int * v, int n);
void initializeAsc(int * v, int n);
void initializeDesc(int * v, int n);
//prints
void printArray(int * v, int n);
void tableTimes(void (*initialize) (int*, int), double n1, double n2, double n3);
//times
double getTimesCreate(int n);
double getTimesOrder(int n, void (*initialize) (int*, int));
//heap functions
void swap(int *a, int *b);
void initializeHeap(pmonticulo heap);
bool isEmptyHeap(pmonticulo heap);
void floatFun(int i, pmonticulo heap);
void insert(int x, pmonticulo heap);
void sink(int i, pmonticulo heap);
int removeMax(pmonticulo heap);
void createHeap(int * v, int n, pmonticulo heap);
void ordenarPorMonticulos(int * v, int n);
//tests
void testCreateHeap();
void testRemoveMax();
void testExecutionTime();
void testOrdenarPorMonticulos();
void testComplexities();

//rand
void inicializar_semilla() { srand(time(NULL)); }
double microsegundos()
{
    struct timeval t;

    if (gettimeofday(&t, NULL) < 0 ) return 0.;
    return (t.tv_usec + t.tv_sec * 1000000.);
}

//initializations
void initializeRandom(int * v, int n)
{
    int i, m = 2*n+1;
    for (i = 0; i < n; i++) 
        v[i] = (rand() % m) - n;
}
void initializeAsc(int * v, int n)
{
    int i;
    for (i = 0; i < n; i++)
        v[i] = i + 1;
}
void initializeDesc(int * v, int n)
{
    int i;
    for (i = 0; i < n; i++)
        v[i] = n - i;
}

//prints
void printArray(int * v, int n)
{
    int i;

    printf("[");
    for (i = 0; i < n; ++i) printf("%3d", v[i]);
    printf("  ]");
}
void tableTimes(void (*initialize) (int*, int), double n1, double n2, double n3)
{
    int i, j;
    const int n_times = 7, init = 500, limit = init * pow(2, n_times-1), alg_repeats = 3;
    long double t;

    printf("%9s %17s %11s%6.4f %11s%6.4f %11s%6.4f\n", "n", "t(n)", "t/n^", n1, "t/n^", n2, "t/n^", n3);
    for (i = 0; i<alg_repeats; i++)
    {
        for (j = init; j<=limit; j*=2)
        {
            t = getTimesOrder(j, initialize);
            printf("%3s %5d %17.6Lf %17.6Lf %17.6Lf %17.6Lf\n", t < 500? "(*)" : "", j, t, t/pow(j, n1), t/pow(j, n2), t/pow(j, n3));
        }
        printf("\n");
    }    
}

//times
double getTimesCreate(int n)
{
    double t, t_init;
    pmonticulo m = MALLOC;
    const int K = 1000, min_t = 500;
    int i, *v = malloc(n * sizeof(int*));

    initializeRandom(v, n);
    t = microsegundos();
    createHeap(v, n, m);
    t = microsegundos() - t;

    if (t < min_t)
    {
        t = microsegundos();
        for (i = 0; i < K; i++)
        {
            initializeRandom(v, n);
            createHeap(v, n, m);
        }
        t = microsegundos() - t;

        t_init = microsegundos();
        for (i = 0; i < K; i++) initializeRandom(v, n);
        t_init = microsegundos() - t_init;

        t = (t - t_init)/K;
    }

    free(m);
    free(v);
    return t;
}
double getTimesOrder(int n, void (*initialize) (int*, int))
{
    double t, t_init;
    const int K = 1000, min_t = 500;
    int i, *v = malloc(n * sizeof(int*));

    initialize(v, n);
    t = microsegundos();
    ordenarPorMonticulos(v, n);
    t = microsegundos() - t;

    if (t < min_t)
    {
        t = microsegundos();
        for (i = 0; i < K; i++)
        {
            initialize(v, n);
            ordenarPorMonticulos(v, n);
        }
        t = microsegundos() - t;

        t_init = microsegundos();
        for (i = 0; i < K; i++) initialize(v, n);
        t_init = microsegundos() - t_init;

        t = (t - t_init)/K;
    }

    free(v);
    return t;    
}

//heap functions
void swap(int *a, int *b) {
    int aux = *a;
    *a = *b;
    *b = aux;
}

void initializeHeap(pmonticulo heap) {
    heap->size = 0;
}

bool isEmptyHeap(pmonticulo heap) {
    return !heap->size;    
}

void floatFun(int i, pmonticulo heap) {
    while (i > 0 && heap->vector[i/2] < heap->vector[i]) {
        swap(&heap->vector[i/2], &heap->vector[i]);
        i = i/2;
    }
}

void insert(int x, pmonticulo heap) {
    if (heap->size == TAM) {
        printf("Error: montículo lleno.\n");
    } else {
        heap->size++;
        heap->vector[heap->size] = x;
        floatFun(heap->size, heap);
    }
}

void sink(int i, pmonticulo heap) {
    int left_child, right_child, j;

    do {
        left_child = 2*i+1;
        right_child = 2*i+2;
        j = i;

        if (right_child <= heap->size && heap->vector[right_child] > heap->vector[i]) {
            i = right_child;
        }
        if (left_child <= heap->size && heap->vector[left_child] > heap->vector[i]) {
            i = left_child;
        }
        swap(&heap->vector[i], &heap->vector[j]);
    } while (j!=i);
}

int removeMax(pmonticulo heap) {
    int x;

    if (isEmptyHeap(heap)) {
        printf("Error: montículo vacío.\n");
    } else {
        x = heap->vector[0];
        heap->vector[0] = heap->vector[heap->size-1];
        heap->size--;

        if (heap->size > 0) {
            sink(0, heap);
        }
        
        return x;
    }
}

void createHeap(int * v, int n, pmonticulo heap) {
    int i;

    for (i = 0; i < n; i++) {
        heap->vector[i] = v[i];
    }
    heap->size = n;

    for (i = heap->size/2; i >= 0; i--) {
        sink(i, heap);
    }
}
void ordenarPorMonticulos(int * v, int n)
{
    //se crea un montículo en base al vector
    pmonticulo m = MALLOC;
    createHeap(v, n, m);
    int i;

    //y se va quitando del monticulo el menor elemento en cada iteración y se añade al vector
    for (i = n-1; i >= 0; i--) v[i] = removeMax(m);

    free(m);
}

//tests
void testCreateHeap()
{
    pmonticulo m = MALLOC;
    int n = 9, *v = MALLOC;

    printf("------------------------\n  TEST CREAR MONTICULO\n------------------------\nVector desordenado: ");
    initializeRandom(v, n);
    printArray(v, n);
    createHeap(v, n, m);
    printf("\nVector del heap: ");
    printArray(m->vector, m->size);
    printf("\nTamaño: %d\n\n", m->size);
    printf("Vector ordenado ascendente: ");
    initializeAsc(v, n);
    printArray(v, n);
    createHeap(v, n, m);
    printf("\nVector del heap: ");
    printArray(m->vector, m->size);
    printf("\nTamaño: %d\n\n", m->size);
    printf("Vector ordenado descendente: ");
    initializeDesc(v, n);
    printArray(v, n);
    createHeap(v, n, m);
    printf("\nVector del heap: ");
    printArray(m->vector, m->size);
    printf("\nTamaño: %d\n\n", m->size);

    free(m);
    free(v);
}
void testRemoveMax()
{
    pmonticulo m = MALLOC;
    int n = 9, *v = MALLOC;
    initializeRandom(v, n);
    createHeap(v, n, m);

    printf("---------------------\n  TEST QUITAR MENOR\n---------------------\nVector inicial del montículo: ");
    printArray(m->vector, m->size);
    printf("\nElemento devuelto: %d\nVector final: ", removeMax(m));
    printArray(m->vector, m->size);
    printf("\n\n");

    free(m);
    free(v);
}
void testExecutionTime()
{
    int i, j;
    const int n_times = 8, init = 500, limit = init * pow(2, n_times-1), alg_repeats = 3;
    long double t;

    printf("-----------------------------------\n  TEST COMPLEJIDAD CREARMONTICULO\n-----------------------------------\n%9s %17s %17s\n", "n", "t(n)", "t/n");
    for (i = 0; i<alg_repeats; i++)
    {
        for (j = init; j<=limit; j*=2)
        {
            t = getTimesCreate(j);
            printf("%3s %5d %17.6Lf %17.6Lf\n", t < 500? "(*)" : "", j, t, t/j);
        }
        printf("\n");
    }

    printf("Se puede observar que t/n tiende a una constante por lo que este proceso tiene complejidad O(n)\n\n");
}
void testOrdenarPorMonticulos()
{
    int * v = MALLOC, n = 9;
    printf("---------------------------------\n  TEST ORDENACION POR MONTICULOS\n---------------------------------\nAleatorio:\n");
    initializeRandom(v, n);
    printArray(v, n);
    printf(" -> ");
    ordenarPorMonticulos(v, n);
    printArray(v, n);
    printf("\nDescendente:\n");
    initializeDesc(v, n);
    printArray(v, n);
    printf(" -> ");
    ordenarPorMonticulos(v, n);
    printArray(v, n);
    printf("\nAscendente:\n");
    initializeAsc(v, n);
    printArray(v, n);
    printf(" -> ");
    ordenarPorMonticulos(v, n);
    printArray(v, n);
    printf("\n\n");

    free(v);
}
void testComplexities()
{
    printf("----------------------\n  TEST COMPLEJIDADES\n----------------------\nAleatorio:\n\n");
    tableTimes(initializeRandom, 0.8, 1.1, 1.2);
    printf("Descendente:\n\n");
    tableTimes(initializeDesc, 0.8, 1.1, 1.2);
    printf("Ascendente:\n\n");
    tableTimes(initializeAsc, 0.8, 1.1, 1.2);
}


int main()
{
    testCreateHeap();
    testRemoveMax();
    testExecutionTime();
    testOrdenarPorMonticulos();
    testComplexities();

    return 0;
}