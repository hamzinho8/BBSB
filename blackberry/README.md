# BlackBerrySmartBridge

A native BlackBerry Java application for BlackBerry OS 7.x (Target: BlackBerry Bold 9790).

This project transforms your BlackBerry into a smart Bluetooth companion terminal for a modern Android smartphone.

## 1. Objectif
Le but de cette application est de déporter les notifications, appels et actions de réponse d'un smartphone Android moderne vers le clavier physique et l'écran du BlackBerry Bold 9790.

## 2. Appareil & OS Cible
- **Device:** BlackBerry Bold 9790
- **OS:** BlackBerry OS 7.x (BBOS 7.1)
- **SDK:** BlackBerry Java Development Environment (JDE) 7.1.x

## 3. Architecture & Bluetooth
L'application communique via **Bluetooth Classic (RFCOMM/SPP)**.
- Le BlackBerry agit en tant que Serveur SPP (`btspp://localhost:...`).
- L'Android s'y connecte en tant que Client.
- L'UUID utilisé est `00001101-0000-1000-8000-00805F9B34FB`.

Toutes les opérations Bluetooth s'exécutent de façon asynchrone dans un `Thread` dédié (`BluetoothServer.java`) pour ne jamais bloquer l'interface UI (`SmartBridgeScreen.java`), qui est mise à jour via `invokeLater()`.

## 4. Protocole (BSB/1)
Format de texte brut encadré par `\n`.
Voir `/protocol/PROTOCOL.md` pour la documentation complète.

## 5. Compilation Locale (Windows)
1. Installez le **BlackBerry JDE 7.1.0**.
2. Définissez la variable d'environnement :
   `set BLACKBERRY_JDE_HOME=C:\Chemin\Vers\BlackBerry JDE 7.1.0`
3. Lancez le script de compilation :
   `build-blackberry.bat`
4. Les fichiers `BlackBerrySmartBridge.cod` et `BlackBerrySmartBridge.jad` seront générés.

## 6. Compilation via GitHub Actions
Le workflow `.github/workflows/blackberry.yml` est configuré pour compiler automatiquement l'application SI ET SEULEMENT SI le SDK est présent. Étant donné que le JDE est propriétaire, GitHub Hosted Runners ne l'incluent pas. Voir `GITHUB_BUILD.md` pour configurer un Self-Hosted Runner.

## 7. Installation sur le BlackBerry
1. Copiez les fichiers `.cod` et `.jad` sur la carte SD de votre BlackBerry.
2. Ouvrez le Gestionnaire de Fichiers BlackBerry, cliquez sur le fichier `.jad`.
3. Cliquez sur **Télécharger/Installer**.
4. Autorisez l'accès Bluetooth et Réseau.
5. Lancez **BlackBerrySmartBridge**.
