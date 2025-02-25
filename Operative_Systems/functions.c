#include "functions.h"

#define PERMISSIONS 0644 //para memoria compartida

extern char **environ;

int globalvar_noinit1;
int globalvar_noinit2;
int globalvar_noinit3;
int globalvar_init1 = 0;
int globalvar_init2 = 0;
int globalvar_init3 = 0;

static int staticvar_noinit1;
static int staticvar_noinit2;
static int staticvar_noinit3;
static int staticvar_init1 = 0;
static int staticvar_init2 = 0;
static int staticvar_init3 = 0;

void f_authors(char ** command) {
    if (command[1] && !strcmp(command[1], "-l")) {
        printf("pablo.manzanares.lopez@udc.es\nalejandro.rodriguezf@udc.es\n");
    } else if (command[1] && !strcmp(command[1], "-n")) {
        printf("Pablo Manzanares Lopez\nAlejandro Rodriguez Franco\n");
    } else { 
        printf("Pablo Manzanares Lopez: pablo.manzanares.lopez@udc.es\nAlejandro Rodriguez Franco: alejandro.rodriguezf@udc.es\n");
    }
}

void f_pid(char ** command)
{
    bool p = command[1] && !strcmp(command[1], "-p");
    printf("Pid del %sshell: %ld\n", p? "padre del ":"", p? (long)getppid():(long)getpid());

    if (command[1]) {}
}

void f_chdir(char ** command)
{
    if (!command[1]) //si no hay argumentos imprime el directorio actual
        printCurrentDir();
    else if (chdir(command[1])) //si el argumento no es un directorio valido salta error, en cualquier otro caso cambia al directorio dado
        perror("Imposible cambiar directorio");
}

void f_time(char ** command)
{
    //se guarda en el struct local los datos referentes al horario actual
    time_t t; 
    time(&t); 
    struct tm *local = localtime(&t);

    //se imprime la hora o la fecha dependiendo del comando
    if (strcmp(command[0], "time")) 
        printf("%02d:%02d:%02d\n", local->tm_hour, local->tm_min, local->tm_sec);
    else 
        printf("%02d/%02d/%d\n", local->tm_mday, local->tm_mon+1, local->tm_year+1900);
}

void f_hist(char ** command, tList * command_history)
{
    if (command[1] && !strcmp(command[1], "-c")) //si hay argumento y es -c se borra la lista 
        deleteList(command_history);
    else if (command[1] && command[1][0]=='-' && isDigitString(command[1]+1 /*sin el guion*/)) //si hay argumento y no es -c hay que comprobar si lo que hay despues es un numero
        printHistUntil(atoi(command[1]+1), *command_history);
    else //en cualquier otro caso se imprime el historial
        printHistUntil(listLength(*command_history), *command_history);
}

void f_command(char ** command, int (*main)(int, char**, char**), char ** envp, tList * command_history, tList * open_files, tList * memory, tList * shared_memory, tList * mmap_memory, tList * bg_proc) 
{
    char *string = MALLOC, **strings = MALLOC_PTR;
    int current_index = 1;
    tPosL i;

    if (!command[1] || !isDigitString(command[1]) || !string || !strings) 
    {
        if (command[1] && isDigitString(command[1])) //si el error fue ocasionado por un error de memoria no se imprime nada
            perror("Error al asignar memoria");
        else //si no hay argumentos o el argumento no es un número se imprime el historial
            printHistUntil(listLength(*command_history), *command_history);

        freeAll(2, string, strings); 
        return;
    }

    //se recorre la lista de historial hasta el numero dado o nulo si no se encuentra
    for (i = first(*command_history); i && current_index++ < atoi(command[1]); i = next(i));

    if (i) //si se encuentra se ejecuta ese comando
    {
        strcpy(string, getItem(i));
        printf("Ejecutando hist (%s): %s\n", command[1], string);
        trimString(string, strings);
        processCommand(strings, main, envp, command_history, open_files, memory, shared_memory, mmap_memory, bg_proc);
    } 
    else printf("No hay elemento %s en el histórico\n", command[1]);

    freeAll(2, string, strings);
}

void f_open(char ** command, tList * open_files, bool show_message)
{
    int i, df, mode = 0;
    char *mode_c = MALLOC, *output = MALLOC;

    if (!command[1] || !mode_c || !output)
    {
        if (!command[1]) //si no hay argumentos se imprime la lista de ficheros abiertos
            printOpenListByDF(*open_files);
        else //fallo en el malloc de mode_c o output
            perror("Error al asignar memoria");

        freeAll(2, mode_c, output); 
        return;
    }

    //para evitar saltos condicionales dependientes de variables sin inicializar
    initializeString(mode_c);

    for (i=2; command[i]; ++i) //se itera desde el segundo argumento asignando los valores correspondientes
    {
        if      (!strcmp(command[i],"cr")) { mode|=O_CREAT;  strcpy(mode_c, "O_CREAT"); }
        else if (!strcmp(command[i],"ex")) { mode|=O_CREAT;  strcpy(mode_c, "O_EXCL"); }
        else if (!strcmp(command[i],"ro")) { mode|=O_RDONLY; strcpy(mode_c, "O_RDONLY"); }
        else if (!strcmp(command[i],"wo")) { mode|=O_WRONLY; strcpy(mode_c, "O_WRONLY"); }
        else if (!strcmp(command[i],"rw")) { mode|=O_RDWR;   strcpy(mode_c, "O_RDWR"); }
        else if (!strcmp(command[i],"ap")) { mode|=O_APPEND; strcpy(mode_c, "O_APPEND"); }
        else if (!strcmp(command[i],"tr")) { mode|=O_TRUNC;  strcpy(mode_c, "O_TRUNC"); }
        else break;
    }

    if ((df=open(command[1], mode, 0777))==-1) //se intenta abrir y no se puede da error 
        fprintf(stderr, "Imposible abrir %s: %s\n", command[1], strerror(errno));
    else //si no simplemente se abrio y se añade a la lista de ficheros abiertos con su descriptor y modo
    {
        sprintf(output, "Descriptor: %d -> %s %s", df, command[1], mode_c);
        insertItem(output, NULL, open_files);
        if (show_message) 
            printf("Anadida entrada %d a la tabla ficheros abiertos\n", df);
    }

    freeAll(2, mode_c, output);
}

void f_close (char ** command, tList * open_files)
{
    int df;

    //si no hay argumentos o este no es un numero se imprime la lista de archivos abiertos
    if (!command[1] || !isDigitString(command[1])) 
        { printOpenListByDF(*open_files); return; }

    if (close(df = atoi(command[1]))==-1) //se intenta cerrar y no se puede da error
        perror("Imposible cerrar descriptor");
    else //si no simplemente se cerró y se elimina de la lista de ficheros abiertos
        deleteAtPosition(getPosByDF(df, *open_files), open_files);
}

void f_dup(char ** command, tList * open_files)
{
    int df;
    tPosL pos;
    char *output = MALLOC, *p = MALLOC, *mode = MALLOC, *aux = MALLOC, **aux_strings = MALLOC_PTR;
    initializeString(p);

    if (!output || !p || !mode || !aux || !aux_strings || !command[1] || !isDigitString(command[1]))
    {
        if (!command[1] || !isDigitString(command[1])) //si no hay argumentos o no es un numero se imprime la lista de archivos abiertos
            printOpenListByDF(*open_files);
        else //si no es un error de memoria
            perror("Error al asignar memoria");

        freeAll(5, output, p, mode, aux, aux_strings); 
        return;
    }

    //el numero se guarda en df
    df = atoi(command[1]);

    /*guardamos la posicion y si existe almacenamos el string en una variable auxiliar 
    para separarla en un array de cadenas y manipularlas mejor*/
    pos = getPosByDF(df, *open_files);
    if (pos) 
        strcpy(aux, getItem(pos));
    else 
        { perror("Imposible duplicar descriptor"); freeAll(5, output, p, mode, aux, aux_strings); return; }

    trimString(aux, aux_strings);

    for (int i = 3; aux_strings[i]; ++i) //se itera desde el nombre del archivo
    {
        /*la tercera posicion se tiene que leer como archivo, aunque el string sea uno de los modos,
          si se lee un modo se copia y se deja de buscar*/
        if (i != 3 &&
           (!strcmp(aux_strings[i], "O_CREAT") ||
            !strcmp(aux_strings[i], "O_EXCL") ||
            !strcmp(aux_strings[i], "O_RDONLY") ||
            !strcmp(aux_strings[i], "O_WRONLY") ||
            !strcmp(aux_strings[i], "O_RDWR") ||
            !strcmp(aux_strings[i], "O_APPEND") ||
            !strcmp(aux_strings[i], "O_TRUNC")))   
            { strcpy(mode, aux_strings[i]); break; }

        strcat(p, aux_strings[i]);
        strcat(p, " ");
    }

    //se quita el espacio sobrante
    p[strlen(p)-1] = '\0';

    //se coloca todo en su lugar y se inserta en la lista de archivos abiertos
    sprintf(output, "Descriptor: %d -> dup %d (%s) %s", dup(df), df, p, mode);
    insertItem(output, NULL, open_files);

    freeAll(5, output, p, mode, aux, aux_strings);
}

void f_listopen(char ** command, tList open_files)
{
    if (command[1] && isDigitString(command[1])) //si tiene argumento y es un numero se imprime hasta ahi
        printOpenListByDFUntil(atoi(command[1]), open_files);
    else //en cualquier otro caso se imprime todo
        printOpenListByDF(open_files);
}

void f_infosys()
{
    //se guardan los datos del dispositivo en un struct y se imprimen
    struct utsname unameData; 
    uname(&unameData);
    printf("%s (%s), OS: %s-%s-%s\n", unameData.nodename, unameData.machine, unameData.sysname, unameData.release, unameData.version);
}

void f_help(char ** command)
{
    char string[MAX_PROMPT];
    //se muestra la ayuda correspondiente a cada comando
    if (!command[1]) 
    {
        printf("help [cmd|-lt|-T topic]: ayuda sobre comandos\n\tComandos disponibles:\nauthors pid chdir " 
               "date time hist command open close dup listopen infosys help quit exit bye create stat list delete deltree\n");
        return;
    }
    
    strcpy(string, command[1]); 

    if (!strcmp(string, "authors")) 
        printf("authors [-n|-l]: Muestra los nombres y/o logins de los autores\n");
    else if (!strcmp(string, "pid")) 
        printf("pid [-p]: Muestra el pid del shell o de su proceso padre\n");
    else if (!strcmp(string, "chdir")) 
        printf("chdir [dir]: Cambia (o muestra) el directorio actual del shell\n");
    else if (!strcmp(string, "date")) 
        printf("date: Muestra la fecha actual\n");
    else if (!strcmp(string, "time")) 
        printf("time: Muestra la hora actual\n");
    else if (!strcmp(string, "hist")) 
        printf("hist [-c|-N]: Muestra (o borra) el historico de comandos\n\t-N: muestra los "
               "N primeros\n\t-c: borra el historico\n");
    else if (!strcmp(string, "command")) 
        printf("command [-N]: Repite el comando N (del historico)\n");
    else if (!strcmp(string, "open")) 
        printf("open fich m1 m2...:	Abre el fichero fich. y lo anade a la lista de ficheros "
               "abiertos del shell\nm1, m2..es el modo de apertura (or bit a bit de los siguientes)\n\tcr: O_CREAT\tap: O_APPEND\n\tex: O_EXCL"
               "\tro: O_RDONLY\n\trw: O_RDWR\two: O_WRONLY\n\ttr: O_TRUNC\n");
    else if (!strcmp(string, "close")) 
        printf("close df: Cierra el descriptor df y elimina el correspondiente fichero de la "
               "lista de ficheros abiertos\n");
    else if (!strcmp(string, "dup")) 
        printf("dup df: Duplica el descriptor de fichero df y anade una nueva entrada a la lista ficheros abiertos\n");
    else if (!strcmp(string, "listopen")) 
        printf("listopen [n]: Lista los ficheros abiertos (al menos n) del shell\n");
    else if (!strcmp(string, "infosys")) 
        printf("infosys: Muestra informacion de la maquina donde corre el shell\n");
    else if (!strcmp(string, "help")) 
        printf("help [cmd|-lt|-T]: Muestra ayuda sobre los comandos\n\t-lt: lista topics de ayuda"
    "\n\t-T topic: lista comandos sobre ese topic\n\tcmd: info sobre el comando cmd\n");
    else if (!strcmp(string, "quit") || !strcmp(command[1], "exit") || !strcmp(command[1], "bye")) 
        printf("%s: Termina la ejecucion del shell\n", command[1]);
    else if (!strcmp(string, "create")) 
        printf("create [-f] [name]: Crea un directorio o un fichero (-f)\n");
    else if (!strcmp(string, "stat")) 
        printf("stat [-long][-link][-acc] [name1] [name2] [...]:	lista ficheros\n\t-long: listado largo"
	           "\n\t-acc: acesstime\n\t-link: si es enlace simbolico, el path contenido\n");
    else if (!strcmp(string, "list")) 
        printf("list [-reca] [-recb] [-hid][-long][-link][-acc] [n1] [n2] [...]: lista contenidos de directorios"
	           "\n\t-hid: incluye los ficheros ocultos\n\t-recb: recursivo (antes)\n\t-reca: recursivo (despues)\n\tresto parametros como stat\n");
    else if (!strcmp(string, "delete")) 
        printf("delete [name1] [name2] [...]: Borra ficheros o directorios vacios\n");
    else if (!strcmp(string, "deltree")) 
        printf("deltree [name1] [name2] [...]: Borra ficheros o directorios no vacios recursivamente\n");
    else if (!strcmp(string, "malloc")) 
        printf("malloc [-free] [tam]: asigna un bloque memoria de tamano tam con malloc \n\t-free:"
               "desasigna un bloque de memoria de tamano tam asignado con malloc\n");
    else if (!strcmp(string, "shared")) 
        printf("shared [-free|-create|-delkey] cl [tam]: asigna memoria compartida con clave cl en el programa"
	           "\n\t-create cl tam: asigna (creando) el bloque de memoria compartida de clave cl y tamano tam"
	           "\n\t-free cl: desmapea el bloque de memoria compartida de clave cl"
	           "\n\t-delkey clelimina del sistema (sin desmapear) la clave de memoria cl\n");
    else if (!strcmp(string, "mmap")) 
        printf("mmap [-free] fich prm: mapea el fichero fich con permisos prm \n\t-free fich: desmapea el ficherofich\n");
    else if (!strcmp(string, "read")) 
        printf("read fiche addr cont: lee cont bytes desde fich a la direccion addr\n");
    else if (!strcmp(string, "write")) 
        printf("write [-o] fiche addr cont: escribe cont bytes desde la direccion addr a fich (-o sobreescribe)\n");
    else if (!strcmp(string, "memdump")) 
        printf ("memdump addr cont: vuelca en pantallas los contenidos (cont bytes) de la posicion de memoria addr\n");
    else if (!strcmp(string, "memfill")) 
        printf("memfill addr cont byte: llena la memoria a partir de addr con byte\n");
    else if (!strcmp(string, "mem")) 
        printf("mem [-blocks|-funcs|-vars|-all|-pmap] ..: muestra muestra detalles de la memoria del proceso"
	           "\n\t-blocks: los bloques de memoria asignados"
	           "\n\t-funcs: las direcciones de las funciones"
	           "\n\t-vars: las direcciones de las variables"
	           "\n\t:-all: todo"
	           "\n\t-pmap: muestra la salida del comando pmap(o similar)\n");
    else if (!strcmp(string, "uid")) 
        printf("uid [-get|-set] [-l] [id]: ccede a las credenciales del proceso que ejecuta el shell"
	           "\n\t-get: muestra las credenciales"
	           "\n\t-set id: establece la credencial al valor numerico id"
	           "\n\t-set -l id: establece la credencial a login id\n");
    else if (!strcmp(string, "showvar")) 
        printf("showvar var: muestra el valor y las direcciones de la variable de entorno var\n");
    else if (!strcmp(string, "changevar")) 
        printf("changevar [-a|-e|-p] var valor: cambia el valor de una variable de entorno"
	           "\n\t-a: accede por el tercer arg de main"
	           "\n\t-e: accede mediante environ"
	           "\n\t-p: accede mediante putenv\n");
    else if (!strcmp(string, "subsvar")) 
        printf("subsvar [-a|-e] var1 var2 valor: sustituye la variable de entorno var1 con var2=valor"
	           "\n\t-a: accede por el tercer arg de main"
	           "\n\t-e: accede mediante environ\n");
    else if (!strcmp(string, "showenv")) 
        printf("showenv [-environ|-addr]: muestra el entorno del proceso"
	           "\n\t-environ: accede usando environ (en lugar del tercer arg de main)"
	           "\n\t-addr: muestra el valor y donde se almacenan environ y el 3er arg main\n");
    else if (!strcmp(string, "fork")) 
        printf("fork: el shell hace fork y queda en espera a que su hijo termine\n");
    else if (!strcmp(string, "exec")) 
        printf("exec VAR1 VAR2 ..prog args....[@pri]: ejecuta, sin crear proceso,prog con argumentos"
	           "en un entorno que contiene solo las variables VAR1, VAR2...\n");
    else if (!strcmp(string, "jobs")) 
        printf("jobs: lista los procesos en segundo plano\n");
    else if (!strcmp(string, "deljobs")) 
        printf("deljobs [-term][-sig]: elimina los procesos de la lista procesos en sp"
	           "\n\t-term: los terminados"
	           "\n\t-sig: los terminados por senal\n");
    else if (!strcmp(string, "job")) 
        printf("job [-fg] pid: muestra informacion del proceso pid."
		       "\n\t-fg: lo pasa a primer plano\n");
    else 
        printf("%s no encontrado\n", command[1]);
}

void f_quit(tList * command_history, tList * open_files, tList * memory, tList * shared_memory, tList * mmap_memory, tList * bg_proc) 
{ 
    deleteList(command_history); 
    deleteList(open_files); 
    deleteList(memory); 
    deleteList(shared_memory); 
    deleteList(mmap_memory); 
    deleteList(bg_proc);
    exit(0); 
}

void f_create(char ** command, tList * open_files)
{
    char *string = MALLOC, **strings = MALLOC_PTR;
    if (!command[1] || (!strcmp(command[1], "-f") && !command[2]) || !string || !strings)
    {
        if (!command[1] || (!strcmp(command[1], "-f") && !command[2])) 
            printCurrentDir();
        else 
            perror("Error al asignar memoria");

        freeAll(2, string, strings); 
        return;
    }

    if (!strcmp(command[1], "-f"))
    {
        sprintf(string, "open %s cr", command[2]);
        trimString(string, strings);
        f_open(strings, open_files, false);
    }
    else if (mkdir(command[1], 0777)) 
        perror("Imposible crear directorio");
    
    freeAll(2, string, strings);
}

void f_stat(char ** command)
{
    struct stat attr;
    //reservo memoria para las listas de archivos y argumentos
    char **files = MALLOC_PTR, **args = MALLOC_PTR;
    bool in_files = 0;
    int i, args_pos = 0, files_pos = 0;

    if (!files || !args) 
        { perror("Error al asignar memoria"); freeAll(2, files, args); return; }

    //inicializo sus elementos como nulos
    for (i = 0; i < MAX_PROMPT; i++) 
    { 
        files[i] = NULL; 
        args[i] = NULL; 
    }

    for (i = 1; command[i]; i++)
    {
        //si el comando no es ningun argumento válido tod0 lo que haya delante sera considerado un archivo
        if (strcmp(command[i], "-long") && strcmp(command[i], "-link") && strcmp(command[i], "-acc")) 
            in_files = 1;

        if (!in_files)
        {
            //se inicializa la posicion en la que se guardará el argumento
            if (!(args[args_pos] = MALLOC)) 
                { perror("Error al asignar memoria"); freeAllRec(2, files, args); return; }

            initializeString(args[args_pos]); //para evitar fallos con el strcmp

            //se guarda cuales de los parametros se han pasado
            if (!strcmp(command[i], "-long") && !includesString("long", args)) 
                strcpy(args[args_pos], "long");
            else if (!strcmp(command[i], "-link") && !includesString("link", args)) 
                strcpy(args[args_pos], "link");
            else if (!strcmp(command[i], "-acc") && !includesString("acc", args)) 
                strcpy(args[args_pos], "acc");
            else //si se repite un parámetro se libera la posicion ya que no se añadirá nada, tampoco incrementa args_pos
                freeAll(1, args[args_pos--]);
            
            args_pos++;
        }       
        else
        {
            if (!(files[files_pos] = MALLOC)) 
                { perror("Error al asignar memoria"); freeAllRec(2, files, args); return; }

            strcpy(files[files_pos++], command[i]);
        }
    }

    if (!files[0]) //implicito !command[1]
        { printCurrentDir(); freeAllRec(2, files, args); return; }

    for (i = 0; i < files_pos; i++)
    {
        if (!stat(files[i], &attr))
        {
            if (includesString("long", args)) 
                printStat(files[i], attr, "long", includesString("link", args), false);
            else if (includesString("acc", args)) 
                printStat(files[i], attr, "acc", includesString("link", args), false);
            else if (includesString("link", args)) 
                printStat(files[i], attr, "link", includesString("link", args), false);
            else 
                printStat(files[i], attr, "few", includesString("link", args), false);
        }
        else fprintf(stderr, "Error al acceder a %s: %s\n", files[i], strerror(errno));
    }

    freeAllRec(2, files, args);
}

void f_list(char ** command)
{
    if (!command[1]) 
        { printCurrentDir(); return; } 

    int i, args_pos = 0, dirs_pos = 0;
    char **dirs = MALLOC_PTR, **args = MALLOC_PTR, recAorB = '\0';
    bool hid = false, in_dirs = false;

    if (!dirs || !args) 
        { perror("Error al asignar memoria"); freeAll(2, dirs, args); return; }

    //inicializo sus elementos como nulos
    for (i = 0; i < MAX_PROMPT; i++) 
    { 
        dirs[i] = NULL; 
        args[i] = NULL; 
    }

    for (i = 1; command[i]; i++)
    {
        //si el comando no es ningun argumento válido tod0 lo que haya delante sera considerado un archivo
        if (strcmp(command[i], "-long") && strcmp(command[i], "-link") && strcmp(command[i], "-acc") && strcmp(command[i], "-reca") 
            && strcmp(command[i], "-recb") && strcmp(command[i], "-hid")) 
            in_dirs = 1;

        if (!in_dirs)
        {
            //se inicializa la posicion en la que se guardará el argumento
            if (!(args[args_pos] = MALLOC))
                { perror("Error al asignar memoria"); freeAllRec(2, dirs, args); return; }
            
            initializeString(args[args_pos]);

            //se guarda cuales de los parametros se han pasado
            if (!strcmp(command[i], "-long") && !includesString("long", args)) 
                strcpy(args[args_pos], "long");
            else if (!strcmp(command[i], "-link") && !includesString("link", args)) 
                strcpy(args[args_pos], "link");
            else if (!strcmp(command[i], "-acc") && !includesString("acc", args)) 
                strcpy(args[args_pos], "acc");
            else if (!strcmp(command[i], "-reca")) 
                recAorB = 'A';
            else if (!strcmp(command[i], "-recb")) 
                recAorB = 'B';
            else if (!strcmp(command[i], "-hid")) 
                hid = true;
            else //si se repite un parámetro entre long, acc y link se libera la posicion ya que no se añadirá nada, tampoco incrementa args_pos
                freeAll(1, args[args_pos--]);

            args_pos++;
        }       
        else
        {
            if (!(dirs[dirs_pos] = MALLOC)) 
                { perror("Error al asignar memoria"); freeAllRec(2, dirs, args); return; }

            strcpy(dirs[dirs_pos++], command[i]);
        }
    }

    if (!dirs[0]) 
        { printCurrentDir(); freeAllRec(2, dirs, args); return; }

    for (i = 0; i < dirs_pos; i++) 
        listDirElements(dirs[i], args, recAorB, hid, false);
    
    freeAllRec(2, dirs, args);
}

void f_delete(char ** command)
{
    struct stat attr;
    char sure, aux;
    int i;

    if (!command[1]) 
        { printCurrentDir(); return; }

    if (!strcmp(command[0], "deltree"))
    {
        printf("Esta es una acción destructiva, deseas continuar [y/n]? ");
        scanf("%c", &sure); scanf("%c", &aux); //aux para evitar desbordamiento de buffer
        if (sure!='y' && sure !='Y') return;
    }
    
    for (i = 1; command[i]; i++)
    {
        if (lstat(command[i], &attr)) 
            fprintf(stderr, "Imposible borrar %s: %s\n", command[i], strerror(errno));
        else if (S_ISDIR(attr.st_mode)) 
        {
            if (!strcmp(command[0], "deltree")) 
                listDirElements(command[i], NULL, 'B', false, true);
            if (rmdir(command[i])) 
                fprintf(stderr, "Imposible borrar %s: %s\n", command[i], strerror(errno));
        }
        else if (remove(command[i])) 
            fprintf(stderr, "Imposible borrar %s: %s\n", command[i], strerror(errno));
    }
}

void f_malloc(char ** command, tList * memory)
{
    void *ptr;
    char *aux = MALLOC, *item = MALLOC, **full_item = MALLOC_PTR, *ptr_string = MALLOC;
    tPosL pos;
    time_t t; 
    time(&t); 
    struct tm *local = localtime(&t);
    unsigned long long memdir;

    if (!command[1] || 
        (!isDigitString(command[1]) && strcmp(command[1], "-free")) || 
        (!strcmp(command[1], "-free") && (!command[2] || !isDigitString(command[2])))) 
        { 
            printf("******Lista de bloques asignados malloc para el proceso %ld\n", (long)getpid()); 
            printList(memory); 
            freeAll(4, aux, item, full_item, ptr_string);
            return;
        }

    if (strcmp(command[1], "-free")) 
    {
        ptr = malloc(atoi(command[1]));
        sprintf(ptr_string, "%p", (void *)ptr);
        strftime(aux, MAX_PROMPT, "%b %d %H:%M", local);
        snprintf(item, MAX_PROMPT, "%20s%17s%14s%7s", ptr_string, command[1], aux, "malloc");
        insertItem(item, NULL, memory);
        printf("Asignados %s bytes en %s\n", command[1], ptr_string);
    }
    else
    {
        for (pos = first(*memory); pos != NULL; pos = next(pos))
        {
            strcpy(aux, getItem(pos));
            trimString(aux, full_item);
            if (!strcmp(full_item[1], command[2]))
            {
                memdir = strtoull(full_item[0], NULL, 16);
                ptr = (void*)memdir;
                if (ptr) free(ptr);
                deleteAtPosition(pos, memory);
                freeAll(4, aux, item, full_item, ptr_string);
                return;
            }
        }
    }
    
    freeAll(4, aux, item, full_item, ptr_string);
}

void f_shared(char ** command, tList * shared_memory) //NO FUNCIONA
{
    int i, size, ptr_free;
    char *item = MALLOC, *aux = MALLOC, *ptr_string = MALLOC, **full_item = MALLOC_PTR;
    key_t key;
    void *ptr;
    time_t t; 
    time(&t); 
    struct tm *local = localtime(&t);
    tPosL pos;


    if (!command[2]) 
    { 
        printf("******Lista de bloques asignados shared para el proceso %ld\n", (long)getpid());
        printList(shared_memory); 
        freeAll(4, item, aux, ptr_string, full_item);
        return;
    }
    for (i = 1; command[i]!=NULL; i++)
    {
        if (!strcmp("-create", command[i]) || !strcmp("-free", command[i]) || !strcmp("-delkey", command[i])) 
            continue;
        else if ((!strcmp("-create", command[1]) && command[i] && command[i+1] && isDigitString(command[i]) && isDigitString(command[i+1])) ||
                 ((!strcmp("-free", command[1]) || !strcmp("-delkey", command[1])) && command[i] && isDigitString(command[i])))
        {
            key = atoi(command[i]);
            if (!strcmp("-create", command[1])) 
                size = atoi(command[i+1]);

            break;
        }
        else 
        { 
            printf("******Lista de bloques asignados shared para el proceso %ld\n", (long)getpid());
            printList(shared_memory); 
            freeAll(4, item, aux, ptr_string, full_item);
            return; 
        }
    }

    if (!strcmp(command[1], "-create")) 
    {
        key = (key_t)strtoul(command[2],NULL,10);
        
        if (!(size = (size_t)strtoul(command[3], NULL, 10))) 
        { 
            printf ("No se asignan bloques de 0 bytes\n"); 
            freeAll(4, item, aux, ptr_string, full_item);
            return; 
        }
        if ((ptr=getShm(key, size)))
        {		    
            sprintf(ptr_string, "%p", (void *)ptr);
            printf ("Asignados %lu bytes en %p\n", (unsigned long) size, ptr);  
            strftime(aux, MAX_PROMPT, "%b %d %H:%M", local);
            snprintf(item, MAX_PROMPT, "%20s%17s%14s%13s%d%c", ptr_string, command[3], aux, "shared (key ", key, ')');
            insertItem(item, NULL, shared_memory);
        }
        else printf ("Imposible asignar memoria compartida clave %lu: %s\n", (unsigned long)key, strerror(errno));
    }
    else if (!strcmp(command[1], "-free")) 
    {
        for (pos = first(*shared_memory); pos != NULL; pos = next(pos))
        {
            strcpy(aux, getItem(pos));
            trimString(aux, full_item);
            full_item[7][strlen(full_item[7])-1] = '\0';

            if (!strcmp(full_item[7], command[2]))
            {
                if ((ptr_free=shmget(key, (size_t)atoi(full_item[1]), PERMISSIONS)) && shmctl(ptr_free, IPC_RMID, NULL) == -1)
                { 
                    perror("Error al liberar la memoria compartida"); 
                    freeAll(4, item, aux, ptr_string, full_item);
                    return; 
                }
            }
            else continue;

            printf("Memoria compartida liberada correctamente.\n");
            deleteAtPosition(pos, shared_memory);
            freeAll(4, item, aux, ptr_string, full_item);
            return;
        }
    }
    else if (!strcmp(command[1], "-delkey")) 
    {
        if ((key= (key_t)strtoul(command[2],NULL,10)) == IPC_PRIVATE)
        { 
            printf ("delkey necesita clave_valida\n"); 
            freeAll(4, item, aux, ptr_string, full_item);
            return; 
        }
        if ((ptr_free=shmget(key,0,0666))==-1)
        { 
            perror ("shmget: imposible obtener memoria compartida"); 
            freeAll(4, item, aux, ptr_string, full_item);
            return; 
        }
        if (shmctl(ptr_free,IPC_RMID,NULL)==-1)
            perror ("shmctl: imposible eliminar id de memoria compartida\n");
    }
    else fprintf(stderr, "Imposible asignar memoria compartida clave %s: %s\n", command[1], strerror(errno));

    freeAll(4, item, aux, ptr_string, full_item);
}

void f_mmap(char ** command, tList * mmap_memory)
{
    int i, protection = 0, file_size;
    void *ptr;
    tPosL pos;
    char *string = MALLOC, **strings = MALLOC_PTR;

    if (!command[1] || (!strcmp(command[1], "-free") && !command[2])) 
    { 
        printf("******Lista de bloques asignados mmap para el proceso %ld\n", (long)getpid());
        printList(mmap_memory); 
        freeAll(2, string, strings);
        return;
    }

    if (strcmp(command[1], "-free"))
    {
        for (i = 0; i < 3 && command[2] && command[2][i]; i++)
        {
            if (command[2][i]=='r') protection|=PROT_READ;
            if (command[2][i]=='w') protection|=PROT_WRITE;
            if (command[2][i]=='x') protection|=PROT_EXEC;
        }

        if ((ptr = mmap_file(command[1], protection, mmap_memory)))
            printf("Fichero %s mapeado en %p\n", command[1], ptr);
        else
            perror("Imposible mapear fichero");
    }
    else
    {
        for (pos = first(*mmap_memory); pos != NULL; pos = next(pos))
        {
            strcpy(string, getItem(pos));
            trimString(string, strings);
            if (!strcmp(strings[5], command[2]))
            {
                file_size = atoi(strings[1]);
                break;
            }
        }

        if (!pos) 
        {
            printf("Fichero %s no mapeado\n", command[2]);
            freeAll(2, string, strings);
            return;
        }

        unsigned long long int memdir = strtoull(strings[0], NULL, 16);
        ptr = (void*)memdir;
        munmap(ptr, file_size);
        
        deleteAtPosition(pos, mmap_memory);
    }

    freeAll(2, string, strings);
}

void f_read(char ** command)
{
    int cnt = 0;
    void *ptr;
    struct stat attr;
    unsigned long long memdir;

    if (!command[2]) 
        { printf("Faltan parámetros\n"); return; }

    if (stat(command[1], &attr)) 
        { printf("No se ha podido hacer stat\n"); return; }            

    if (!command[3]) 
        cnt = attr.st_size;
    else if (isDigitString(command[3])) 
        cnt = atoi(command[3]);

    memdir = strtoull(command[2], NULL, 16);
    ptr = (void*)memdir;

    if (readFile(command[1], ptr, cnt) == -1)
        perror("Imposible leer fichero");
    else
        printf("Leídos %d bytes de %s en %s\n", cnt, command[1], command[2]);
}

void f_write(char ** command)
{    
    int cnt = 0;
    void *ptr;
    bool o;
    unsigned long long memdir;

    if ((!command[1] ||
         (command[1] && !strcmp(command[1], "-o") && !command[4])) ||
         (command[1] && strcmp(command[1], "-o") && !command[3])) 
        { printf("Faltan parámtros\n"); return; }

    o = !strcmp(command[1], "-o");

    cnt = atoi(command[o? 4:3]);

    memdir = strtoull(command[o? 3:2], NULL, 16);
    ptr = (void*)memdir;

    if (writeFile(command[o? 2:1], ptr, cnt, o)==-1)
        perror("Imposible escribir fichero");
    else 
        printf("Escritos %s bytes en %s desde %s\n", command[o? 4:3], command[o? 2:1], command[o? 3:2]);
}

void f_memdump(char ** command)
{
    int cnt = 25, i, j;
    char *buffer;
    unsigned long long memdir;

    if (!command[1]) return;

    if (command[2] && isDigitString(command[2])) 
        cnt = atoi(command[2]);
    
    memdir = strtoull(command[1], NULL, 16);

    if (!(buffer = (char*)malloc(cnt))) 
        { perror("Error al asignar memoria"); free(buffer); return; }

    for (i = 0; i < cnt; i++) 
    {
        if ((memdir + i) < memdir) 
        {
            fprintf(stderr, "Dirección de memoria fuera de límites.\n");
            free(buffer);
            return;
        }
        buffer[i] = *((char*)(memdir + i));
    }

    for (i = 0; i*25 < cnt; i++) 
    {
        for (j = 0; j < 25 && i*25+j < cnt; j++) 
        {
            if (!buffer || buffer[i*25+j]<0) 
                printf("   ");
            else 
                printf("%3c", buffer[i*25+j]);
        }
        printf("\n");  

        for (j = 0; j < 25 && i*25+j < cnt; j++) 
        {
            if (!buffer || buffer[i*25+j]<0) 
                printf(" 00");
            else 
                printf(" %02X", buffer[i*25+j]);
        }
        printf("\n");
    }
    printf("\n");

    free(buffer);
}

void f_memfill(char ** command)
{
    int cnt = 128, i, byte = 65;
    char *buffer;
    unsigned long long memdir;

    if (!command[1]) return;

    if (command[2] && isDigitString(command[2])) 
        cnt = atoi(command[2]);

    if (command[3] && isDigitString(command[3]))
        byte = atoi(command[3]);

    memdir = strtoull(command[1], NULL, 16);
    buffer = (char*)memdir;

    for (i = 0; i < cnt; i++) 
        buffer[i] = byte;

    printf("Llenando %d bytes de memoria con el byte %c(%X) a partir de la dirección %s\n", cnt, byte, byte, command[1]);
}

void f_mem(char ** command, tList * memory, tList * shared_memory, tList * mmap_memory)
{
    int localvar_noinit1;
    int localvar_noinit2;  
    int localvar_noinit3;
    int localvar_init1 = 0;
    int localvar_init2 = 0;
    int localvar_init3 = 0;

    if(!command[1] || !strcmp(command[1], "-all"))
    {
        printf("******Lista de bloques asignados para el proceso %ld\n", (long)getpid());
        printList(memory);
        printList(shared_memory);
        printList(mmap_memory);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Funciones programa:", &f_mem, &processCommand, &f_pid);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Funciones librerías:", &strcmp, &printf, &getpid);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables locales:", &localvar_init1, &localvar_init2, &localvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables locales (N.I.):", &localvar_noinit1, &localvar_noinit2, &localvar_noinit3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables globales:", &globalvar_init1, &globalvar_init2, &globalvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables globales (N.I.):", &globalvar_noinit1, &globalvar_noinit2, &globalvar_noinit3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables estáticas:", &staticvar_init1, &staticvar_init2, &staticvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables estáticas (N.I.):", &staticvar_noinit1, &staticvar_noinit2, &staticvar_noinit3);
        return;
    }

    if(!strcmp(command[1], "-blocks"))
    {
        printf("******Lista de bloques asignados para el proceso \t\t%ld\n", (long)getpid());
        printList(memory);
        printList(shared_memory);
        printList(mmap_memory);
    }

    if(!strcmp(command[1], "-funcs"))
    {
        printf("%30s\t\t%p,\t%p,\t%p\n", "Funciones programa:", &f_mem, &processCommand, &f_pid);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Funciones librerías:", &strcmp, &printf, &getpid);
    }
    
    if(!strcmp(command[1], "-vars"))
    {
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables locales:", &localvar_init1, &localvar_init2, &localvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables locales (N.I.):", &localvar_noinit1, &localvar_noinit2, &localvar_noinit3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables globales:", &globalvar_init1, &globalvar_init2, &globalvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables globales (N.I.):", &globalvar_noinit1, &globalvar_noinit2, &globalvar_noinit3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables estáticas:", &staticvar_init1, &staticvar_init2, &staticvar_init3);
        printf("%30s\t\t%p,\t%p,\t%p\n", "Variables estáticas (N.I.):", &staticvar_noinit1, &staticvar_noinit2, &staticvar_noinit3);
    }

    if(!strcmp(command[1], "-pmap"))
        doMemPmap();
}

void f_recurse(char ** command)
{
    if (!command[1] || !isDigitString(command[1])) return;

    recurse(atoi(command[1]));
}

void f_uid(char ** command)
{
    int id;

    if (!command[1] || //no hay argumentos
        (strcmp(command[1], "-get") && strcmp(command[1], "-set")) || //el primer argumento no es -get ni -set
        !strcmp(command[1], "-get") || //-get y lo que sea
        (!strcmp(command[1], "-set") && !command[2]) || //-set y nada mas
        (!strcmp(command[1], "-set") && !strcmp(command[2], "-l") && !command[3])) //-set -l y nada mas
    { show_credentials(); return; }

    if (!strcmp(command[1], "-set"))
    {
        if ((!isDigitString(command[2])) || //la id no ems un digito
            (isDigitString(command[2]) && seteuid(id = atoi(command[2])) == -1) || //no se pudo settear la id
            (!strcmp(command[2], "-l") && setlogin(command[3]) == -1)) //no se pudo settear el login
        { perror("Imposible cambiar credencial"); return; }
    }
}

void f_showvar(char ** command, int (*main)(int, char**, char**), char ** envp)
{
    char **env, *path;
    int i = 0;
    
    if (!command[1])
    {
        for (env = envp; *env; env++) 
            printf("%p->main arg3[%d]=(%p) %s\n", (void*)env, i++, (void*)*env, *env);
        return;
   	}

	if (!(path = getenv(command[1]))) return;

    for (env = envp; *env; env++) 
	{
        if (!strncmp(*env, command[1], strlen(command[1])) && (*env)[strlen(command[1])] == '=') 
		{
            printf("Con arg3 main %s(%p) @%p\n", *env, (void*)(*env + strlen(command[1]) + 1), (void*)main);
            break;
        }	
    }

    for (env = environ; *env; env++)
    {
        if (!strncmp(*env, command[1], strlen(command[1])) && (*env)[strlen(command[1])] == '=') 
        {
            printf("Con environ %s(%p) @%p\n", *env, (void*)*env, (void*)&environ);
            break;
        }
    }

	printf("Con getenv %s(%p)\n", path, (void*)path);
}

void f_changevar(char ** command, char ** envp)
{
    char **env, string[MAX_PROMPT];
    int position;

	if (!command[3] ||
		(strcmp(command[1], "-a") && strcmp(command[1], "-e") && strcmp(command[1], "-p"))) 
   	{
        printf("Uso: changevar [-a|-e|-p] var valor\n");
        return;
    }

	if (!getenv(command[2])) //verifica que existe la variable, no se almacena
		{ perror("Imposible cambiar variable"); return; }

	if (!strcmp(command[1], "-a"))
		env = envp;
	else if (!strcmp(command[1], "-e"))
		env = environ;

	if ((strcmp(command[1], "-p")) && (position = findVariable(command[2], env)) != -1) {
        sprintf(env[position]+strlen(command[2])+1, "%s", command[3]);
    }
	else {
		sprintf(string, "%s=%s", command[2], command[3]);
		putenv(string);
	}
}

void f_subsvar(char ** command, char ** envp)
{
    char **env, *string = MALLOC;
    int position;

	if (!string)
		{ perror("Error al asignar memoria"); free(string); return; }

	if (!command[4] ||
		(strcmp(command[1], "-a") && strcmp(command[1], "-e"))) 
   	{
        printf("Uso: subsvar [-a|-e] var1 var2 valor\n");
		free(string);
        return;
    }

	if (!getenv(command[2])) //verifica que existe la variable, no se almacena
		{ perror("Imposible cambiar variable"); free(string); return; }

	if (!strcmp(command[1], "-a"))
		env = envp;
	else if (!strcmp(command[1], "-e"))
		env = environ;

	if ((position = findVariable(command[2], env)) != -1) 
        sprintf(env[position], "%s=%s", command[3], command[4]);

    free(string);
}

void f_showenv(char ** command, char ** envp)
{
    char **env;
    int i = 0;

    //no hay argumentos o es environ, en cada caso se asigna a env el valor que corresponda
    if (!command[1] || !strcmp(command[1], "-environ"))
    {
        for (env = command[1]? environ:envp; *env; env++)
            printf("%p->%s[%d]=(%p) %s\n", (void*)env, command[1]?"environ":"main arg3", i++, (void*)*env, *env);
    }
    
    //si el argumento no es válido se indica el uso
    else if (strcmp(command[1], "-environ") && strcmp(command[1], "-addr"))
        printf("Uso: showenv [-environ|-addr]\n");
    
    //caso -addr
    else if (!strcmp(command[1], "-addr"))
        printf("environ:   %p (almacenado en %p)\nmain arg3: %p (almacenado en %p)\n", 
                (void*)environ, (void*)&environ, (void*)envp, (void*)&envp);
}

void f_fork(char ** command, tList * bg_proc)
{
    pid_t pid;

    if (!(pid = fork())) 
    {
        printf("ejecutando proceso %d\n", getpid());
        deleteList(bg_proc);
    }
    else if (pid != -1) 
        waitpid(pid, NULL, 0);
}

void f_exec(char ** command)
{
    char string[MAX_PROMPT];

    if (!command[1]) 
        { printf("Imposible ejecutar: Bad address\n"); return; }
    
    untrimString(command+1/*desde command[1]*/, string);

    system(string);
}

void f_jobs(tList * bg_proc)
{ 
    tPosL pos;
    char **process = MALLOC_PTR, *item = MALLOC, *aux_string = MALLOC, *time_string = MALLOC, *command_copy = MALLOC, *time_copy = MALLOC, *user_copy = MALLOC;
    pid_t pid;
    int status;
    time_t t; 
    time(&t); 
    struct tm *local = localtime(&t);

    for (pos = first(*bg_proc); pos; pos = next(pos))
    {
        pid = getPidFromPos(pos);
        strftime(time_string, MAX_PROMPT, "%Y/%m/%d %H:%M:%S", local);

        strcpy(aux_string, getItem(pos));
        trimString(aux_string, process);

        //obtener copia del comando
        initializeString(command_copy);
        for (int i = 7; process[i]; i++){
            strcat(command_copy, process[i]);
            strcat(command_copy, " ");
        }
        command_copy[strlen(command_copy)-1] = '\0';

        //obtener copia de la hora
        strcpy(time_copy, process[3]);
        sprintf(time_copy, "%s %s", process[3], process[4]);

        //obtener copia del username
        strcpy(user_copy, process[1]);

        if (!strcmp(process[5], "ACTIVO") || !strcmp(process[5], "PAUSADO") )
            waitpid(getPidFromPos(pos), &status, WNOHANG);
        else {
            printItem(pos);
            printf("\n");
            continue;
        }

        if (pid == -1) {
            perror("No se pudo realizar waitpid");
            freeAll(7, item, aux_string, time_string, command_copy, time_copy, user_copy, process);
            return;
        }
            
        if (WIFEXITED(status)) //el proceso ha terminado
            sprintf(item, "%d %s p=%d %s TERMINADO (%03d) %s", pid, user_copy, getpriority(PRIO_PROCESS, pid), time_copy, WEXITSTATUS(status), command_copy);

        else if (WIFSIGNALED(status)) //proceso interrumpido por una señal
            sprintf(item, "%d %s p=%d %s SENALADO (%s) %s", pid, user_copy, getpriority(PRIO_PROCESS, pid), time_copy, signalName(WTERMSIG(status)), command_copy);

        else if (WIFSTOPPED(status)) //paused
            sprintf(item, "%d %s p=%d %s PAUSADO (%s) %s", pid, user_copy, getpriority(PRIO_PROCESS, pid), time_copy, signalName(WSTOPSIG(status)), command_copy);

        updateItem(item, pos);
        printItem(pos);
        printf("\n");
    }

    freeAll(7, item, aux_string, time_string, command_copy, time_copy, user_copy, process);
}

void f_deljobs(char ** command, tList * bg_proc)
{
    char *command_getpid = MALLOC, *pid = malloc(10*sizeof(char));
    FILE *output;

    if (!command[1] ||
        (strcmp(command[1], "-term") && strcmp(command[1], "-sig"))) 
        { f_jobs(bg_proc); freeAll(2, command_getpid, pid); return; }

    if (!strcmp(command[1], "-term")) 
        strcpy(command_getpid, "ps -e --format pid,state | grep -v 'R' | grep 'T' | awk '{print $1}'");
    else if (!strcmp(command[1], "-sig"))
        strcpy(command_getpid, "ps -e --format pid,state | grep -v 'R' | grep 'Z' | awk '{print $1}'");
    
    
    if (!(output = popen(command_getpid, "r")))
        { perror("Error al ejecutar el comando"); freeAll(2, command_getpid, pid); return; }
    
    while (fgets(pid, sizeof(pid), output))
        if (kill(atoi(pid), SIGKILL) == -1) 
            { perror("Error al matar el proceso"); freeAll(2, command_getpid, pid); return; }
}

void f_job(char ** command, tList * bg_proc)
{
    tPosL pos;

    if (!command[1] || //no hay argumentos
        (!strcmp(command[1], "-fg") && !command[2]) || //el argumento es -fg y no hay command[2]
        !isDigitString(command[strcmp(command[1], "-fg")? 1:2])) //el argumento no es un digito
        printList(bg_proc);
    else if (strcmp(command[1], "-fg"))
    {
        for (pos = first(*bg_proc); pos; pos = next(pos))
            if (!strcmp(strtok(getItem(pos), " "), command[1]))
                { printItem(pos); break; }

        if (!pos) printList(bg_proc); 
    }
}


void f_invalid(char ** command, tList * bg_proc) 
{ 
    char *untrimmed_command = MALLOC, *item = MALLOC, *time_string = MALLOC;
    pid_t pid;
    time_t t; 
    time(&t); 
    struct tm *local = localtime(&t);
    bool ampersand;

    untrimString(command, untrimmed_command);
    ampersand = !strcmp(command[stringsSize(command)-1], "&");

    if (ampersand) {
        if (!(pid = fork())) {
            command[stringsSize(command) - 1] = NULL;
            freeAll(3, untrimmed_command, item, time_string);
            execvp(command[0], command);
            // Manejar errores si execvp falla
            fprintf(stderr, "Error al ejecutar el comando en segundo plano\n");
            exit(EXIT_FAILURE);
        }

        strftime(time_string, MAX_PROMPT, "%Y/%m/%d %H:%M:%S", local);
        sprintf(item, "%d %s p=%d %s ACTIVO (000) %s", pid, getenv("USER"), getpriority(PRIO_PROCESS, pid), time_string, untrimmed_command);
        item[strlen(item) - 2] = '\0'; // Quitar el &
        insertItem(item, NULL, bg_proc);
    } else {
        system(untrimmed_command);
    }

    freeAll(3, untrimmed_command, item, time_string);
}


void processCommand(char ** command, int (*main)(int, char**, char**), char ** envp, tList * command_history, tList * open_files, tList * memory, tList * shared_memory, tList * mmap_memory, tList * bg_proc)
{
    if (!command || !command[0]) return;
    char first[MAX_PROMPT];
    strcpy(first, command[0]); //reducimos los accesos al heap

    //se ejecuta el comando correspondiente a command
    if (!strcmp(first, "authors")) 
        f_authors(command);
    else if (!strcmp(first, "pid")) 
        f_pid(command);
    else if (!strcmp(first, "chdir")) 
        f_chdir(command);
    else if (!strcmp(first, "date") || !strcmp(command[0], "time")) 
        f_time(command);
    else if (!strcmp(first, "hist"))   
        f_hist(command, command_history);
    else if (!strcmp(first, "command")) 
        f_command(command, main, envp, command_history, open_files, memory, shared_memory, mmap_memory, bg_proc);
    else if (!strcmp(first, "open")) 
        f_open(command, open_files, true);
    else if (!strcmp(first, "close")) 
        f_close(command, open_files);
    else if (!strcmp(first, "dup")) 
        f_dup(command, open_files);
    else if (!strcmp(first, "listopen")) 
        f_listopen(command, *open_files);
    else if (!strcmp(first, "infosys")) 
        f_infosys();
    else if (!strcmp(first, "help")) 
        f_help(command);
    else if (!strcmp(first, "quit") || !strcmp(command[0], "exit") || !strcmp(command[0], "bye")) 
        f_quit(command_history, open_files, memory, shared_memory, mmap_memory, bg_proc);
    else if (!strcmp(first, "clear")) system("clear"); //quitar
    else if (!strcmp(first, "create")) 
        f_create(command, open_files);
    else if (!strcmp(first, "stat")) 
        f_stat(command);
    else if (!strcmp(first, "list")) 
        f_list(command);
    else if (!strcmp(first, "delete") || !strcmp(command[0], "deltree")) 
        f_delete(command);
    else if (!strcmp(first, "malloc")) 
        f_malloc(command, memory);
    else if (!strcmp(first, "shared")) 
        f_shared(command, shared_memory);
    else if (!strcmp(first, "mmap")) 
        f_mmap(command, mmap_memory);
    else if (!strcmp(first, "read")) 
        f_read(command);
    else if (!strcmp(first, "write")) 
        f_write(command);
    else if (!strcmp(first, "memdump")) 
        f_memdump(command);
    else if (!strcmp(first, "memfill")) 
        f_memfill(command);
    else if (!strcmp(first, "mem")) 
        f_mem(command, memory, shared_memory, mmap_memory); 
    else if (!strcmp(first, "recurse")) 
        f_recurse(command); 
    else if (!strcmp(first, "uid")) 
        f_uid(command);
    else if (!strcmp(first, "showvar")) 
        f_showvar(command, main, envp);
    else if (!strcmp(first, "changevar")) 
        f_changevar(command, envp);
    else if (!strcmp(first, "subsvar")) 
        f_subsvar(command, envp);
    else if (!strcmp(first, "showenv")) 
        f_showenv(command, envp);
    else if (!strcmp(first, "fork")) 
        f_fork(command, bg_proc);
    else if (!strcmp(first, "exec")) 
        f_exec(command);
    else if (!strcmp(first, "jobs"))
        f_jobs(bg_proc);
    else if (!strcmp(first, "deljobs")) 
        f_deljobs(command, bg_proc);
    else if (!strcmp(first, "job")) 
        f_job(command, bg_proc);
    else if (!strcmp(first, "hola"))
    {
        for (int i = 0; envp[i]; i++)
        {
            printf("%s\n", envp[i]);
        }
    }
    else 
        f_invalid(command, bg_proc);
}

