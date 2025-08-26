#!/bin/bash

echo "🛑 Arrêt de l'application SGI..."

# Arrêter tous les conteneurs
docker-compose down

echo "✅ Application arrêtée avec succès!"

# Optionnel: supprimer les volumes
read -p "Voulez-vous supprimer les volumes (base de données) ? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🗑️  Suppression des volumes..."
    docker-compose down -v
    echo "✅ Volumes supprimés!"
fi
