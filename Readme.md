# TP5 Actuator - Denisa Dudas - 4AL1

## Description
Ce TP a pour but d'introduire la notion d'actuators de Spring Boot.

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
Chaque exercice du tp5 a été réalisé et testé sur Postman.<br>
Chaque fichier à été testé avec un coverage supérieur à 80%.<br>
La collection Postman est disponible dans ce répertoire.<br>
Le tp est également disponible sur github: https://github.com/ESGI4-AL/tp5-actuators

## Questions du TP

### Exercice 2 : Découverte des actuators

3. Que constatez-vous avec la requête suivante : http://localhost:8080/actuator/ ?<br>

```md
Nous pouvons constater que l'endpoint suivant retourne un répertoire avec tous les actuators disponibles sous forme de lien.
```

4. Que constatez-vous avec la requête suivante : http://localhost:8080/actuator/info ?

```md
Nous pouvons constater que l'endoint suivant retourne les informations que nous avons personnalisées dans l'application comme la description et l'auteur.
```

5. Que constatez-vous avec la requête suivante : http://localhost:8080/actuator/beans ?
   Arrivez-vous à identifier les objets de notre code instanciés par Spring Boot
   (Annotations stéréotypes, Bean configuration)

```md
Nous pouvons constater que l'endoint suivant retourne tous les beans gérés par le conteneur Spring avec leurs détails de configuration.
Oui, nous arrivons à repérer les objets de notre code instancié par Spring Boot comme par example:
```

````json
"rentalPropertyRepository": {
    "type": "fr.esgi.rent.repository.RentalPropertyRepository",
    "resource": "fr.esgi.rent.repository.RentalPropertyRepository defined in @EnableJpaRepositories"
}

"restTemplate": {
  "type": "org.springframework.web.client.RestTemplate",
  "resource": "class path resource [fr/esgi/rent/config/RestTemplateConfig.class]",
  "dependencies": ["restTemplateConfig"]
}
````

6. Que constatez vous avec la requête suivante :
   http://localhost:8080/actuator/configprops ?
   Arrivez-vous à identifier les propriétés de notre code (application.properties)

```md
Nous pouvons constater que l'endoint suivant expose toutes les propriétés de configuration liées aux @ConfigurationProperties.
Oui, nous arrivons à repérer les propriétés de notre code comme par example:
```

````json
"management.endpoint.health-org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties": {
    "inputs": {
        "showDetails": {
            "origin": "class path resource [application.properties] - 20:41"
        }
    }
}
````

### Health Actuator

L’actuator qui indique l’état global de l’application est l’actuator health :
http://localhost:8080/actuator/health

1. Le statut “UP” ne nous permet pas de comprendre les conditions pour que
   l’application soit considérée “UP”.

2. Ajoutez la propriété suivante pour plus de détails :
   ``management.endpoint.health.show-details=always``

3. Que constatez-vous ? Expliquez maintenant pourquoi l’application est “UP” ?

````md
Nous pouvons constater que l'endpoint suivant expose maintenant les détails de tous les composants qui contribuent à l'état de santé global de l'application.
````