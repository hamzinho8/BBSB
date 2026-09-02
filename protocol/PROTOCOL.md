# BlackBerrySmartBridge Protocol (BSB/1)

Le protocole BSB/1 est un protocole texte transitant via Bluetooth SPP (Serial Port Profile).

- **Encodage** : UTF-8
- **Fin de message** : `\n` (Line Feed)
- **Séparateur d'arguments** : `|`

## 1. Handshake & Heartbeat

### PING / PONG
Utilisé pour vérifier la connexion et maintenir le socket actif.
- `PING` (Envoyé par Android ou BlackBerry)
- `PONG` (Réponse attendue immédiatement)

### HELLO (Négociation)
Envoyé par Android lors de la connexion initiale :
`HELLO|BSB/1|ANDROID`

Le BlackBerry doit répondre :
`HELLO|BSB/1|BLACKBERRY_9790`
`READY`

## 2. Informations Système

### BATTERY
Mise à jour du niveau de batterie (en %).
- `BATTERY|78`

## 3. Notifications

### NOTIFICATION
Transfère une notification depuis Android vers l'écran du BlackBerry.
Format : `NOTIFICATION|id_unique|nom_application|expéditeur|contenu_du_message`
Exemple : `NOTIFICATION|152|WhatsApp|Mohamed|Salut, tu viens ce soir ?`

### REPLY (BlackBerry -> Android)
L'utilisateur tape une réponse sur le clavier QWERTY.
Format : `REPLY|id_unique|texte_de_reponse`
Exemple : `REPLY|152|Oui j'arrive !`

## 4. Appels Téléphoniques

### CALL_INCOMING
Android signale un appel entrant.
Format : `CALL_INCOMING|id_unique|Nom_Contact|Numero`
Exemple : `CALL_INCOMING|999|Amina|0612345678`

### CALL_ANSWER (BlackBerry -> Android)
L'utilisateur appuie sur la touche verte.
Format : `CALL_ANSWER|id_unique`

### CALL_REJECT (BlackBerry -> Android)
L'utilisateur appuie sur la touche rouge.
Format : `CALL_REJECT|id_unique`

## 5. Erreurs

Si une commande est mal formatée ou non reconnue :
- `ERROR|UNKNOWN_COMMAND`
- `ERROR|INVALID_PACKET`
- `ERROR|MESSAGE_TOO_LONG`
