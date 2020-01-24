# Expression des besoins

One Paragraph of project description goes here

## Getting Started

L'application à développer est une application permettant la gestion des finances personnelles.L'utilisateur pourra gérer un seul compte en banque avec l'application. Un compte est composéd'un libellé et d'un solde de départ. La gestion se fait en ajoutant, modifiant ou supprimant desopérations et en rapprochant les opérations pour vérifier les comptes.

### Contraintes fonctionnelles et de données

### Opérations

Chaque opération est composée des éléments suivants: date, tiers, somme, moyen de paiement etsi cette opération a été rapprochée ou pas (cette dernière donnée sera éditable par un écran derapprochement dédié).Les moyens de paiement sont fixés par l'application (non éditables par l’utilisateur).Pour les tiers l'utilisateur pourra à l'édition d'une opération saisir un nouveau tiers ou utiliser untiers saisi précédemment pour une autre opération.

### Écran d'accueil

Un écran d'accueil permettra d'ajouter des opérations, de visualiser les opérations du comptes(sur un mois), de démarrer un rapprochement et devra afficher un résumé du compte pour le moisen cours: mois en cours, total débits, total crédit, solde. Le solde sera affiché en vert si il estpositif et en rouge si il est négatif.

### Visualisation des comptes mensuels

La visualisation des opérations sur un mois devra afficher sous forme de tableau/liste/grille (auchoix) les opérations du mois en cours triées par date avec les éléments suivants pour chaqueopération: date, tiers, débit, crédit, solde (calculé pour chaque opération, à partir du solde audébut du mois). Une sélection d’une opération (par exemple un clic) affichera le détail éditablede l'opération  avec toutes  ses informations. Un «swipe» supprimera l'opération  avec uneconfirmation. Par défaut sera affiché le mois actuel, et il sera possible de visualiser d’autres mois(précédents/suivants). On aura une indication pour savoir si une opération est rapprochée ou pas.


### Rapprochements

Un rapprochement consiste à marquer manuellement les opérations inscrites dans un relevémensuel envoyé par la banque jusqu'à ce que le solde des opérations rapprochées calculé parl'application soit identique au solde marqué sur le relevé. Le solde rapproché se calcule à partir du dernier solde rapproché auquel on additionne (ousoustrait) les opérations rapprochée manuellement par l'utilisateur.Lors   du   lancement   d'un   rapprochement   l'utilisateur   saisira   le   solde   du   relevé   puis   lerapprochement se fera avec une vue identique à la visualisation du compte avec uniquement lesopérations non rapprochées. Dans cette vue un clic sur une opération marquera l'opérationcomme temporairement rapprochée (cela devra être visible) et l'utilisateur aura le moyen (devotre choix) de connaître la différence entre le solde à atteindre et celui calculé. L'utilisateur nepourra valider le rapprochement que si la différence est à zéro. Tant que le rapprochement n’estpas validé il pourra être continué à tout moment. La validation du rapprochement marquera lesopérations comme définitivement rapprochées.


## Contraintes non fonctionnelles.

Le développement sera effectué pour être compatible avec les plateformes Android et devraobligatoirement être développé à l'aide du SDK officiel avec le langage Java et compatible avecdes terminaux en version API 23.Les données seront sauvegardées dans une base de données locale au terminal.L'application devra au maximum simplifier la vie de l'utilisateur: initialisation correcte desformulaires, choix pertinents des types de saisies pour les champs textes, ...

### Fonctionnalités supplémentaires

- CRUD pour les tiers: ajouter la possibilité de lister, ajouter, modifier et supprimer (si possible)les tiers.
- CRUD pour les  moyens de paiement  : ajouter la possibilité de lister, ajouter, modifier etsupprimer (si possible) les moyens de paiements.
- Historique des rapprochements: afficher la liste des rapprochement du plus récent au plusancien, avec les détails (date, solde).