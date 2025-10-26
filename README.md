# 🧩 Group2 Final Project 3

This repository contains the full DevOps project structure for **Group 2**, built following a complete cloud-native CI/CD architecture using **Azure Kubernetes Service (AKS)**, **Terraform**, **GitHub Actions**, and **Docker**.

---

## 🧱 Project Structure

```bash
.github/
│
└── workflows/
    ├── infra-pipeline.yml        # CI/CD for infrastructure (Terraform + Ansible)
    ├── k8s-pipeline.yml          # CI/CD for Kubernetes deployments
    ├── frontend-pipeline.yml     # CI/CD for React frontend build & deploy
    └── backend-pipeline.yml      # CI/CD for Spring Boot backend build & deploy

infra/
│
├── terraform/                   # IaC for Azure resources (AKS, SQL, VNet, etc.)
│   ├── main.tf
│   ├── variables.tf
│   └── outputs.tf
│
└── ansible/                     # Configuration management and provisioning
    └── playbook.yml

k8s/
│
├── deployments/                 # Pod deployments for frontend and backend
│   ├── frontend-deployment.yaml
│   └── backend-deployment.yaml
│
├── services/                    # Service exposures within cluster
│   ├── frontend-service.yaml
│   └── backend-service.yaml
│
├── ingress/                     # Ingress controller or Application Gateway rules
│   └── ingress.yaml
│
├── secrets/                     # Kubernetes secrets and configurations
│   └── app-secrets.yaml
│
└── README.md

frontend/
│
├── src/                         # React + TypeScript source code
├── public/
├── package.json
├── vite.config.ts
├── Dockerfile                   # Frontend Docker build image
└── README.md

backend/
│
├── src/                         # Spring Boot application source code
├── pom.xml
├── Dockerfile                   # Backend Docker build image
└── README.md
