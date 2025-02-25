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