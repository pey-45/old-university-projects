#include <stdlib.h>
#include <stdbool.h>

#define LNULL NULL

typedef int tItemL; //los datos de la lista son enteros

typedef struct tNode *tPosL; //se define que tPosL apunta a la estructura tNode

struct tNode
{
    tItemL Item;
    tPosL sig;
};

typedef tPosL tList; //tList se considera la primera posición de la lista





bool createNode(tPosL *p)
{
    *p = (tPosL)malloc(sizeof(struct tNode));
    return *p != LNULL;
}

void createEmptyList(tList *L) //L se pasó como &L, por tanto es una dir. de memoria
{
    *L = LNULL; //a la dirección se le asigna valor NULO
}

bool isEmptyList(tList L)
{
    return L==LNULL;
}

bool insertItem(tItemL item, tPosL p, tList *L)
{
    tPosL q; //elemento a insertar

    if (!createNode(&q)) //si no se puede crear el nodo no se podrá insertar, a la vez se crea si se pudiera
        return false;
    else
    {
        q->Item = item;
        q->sig = LNULL;

        if (isEmptyList(*L)) //se comprueba si la lista está vacía
        {
            *L = q;
        }
        else if (p==LNULL) //si la posición dada es nula
        {
            tPosL i;
            for (i = *L; i->sig != LNULL; i = i->sig);
            i->sig = q;
        }
        else if (p==*L)
        {
            q->sig = *L;
            q = *L;
        }
        else
        {
            q->Item = p->Item;
            p->Item = item;
            q->sig = p->sig;
            p->sig = q;
        }
        return true;
    }
}

void updateItem(tItemL item, tPosL p, tList* L)
{
    p->Item = item;
}

tPosL findItem(tItemL item,tList L)
{
    tPosL i;
    for (i = L; i!=LNULL && i->Item!=item; i = i->sig);

    return i;
}

tItemL getItem(tPosL p, tList L)
{
    return p->Item;
}

tPosL first(tList L)
{
    return L;
}

tPosL last(tList L)
{
    tPosL i;
    for (i = L; i->sig!=LNULL; ++i);
    return i;
}

tPosL previous(tPosL p, tList L)
{
    if (p==L)
        return LNULL;
    else
    {
        tPosL i;
        for (i = L; i->sig!=p; ++i);

        return i;
    }
}

tPosL next(tPosL p,tList L)
{
    return p->sig;
}

void deleteAtPosition(tPosL p , tList *L)
{
    if (p==*L)
        *L = (*L)->sig;
    else if (p->sig==LNULL)
    {
        tPosL i;
        for (i = *L; i->sig!=p; i = i->sig);
        i->sig = LNULL;
    }
    else
    {
        tPosL q;

        q = p->sig; //q es la siguiente posición a p
        p->Item = q->Item; //los datos de p serán los de su siguiente posición
        p->sig = q->sig; //p apunta a dos posiciones adelante, ignorando q ya que el item es el mismo
        p = q; //p pasa a ser la siguiente posición ya que fue eliminado
    }

    free(p);
}

void deleteList(tList *L)
{
    tPosL p;

    while (!isEmptyList(*L))
    {
        p = *L; //se guarda el nodo en una variable
        *L = (*L)->sig; //el primer elemento pasa a ser el siguiente
        free(p); //se libera la memoria del nodo en desuso
    }
}

bool copyList(tList L, tList *M)

{
    createEmptyList(M);

    tPosL p, q; //p para L y q para M

    if (!isEmptyList(L))
    {
        tPosL i; //variable contador
        p = L; //p empieza siendo la primera posición de la lista a copiar
        while (p!=LNULL && createNode(&i)) //mientras no se acabe L y haya espacio
        {
            i->Item = p->Item; //el item de la variable auxiliar es el de la posición actual en L
            i->sig = LNULL;

            if (p == L) //si estamos en la primera posición de L
            {
                *M = i; //la primera posición tiene los atributos de la primera de L
            }
            else
            {
                q->sig = i; //el siguiente a la posición de M de la iteración anterior pasa a tener los atributos de esa posición de L
            }
            q = i; //?????
            p = p->sig; //se avanza una posición el el recorrido en L
        }

        if (p != LNULL) //no hay más espacio
        {
            deleteList(M);
            return false;
        }
    }
    return true;
}