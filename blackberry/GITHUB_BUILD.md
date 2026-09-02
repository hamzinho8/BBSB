# Configuration GitHub Actions (BlackBerry JDE)

Le compilateur historique BlackBerry (`rapc.exe`) nécessite l'environnement propriétaire Java ME fourni par RIM/BlackBerry. Ce SDK n'est **pas** open-source et ne peut pas être pré-installé ou téléchargé dynamiquement sur les runners hébergés par GitHub.

## Utilisation d'un Self-Hosted Runner (Recommandé)

Pour que la CI GitHub compile automatiquement vos fichiers `.cod` :

1. **Préparez un serveur Windows** (ou une VM locale).
2. **Installez BlackBerry JDE 7.1.x** depuis vos archives légitimes.
3. **Installez le Runner GitHub** (`Settings > Actions > Runners > New self-hosted runner`).
4. **Configurez les variables d'environnement** sur le serveur Windows :
   - `BLACKBERRY_JDE_HOME` pointant vers le dossier d'installation du JDE.
   - `JAVA_HOME` pointant vers un JDK 32-bit (généralement JDK 1.6 requis par le JDE).
5. Démarrez le service du runner.

Le workflow `.github/workflows/blackberry.yml` détectera automatiquement la présence de la variable `BLACKBERRY_JDE_HOME` et lancera la compilation RAPC.

## Sans Self-Hosted Runner

Si le JDE n'est pas détecté, le workflow exécutera uniquement l'étape "Validate Source Structure" pour s'assurer que le code respecte l'arborescence requise, et ignorera gracieusement l'étape de compilation sans faire échouer l'ensemble du pipeline.
