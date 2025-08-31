#!/bin/bash

# Standard Deployment Script
# Follows DevSecOps best practices for Kubernetes deployment

set -e

ENVIRONMENT=${1:-development}
NAMESPACE=${2:-$ENVIRONMENT}

# Set IMAGE_TAG to current commit if not provided
IMAGE_TAG=${IMAGE_TAG:-$(git rev-parse HEAD 2>/dev/null || echo "latest")}

echo "🚀 Deploying to $ENVIRONMENT environment..."
echo "📍 Namespace: $NAMESPACE"
echo "🏷️ Image Tag: $IMAGE_TAG"

# Validate environment
case $ENVIRONMENT in
  development|staging|production)
    echo "✅ Valid environment: $ENVIRONMENT"
    ;;
  *)
    echo "❌ Invalid environment. Use: development, staging, or production"
    exit 1
    ;;
esac

# Check if kubectl is configured
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ kubectl is not configured or cluster is not accessible"
    exit 1
fi

# Deploy with Helm
echo "📦 Deploying with Helm..."
helm upgrade --install microservices-$ENVIRONMENT ./charts/microservices \
    --namespace $NAMESPACE \
    --create-namespace \
    --set image.repository=$REGISTRY_URL/$REGISTRY_NAMESPACE \
    --set image.tag=$IMAGE_TAG \
    --set environment=$ENVIRONMENT \
    --wait --timeout=10m

# Health check
echo "🔍 Running health check..."
sleep 30
kubectl get pods -n $NAMESPACE
kubectl wait --for=condition=ready pod -l app.kubernetes.io/instance=microservices-$ENVIRONMENT -n $NAMESPACE --timeout=300s

echo "✅ Deployment to $ENVIRONMENT completed successfully!"
