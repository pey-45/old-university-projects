#include <stdlib.h>
#include <stdbool.h>

#define NULLBST NULL

typedef int tKey;

typedef struct tBSTNode *tBSTPos;

struct tBSTNode {
    tKey key;
    tBSTPos right;
    tBSTPos left;
};

typedef tBSTPos tBST;





bool createBSTNode(tBSTPos* p, tKey key)
{
    *p = (tBST)malloc(sizeof(struct tBSTNode));
    if (*p!=NULLBST)
    {
        (*p)->key = key;
        (*p)->left = NULLBST;
        (*p)->right = NULLBST;
    }

    return *p!=NULLBST;
}

bool isEmptyTree (tBST tree)
{
    return tree==NULLBST;
}

void createEmptyTree(tBST* tree)
{
    *tree = NULLBST;
}

tBST findKey(tBST tree, tKey key)
{
    if (isEmptyTree(tree))
        return NULLBST;
    else if (key==tree->key)
        return tree;
    else if (key<tree->key)
        return findKey(tree->left, key);
    else
        return findKey(tree->right, key);
}

bool insertKey(tBST* tree, tKey key)
{
   if (isEmptyTree(*tree))
       return createBSTNode(tree, key);
   else if (key == (*tree)->key)
       return true;
   else if (key < (*tree)->key)
       return insertKey(&(*tree)->left, key);
   else
       return insertKey(&(*tree)->right, key);
}

void replace (tBST* subTree,tBST* auxTree)
{
    if (!isEmptyTree((*subTree)->right))
    {
        replace(&(*subTree)->right,auxTree);
    }
    else
    {
        (*auxTree)->key = (*subTree)->key;
        *auxTree = *subTree;
        (*subTree) = (*subTree)->left;
    }
}