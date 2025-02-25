#include <iostream>
#include <stdlib.h>
#include "list_dynamic.h"
using namespace std;

void printList(tList L)
{
    tPosL i;
    int cnt = 1;
    for (i = first(L); i!=LNULL; i = next(i, L))
    {
        cout << cnt << ": " << i->Item << " (" << i << ")" << endl;
        ++cnt;
    }
}

int main()
{
    tList L; //L es una dirección de memoria random, y &L la dirección de memoria del nodo
    createEmptyList(&L); //se pasa la dirección de memoria y se le asigna valor nulo al valor que apunta a esa dirección, osea, L, ahora L tiene valor 0x0
    isEmptyList(L)? cout << "Vacia" << endl:cout << "No vacia" << endl;

    /*tPosL nodo; //el valor de nodo es una dirección de memoria, en createNode se pasa la dirección de memoria de nodo y se modifica el valor que apunta a esa dirección, es decir, el valor de nodo
    createNode(&nodo)? cout << "Nodo creado" << endl:cout << "Nodo no creado" << endl;*/

    insertItem(4, LNULL, &L); //se pasa la dirección de L, para cambiar el valor de L con (dentro de la funcion) *L, que es el valor que apunta a L, la dirección
    //Ahora el valor de L pasa de ser 0x0 a ser la dirección de memoria del nodo insertado, ya que la lista estaba vacía. Además, el item de L es 4 y su siguiente nulo, 0x0
    insertItem(7, LNULL, &L);
    insertItem(35, LNULL, &L);
    insertItem(2, LNULL, &L);
    insertItem(0, LNULL, &L);

    printList(L);
    //En definitiva, una variable tPosL almacena una dirección de memoria, y tiene ciertos atributos
    //tPosL utiliza 10 bytes de memoria, 4 para el entero

    system("pause");
    return 0;
}
