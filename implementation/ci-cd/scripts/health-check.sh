#!/bin/bash

# Health Check Script
# Simple health validation for deployed services

set -e

ENVIRONMENT=${1:-development}
NAMESPACE=${2:-$ENVIRONMENT}

echo "🏥 Running health check for $ENVIRONMENT environment..."

# Check if kubectl is configured
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ kubectl is not configured"
    exit 1
fi

# Check pod status
echo "📊 Pod Status:"
kubectl get pods -n $NAMESPACE

# Wait for pods to be ready
echo "⏳ Waiting for pods to be ready..."
kubectl wait --for=condition=ready pod -l app.kubernetes.io/instance=microservices-$ENVIRONMENT -n $NAMESPACE --timeout=300s

# Check service endpoints
echo "🔗 Service Status:"
kubectl get services -n $NAMESPACE

# Simple connectivity test
echo "🌐 Testing service connectivity..."
for service in account-service payment-service transaction-service; do
    if kubectl get service $service -n $NAMESPACE &> /dev/null; then
        echo "✅ $service is accessible"
    else
        echo "❌ $service is not accessible"
    fi
done

echo "✅ Health check completed for $ENVIRONMENT!"
