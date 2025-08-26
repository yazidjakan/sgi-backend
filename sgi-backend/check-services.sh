#!/bin/bash

echo "🔍 Vérification des services dans Eureka..."

# Attendre que Eureka soit prêt
echo "⏳ Attente du démarrage d'Eureka..."
sleep 10

# Vérifier Eureka
echo "📊 État d'Eureka:"
curl -s http://localhost:8762/actuator/health | jq . 2>/dev/null || echo "Eureka n'est pas encore prêt"

echo ""
echo "🔍 Services enregistrés dans Eureka:"
curl -s http://localhost:8762/eureka/apps | grep -o '<instanceId>[^<]*</instanceId>' | sed 's/<instanceId>\(.*\)<\/instanceId>/\1/' 2>/dev/null || echo "Aucun service trouvé"

echo ""
echo "🏥 Health checks des services:"
echo "Auth Service:"
curl -s http://localhost:8075/actuator/health | jq . 2>/dev/null || echo "Auth service non disponible"

echo ""
echo "Gateway Service:"
curl -s http://localhost:9999/actuator/health | jq . 2>/dev/null || echo "Gateway service non disponible"

echo ""
echo "🔗 Test de routage du gateway:"
echo "Test de /api/v1/auth/login:"
curl -s -X POST http://localhost:9999/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"test"}' \
  -w "\nHTTP Status: %{http_code}\n" || echo "Erreur de connexion au gateway"

echo ""
echo "✅ Vérification terminée!"
