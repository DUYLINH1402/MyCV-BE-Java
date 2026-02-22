#!/bin/bash
# =====================================================
# EC2 Setup Script - Portfolio API
# Chạy script này trên EC2 instance mới
# Usage: chmod +x ec2-setup.sh && ./ec2-setup.sh
# =====================================================

set -e

echo "🚀 Starting EC2 Setup for Portfolio API..."

# =====================================================
# 1. Update system packages
# =====================================================
echo "📦 Updating system packages..."
sudo yum update -y

# =====================================================
# 2. Install Docker
# =====================================================
echo "🐳 Installing Docker..."
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER

# =====================================================
# 3. Install Docker Compose
# =====================================================
echo "🔧 Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# =====================================================
# 4. Create app directory
# =====================================================
echo "📁 Creating application directory..."
mkdir -p ~/portfolio-api
cd ~/portfolio-api

# =====================================================
# 5. Create .env file template
# =====================================================
echo "📝 Creating .env template..."
cat > .env.example << 'EOF'
# =====================================================
# Portfolio API - Production Environment Variables
# Copy file này thành .env và điền giá trị thực
# =====================================================

SPRING_PROFILES_ACTIVE=prod

# Database (AWS RDS)
DB_HOST=your-rds-endpoint.region.rds.amazonaws.com
DB_PORT=5432
DB_NAME=portfolio_db
DB_USERNAME=postgres
DB_PASSWORD=your-rds-password

# Security
JWT_SECRET=your-jwt-secret-at-least-32-characters
JWT_EXPIRATION=86400000
ADMIN_SECRET_TOKEN=your-admin-token

# Server
SERVER_PORT=8080
EOF

# =====================================================
# 6. Setup instructions
# =====================================================
echo ""
echo "✅ EC2 Setup Complete!"
echo ""
echo "📋 Next Steps:"
echo "1. Copy .env.example to .env and fill in your values:"
echo "   cp .env.example .env"
echo "   nano .env"
echo ""
echo "2. Configure GitHub Secrets in your repository:"
echo "   - EC2_HOST: Your EC2 public IP/DNS"
echo "   - EC2_USER: ec2-user"
echo "   - EC2_SSH_PRIVATE_KEY: Your EC2 private key content"
echo ""
echo "3. Push code to main branch to trigger deployment"
echo ""
echo "4. Check logs with: docker logs -f portfolio-api"
echo ""
echo "⚠️ IMPORTANT: Log out and log back in for docker group to take effect"
echo "   Or run: newgrp docker"

