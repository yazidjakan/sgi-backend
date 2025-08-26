# SGI - Système de Gestion d'Incidents

Application complète de gestion d'incidents avec microservices backend et frontend Angular.

## 🏗️ Architecture

### Backend (Microservices)
- **Discovery Service** (8762) - Service de découverte Eureka
- **Config Service** (8888) - Service de configuration centralisée
- **Gateway Service** (9999) - API Gateway (point d'entrée unique)
- **Auth Service** (8075) - Authentification et autorisation
- **User Service** (8076) - Gestion des utilisateurs
- **Incident Service** (8077) - Gestion des incidents
- **SLA Service** (8079) - Gestion des SLAs
- **Notification Service** (8080) - Notifications
- **Report Service** (8081) - Génération de rapports
- **Prediction Service** (5000) - Prédictions ML (Python/FastAPI)

### Frontend
- **Angular Application** (80) - Interface utilisateur

### Base de données
- **MySQL** (3306) - Base de données principale

## 🚀 Déploiement avec Docker

### Prérequis
- Docker
- Docker Compose

### Démarrage rapide

1. **Cloner le projet**
```bash
git clone <repository-url>
cd DXC-project
```

2. **Démarrer l'application**
```bash
cd sgi-backend
chmod +x start.sh
./start.sh
```

3. **Accéder à l'application**
- Frontend: http://localhost
- API Gateway: http://localhost:9999
- Discovery Service: http://localhost:8762
- Config Service: http://localhost:8888

### Commandes utiles

**Démarrer les services**
```bash
docker-compose up -d
```

**Voir les logs**
```bash
# Tous les services
docker-compose logs -f

# Service spécifique
docker-compose logs -f gateway-service
```

**Arrêter les services**
```bash
docker-compose down
```

**Reconstruire les images**
```bash
docker-compose build --no-cache
docker-compose up -d
```

**Nettoyer complètement**
```bash
docker-compose down -v --rmi all
```

## 🔧 Configuration

### Variables d'environnement
Les services utilisent les variables d'environnement suivantes :
- `DISCOVERY_SERVICE_URL`: URL du service de découverte
- `CONFIG_SERVICE_URL`: URL du service de configuration
- `MYSQL_ROOT_PASSWORD`: Mot de passe root MySQL
- `MYSQL_PASSWORD`: Mot de passe utilisateur MySQL

### Ports exposés
- 80: Frontend Angular
- 9999: API Gateway
- 8762: Discovery Service
- 8888: Config Service
- 8075-8081: Services métier
- 5000: Prediction Service
- 3306: MySQL

## 📊 Monitoring

### Health Checks
Tous les services Spring Boot incluent des health checks automatiques.

### Logs
Les logs sont disponibles via Docker Compose :
```bash
docker-compose logs -f [service-name]
```

## 🔒 Sécurité

- Les services communiquent via un réseau Docker privé
- Le Gateway Service agit comme point d'entrée unique
- Authentification JWT pour les API
- Headers de sécurité configurés dans nginx

## 🐛 Dépannage

### Problèmes courants

1. **Ports déjà utilisés**
```bash
# Vérifier les ports utilisés
netstat -tulpn | grep :80
netstat -tulpn | grep :9999
```

2. **Services qui ne démarrent pas**
```bash
# Vérifier les logs
docker-compose logs [service-name]
```

3. **Problèmes de base de données**
```bash
# Redémarrer MySQL
docker-compose restart mysql
```

4. **Nettoyer et redémarrer**
```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d
```

## 📝 API Endpoints

Tous les endpoints passent par le Gateway Service (port 9999) :

- `/api/v1/auth/**` - Authentification
- `/api/v1/users/**` - Gestion des utilisateurs
- `/api/v1/incidents/**` - Gestion des incidents
- `/api/v1/slas/**` - Gestion des SLAs
- `/api/v1/notifications/**` - Notifications
- `/api/v1/reports/**` - Rapports
- `/api/v1/predictions/**` - Prédictions ML

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature
3. Commit les changements
4. Push vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.
