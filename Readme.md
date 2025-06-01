# TP4 Spring - Denisa Dudas - 4AL1

## Description
Ce projet Spring Boot vise à créer une API REST pour la gestion de locations immobilières avec une fonctionnalité d'intégration avec les stations Vélib.

## Démarrer les Applications
Chaque application peut être exécutée indépendamment:

### Rent API (port 8080)
```bash
cd rent-api
mvn clean compile
mvn spring-boot:run
```

### Station Velib API (port 8081)
```bash
cd station-velib-api
mvn clean compile
mvn spring-boot:run
```
## Informations
Chaque exercice du tp4 a été réalisé et testé sur Postman.<br>
Chaque fichier à été testé avec un coverage supérieur à 80%.<br>
La collection Postman est disponible dans ce répertoire.<br>
Le tp est également disponible sur github: https://github.com/denisadudas/tp4-spring-rent-api

## Questions en fin de TP

1. A quoi sert le paramètre cascade de l’annotation @ManyToOne
   dans le code ci-dessous ? Vous donnerez un exemple concret <br>

```md
Le paramètre cascade definit quelles sont les opérations qui vont être propagées de l'entité parent vers l'entité enfant.
Exemple:
```
```java
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "property_type_id")
private PropertyTypeEntity propertyType;
```
```md
Dans cette example si on sauvegarde un RentalPropertyEntity, le PropertyTypeEntity associé serai aussi sauvegardé automatiquement.
```

2. En JEE, nous avons vu qu’il était nécessaire de créer des objets pour gérer la
   connexion à la base de données. En Spring Boot, nous n’avons pas eu besoin de le
   faire. En effet, seul la présence de la dépendance spring-boot-starter-data-jpa
   et du paramétrage des propriétés ci-dessous suffisent. Citez le nom de cette
   fonctionnalité Spring Boot et expliquez son fonctionnement.

```md
Le nom de la fonctionnalité de Spring Boot est l'auto-configuration et Spring Boot utilise cette fonctionnalité pour configurer automatiquement les beans selon les dépendances présentes dans le classpath.
Concrétement Spring Boot scanne les dépendances au démarrage, il utilise les classes @AutoConfiguration, puis il applique les configurations selon les proprietés décrites dans application.properties etensuite il nous reste à injecter les objets et les utiliser.
```

3. Lorsque vous exécutez la méthode main() d’une application Spring Boot, quelle
   différence constatez-vous avec une application JEE ?
   Intéressez vous notamment au déploiement du jar dans Tomcat.

```md
Une application JEE génère un fichier WAR qui doit être déployé dans un serveur d'applications externe comme Tomcat, tandis qu'une application Spring Boot génère un JAR exécutable qui embarque directement un serveur Tomcat.
Ainsi, au lieu de devoir installer et configurer un serveur séparément puis y déployer notre WAR, nous pouvons simplement exécuter notre application, ce qui simplifie le déploiement et rend l'application auto-suffisante.
```

4. Quelles sont les annotations permettant aux objets RentalPropertyRepository et
   RentalPropertyDtoMapper d’être injectés par constructeur ?

```md
Les annotations qui permettent à RentalPropertyRepository et RentalPropertyDtoMapper d'être injectés par constructeur sont @Repository pour le repository et @Component pour le mapper.
```

5. Dans l’exercice 6, quelle annotation permet d’appliquer des contraintes de validation
   pour chaque champ du body request (corps de la requête) ?

```md
L'annotation @Valid permet d'appliquer des contraintes de validation pour chaque champ du body request dans l'exercice 6.
```

6. Quelles sont les annotations utilisées pour injecter HttpClient par constructeur ?
   Et pourquoi ?

```md
Les annotations utilisées pour injecter RestTemplate (équivalent HttpClient) par constructeur sont @Component sur la classe VelibStationClient, @Configuration sur la classe de configuration, et @Bean sur la méthode qui crée le RestTemplate.
Ces annotations sont nécessaires parce que RestTemplate n'est pas automatiquement configuré par Spring Boot. Nous devons explicitement créer un bean avec @Bean dans une classe @Configuration, puis Spring peut l'injecter dans notre classe @Component.
Cela nous permet de centraliser la configuration du client HTTP et de le réutiliser dans plusieurs classes si nécessaire.
```

7. En Spring Boot, quelle annotation peut-on utiliser pour injecter les valeurs des
   propriétés ci-dessous (présentes dans application.properties) dans les champs d’un
   objet Java ? Et pourquoi ?

```md
L'annotation @Value peut être utilisée pour injecter les valeurs des propriétés d'application.properties dans les champs d'un objet Java.
Elle est utile parce qu'elle permet d'externaliser la configuration de notre application : au lieu de coder en dur des valeurs comme l'URL de l'API vélib, nous pouvons les définir dans application.properties et les injecter avec @Value("${velib.stations.api.url}")
```
