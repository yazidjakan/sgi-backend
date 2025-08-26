#!/bin/bash

echo "🚀 Démarrage de l'application SGI..."

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez installer Docker d'abord."
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose n'est pas installé. Veuillez installer Docker Compose d'abord."
    exit 1
fi

# Arrêter les conteneurs existants
echo "🛑 Arrêt des conteneurs existants..."
docker-compose down

# Nettoyer les images existantes (optionnel)
read -p "Voulez-vous nettoyer les images existantes ? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🧹 Nettoyage des images..."
    docker-compose down --rmi all
fi

# Construire et démarrer les services
echo "🔨 Construction des images..."
docker-compose build --no-cache

echo "🚀 Démarrage des services..."
docker-compose up -d

# Attendre que les services soient prêts
echo "⏳ Attente du démarrage des services..."
sleep 30

# Vérifier l'état des services
echo "📊 État des services:"
docker-compose ps

# Attendre que tous les services soient prêts
echo "⏳ Attente que tous les services soient healthy..."
sleep 60

# Vérifier les services dans Eureka
echo "🔍 Vérification des services dans Eureka..."
if [ -f "./check-services.sh" ]; then
    chmod +x ./check-services.sh
    ./check-services.sh
fi

echo ""
echo "✅ Application démarrée avec succès!"
echo ""
echo "🌐 Frontend: http://localhost"
echo "🔧 Gateway API: http://localhost:9999"
echo "📊 Discovery Service: http://localhost:8762"
echo "⚙️  Config Service: http://localhost:8888"
echo "🗄️  MySQL: localhost:3306"
echo ""
echo "📝 Logs: docker-compose logs -f [service-name]"
echo "🛑 Arrêt: docker-compose down"
echo ""
echo "🔍 Pour vérifier les services: ./check-services.sh"
