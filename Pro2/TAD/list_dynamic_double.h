#include <stdlib.h>
#include <stdbool.h>

#define LNULL NULL

typedef int tItemL;

typedef struct tNode* tPosL;

struct tNode
{
    tItemL Item;
    tPosL sig;
    tPosL prev;
};

typedef tPosL tList;





bool createNode(tPosL* p)
{
    *p = (tPosL)malloc(sizeof(struct tNode));
    return *p != LNULL;
}

void createEmptyList(tList *L)
{
    *L = LNULL;
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
        q->prev = LNULL;

        if (isEmptyList(*L)) //se comprueba si la lista está vacía
        {
            *L = q;
        }
        else if (p==LNULL) //si la posición dada es nula
        {
            tPosL i;
            for (i = *L; i != LNULL; i = i->sig);
            i->sig = q;
            q->prev = i;
        }
        else if (p==*L)
        {
            q->sig = p;
            p->prev = q;
            q = *L;
        }
        else
        {
            q->Item = p->Item;
            p->Item = item;
            if (p->sig != LNULL) {
                p->sig->prev = q;
            }
            q->sig = p->sig;
            p->sig = q;
            q->prev = p;
        }
        return true;
    }
}
//CORRECTO MIRANDO (REPASAR)

void updateItem(tItemL item, tPosL p, tList* L)
{
    p->Item = item;
}
//CORRECTO

tPosL findItem(tItemL item,tList L)
{
    tPosL i;
    for (i = L; i!=LNULL && i->Item!=item; i = i->sig);

    return i;
}
//CORRECTO

bool isEmptyList(tList L)
{
    return L==LNULL;
}
//CORRECTO

tItemL getItem(tPosL p, tList L)
{
    return p->Item;
}
//CORRECTO

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
//ES I->SIG, NO I

tPosL previous(tPosL p, tList L)
{
    return p->prev;
}
//NO SE COMPRUEBA SI LA LISTA ESTÁ VACÍA, NO HACE FALTA

tPosL next(tPosL p,tList L)
{
    return p->sig;
}
//CORRECTO

void deleteAtPosition(tPosL p , tList *L)
{
    if (p==*L)
    {
        *L = (*L)->sig;

        if (!isEmptyList(*L)) //OJO A ESTO
            (*L)->prev = LNULL;
    }
    else
    {
        tPosL q;

        q = p->prev;
        p->Item = q->Item;
        q->sig = p->sig;
        p->sig->prev = q;
        p = q;
    }

    free(p);
}
//REPASAR

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
//REPASAR

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
            i->prev = LNULL;

            if (p == L) //si estamos en la primera posición de L
            {
                *M = i; //la primera posición tiene los atributos de la primera de L
            }
            else
            {
                q->sig = i; //el siguiente a la posición de M de la iteración anterior pasa a tener los atributos de esa posición de L
                i->prev = q;
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