#include <stdlib.h>
#include <stdbool.h>

#define SNULL NULL

typedef int tItemS;
typedef struct tNodeS *tPosS;

struct tNodeS {
    tItemS Item;
    tPosS down;
};

typedef tPosS tStack;

/* Function prototypes */





void createEmptyStack(tStack *S)
{
    *S = SNULL;
}

bool createNode(tPosS *p)
{
    *p = (tPosS)malloc(sizeof(struct tNodeS));
    return *p!=SNULL;
}

bool push(tItemS item, tStack *S)
{
    tPosS p;
    if (!createNode(&p))
        return false;
    else
    {
        p->Item = item;
        p->down = *S;
        *S = p;

        return true;
    }
}

void pop(tStack *S)
{
    tPosS aux;

    aux = *S;
    *S = (*S)->down;
    free(aux);
}

tItemS peek(tStack S)
{
    return (*S).Item;
}

bool isEmptyStack(tStack S)
{
    return S==SNULL;
}

