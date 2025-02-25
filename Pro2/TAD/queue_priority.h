#include <stdlib.h>
#include <stdbool.h>

#define QNULL NULL

typedef int tItemQ;

typedef struct tNodeQ *tPosQ;

struct tNodeQ{
    tItemQ Item;
    tPosQ sig;
};

typedef struct Queue{
    tPosQ front;
    tPosQ rear;

} tQueue;





bool createNode(tPosQ *p)
{
    *p = (tPosQ)malloc(sizeof(struct tNodeQ));
    return *p != QNULL;
}

void createEmptyQueue(tQueue *Q)
{
    Q->front = QNULL;
    Q->rear = QNULL;
}

bool isEmptyQueue(tQueue Q)
{
    return Q.front==QNULL;
}

bool enqueue(tItemQ item, tQueue *Q)
{
    tPosQ p;
    if (!createNode(&p))
    {
        return false;
    }
    else
    {
        p->Item = item;
        p->sig = QNULL;

        if (isEmptyQueue(*Q))
            Q->front = p;
        else
            Q->rear->sig = p;
        Q->rear = p;

        return true;
    }
}

void dequeue(tQueue *Q)
{
    tPosQ p;

    p = Q->front;
    Q->front = p->sig;

    if (isEmptyQueue(*Q))
        Q->rear = QNULL;

    free(p);
}

tItemQ front(tQueue Q)
{
    return Q.front->Item;
}

//ORDERED LIST

#define LNULL NULL
#define MAXPRIO 5 //Max priority level

typedef int tPriority;

typedef struct tItemL {
    tPriority prio;
    tQueue queue;
} tItemL;

typedef struct tNodeL* tPosL;  //Ptr to tNode

struct tNodeL {
    tItemL data;
    tPosL next;
};

typedef tPosL tOrderedList;

/* Function prototypes */

bool isEmptyList(tOrderedList L)
{
    return (L == LNULL);
}

void createEmptyList(tOrderedList *L)
{
    *L = LNULL;
}

bool createNodeL(tPosL *p)
{
    *p = (tPosL)malloc(sizeof(struct tNodeL));
    return *p != LNULL;
}

tPosL findPosition(tOrderedList L, tItemL d)
{
    tPosL p,tmp;

    p = L; //se guarda la primera posición de la lista en p
    tmp = L; //lo mismo en tmp
    while ((p != LNULL) && (p->data.prio < d.prio)) //mientras que no se acabe la lista y  la prioridad de la posición actual sea menor al del item a añadir
    {
        tmp = p;
        p = p->next;
    }
    return tmp; //se devuelve el ultimo elemento con mayor prioridad al elemento buscado (???)
}

bool insertItem(tPriority prio, tOrderedList *L) {
    tPosL q, p;

    if (!createNodeL(&q) || prio > MAXPRIO)
    {
        return false;
    }
    else
    {
        q->data.prio = prio; //se guarda la prioridad en la cola del nodo q
        createEmptyQueue(&q->data.queue); //se inicializa la cola
        q->next = LNULL;

        if (isEmptyList(*L))
        {
            *L = q; //si la lista está vacía se coloca en la primera posición
        }
        else if (prio < (*L)->data.prio)
        {
            //si la prioridad es mayor (numero menor) a la del primer elemento se coloca al principio
            q->next = *L;
            *L = q;
        }
        else
        {
            p = findPosition(*L, q->data);
            q->next = p->next;
            p->next = q;
        }

        return true;
    }
}

void updateItem(tOrderedList *L, tPosL p, tQueue queue)
{
    p->data.queue = queue;
}

tPosL findItem(tPriority prio, tOrderedList L)
{
    tPosL p;

    for (p = L; (p != LNULL) && (p->data.prio < prio); p = p->next);

    return p;
}

void getItem(tPosL p, tOrderedList L, tPriority *prio, tQueue *queue)
{

    *prio = p->data.prio;
    *queue = p->data.queue;
}

tPosL first(tOrderedList L)
{

    return L; // L always points to te first element of the list
}

tPosL last(tOrderedList L)
{
    tPosL p;

    for (p = L; p->next != LNULL; p = p->next)
        ;
    return p;
}

tPosL previous(tPosL p, tOrderedList L)
{
    tPosL q;

    if (p == L) {
        return LNULL;
    } else {
        for (q = L; q->next != p; q = q->next)
            ;
        return q;
    }
}

tPosL next(tPosL p, tOrderedList L)
{
    return p->next;
}

void deleteAtPosition(tPosL p, tOrderedList *L)
{
    tPosL q;

    if (p == *L)
    { // Delete first element
        *L = (*L)->next;
    }
    else if (p->next == LNULL)
    { //Delete last element
        for (q = *L; q->next->next != p; q = q->next)
            ;
        q->next = LNULL;

    }
    else { //Middle deletion Overwrite p with q
        //p--> element we want to delete
        //q-->next element

        q = p->next;
        p->data = q->data;
        p->next = q->next;
        p = q;
    }

    free(p);
}

void deleteList(tOrderedList *L)
{
    tPosL p;

    while (!isEmptyList(*L))
    {
        p = *L;
        *L = (*L)->next;
        free(p);
    }
}

//PRIORITY QUEUE

#define LNULL NULL

typedef tOrderedList tQueueP;

bool isEmptyQueueP(tQueueP queueP)
{
    return (queueP==LNULL);
}

void createEmptyQueueP(tQueueP *queueP)
{
    *queueP=LNULL;
}

bool enqueueP(tItemQ item, tPriority prio, tQueueP *queueP)
{
    tPosL pos;
    tQueue Q;

    pos = findItem(prio, *queueP); //se busca la cola en esa prioridad

    if (pos == LNULL) { //si la posición es nula es que no existe, entonces se añade
        if (!insertItem(prio, queueP)) {//si no se puede insertar devuelve false
            return false;
        }
        pos = findItem(prio, *queueP); //???
    }

    //se guarda la cola en pos
    getItem(pos, *queueP, &prio, &Q);

    // Update the queue
    if (!enqueue(item, &Q)) {
        return false;
    }
    //Update the list
    updateItem(queueP, pos, Q);
    return true;
}

void dequeueP(tQueueP *queueP) {
    /* Precondition: priority queue is not empty */

    tPosL posL;
    tItemL itemL;
    tQueue queue;
    tPriority priority;

    //Extract the queue
    posL = first(*queueP);
    getItem(posL, *queueP, &priority, &queue);
    dequeue(&queue);
    //If the queue remains empty, delete the entire node.
    if (isEmptyQueue(queue)) {
        deleteAtPosition(posL, queueP);
    } else {
        updateItem(queueP, posL, queue);
    }
}

tItemQ frontP(tQueueP queueP)
{ //Precondition: QueueP not empty
    tPosL first_pos;
    tQueue queue;
    tPriority node_prio;


    first_pos=first(queueP);
    getItem(first_pos, queueP, &node_prio, &queue);
    return queue.front->Item;

}