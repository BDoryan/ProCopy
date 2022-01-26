
# Bienvenue sur ProC0PY

Ce programme est un petit projet créé lors d'un stage de baccalauréat professionnel système numérique, lorsque j'avais du temps libre pendant ce stage, je développé sur ce logiciel. En effet, je l'ai créé en grande partie pour du divertissement, ce n'est pas dans mon intérêt de le faire devenir l'outil numéro un.

# Son rôle

ProC0PY, comme son nom l'indique, il permet de gérer des fichiers et donc de les copier de manière simple et efficace, vous avez la possibilité de copier le contenu d'un dossier utilisateur mais aussi de copier un dossier à un autre dossier destinataire.

Le programme ne se contente pas uniquement de copier, mais vous avez la possibilité de copier des fichiers avec certaines conditions, par exemple: ignore un fichier avec l'extension .exe ou un .png

# Transfert des fichiers

Comme je l'ai dit précédemment, vous avez la possibilité de copier des fichiers en sélectionnant un dossier de source pour enfin copier vers un dossier destinataire.

Vous avez la possibilité de configurer le transfert, comme ignorer des extensions, ignorer les fichiers vide, ignorer les dossiers vide, vider le dossier destinataire, copier les sous-dossier et autres.

## Technologie multithreading

Il faut savoir que lorsqu'on copie des fichiers, cela peut prendre du temps, ce temps assez précieux et c'est pour cela que j'ai mis en place un système qui permet de copier de manière plus rapide. Je pense pas inventer une nouvelle technologie, mais je pense qu'elle peut-être pratique pour les plus pressais.

J'ai réalisé un test dernièrement, j'ai copié le dossier "Program Files" (5.05 Go) du disque principal (SSD) vers un second disque (HDD), le processus lancer sans la technologie multithreading à durée **1m45s278ms**. Ensuite j'ai réalisé un autre test avec l'option d'activé (avec 100 thread maximum) la processus à durée **1m15s820ms**, on remarque une différence, celle-ci aurait pu être plus important si j'avais défini le nombre au-dessus de 100.

De plus, la copie avec Windows directement (CTRL+C et CTRL+V) à durée **2m30s260ms**, je suppose que Windows effectue plusieurs vérification qui fait ralentir le processus.

**Enfin pour terminé, il faut savoir que cette technologie est gourmande et qu'il faut un processeur d'adapter pour l'utiliser, sinon on ne ressent pas la différence (voir même le contraire).**

# Importer un utilisateur

Le programme met à disposition un outil assez utile. En effet, il vous permet de récupérer les fichiers principaux d'un utilisateur (Documents, Musiques, Images).

Si vous avez envie de rajouter des fichiers à la copie, vous en avez la possibilité avec le sélecteur de fichier. Vous avez tout simple à prendre ce qui vous intéresse.

# API

**Développé en Java 8**, vous avez la possibilité de faire la mise à niveau vous même.

Ce projet n'est pas qu'un simple programme, derrière j'utilise l'API ProC0PY, celle-ci est le cœur du projet, c'est elle qui gère tout.

    ProCopy proCopy = new ProCopy(
    new File("C:\\Users\\Utilisateur\\Documents\\ProCopy\\source"), 
    new File("C:\\Users\\Utilisateur\\Documents\\ProCopy\\destination"));   
    proCopy.setConfiguration(new ProCopyConfiguration(  
            ProCopyMode.COPY, /* COPY or CUT files */
            true, /* ignore error */
            true, /* overwrite*/
            false, /* copy empty directory */
            false,  /* copy empty file */
            true, /* copy sub directory */
            true, /* copy hidden file */
            true, /* copy hidden directory */
            false, /* use mutlithreading */
            true, /* clear destination directory */ 
            2000) /* max thread if you use multithreading */ 
    );
    proCopy.start();

# Images

### Interface du transfert de fichier![Interface du transfert de fichier](https://i.ibb.co/X39Y6pG/Capture-d-cran-2022-01-26-090619.png)
### Interface du transfert de fichier
![Interface importation de fichier](https://i.ibb.co/fvCRy6Z/Capture-d-cran-2022-01-26-090720.png)

# Téléchargement

Si vous avez l'envie d'utiliser le programme, il vous faut l'éxécuter avec la version 8 de Java.

    /target/procopy-java-8.jar
[Lien de téléchargement](https://github.com/BDoryan/ProCopy/target/procopy-java-8.jar)