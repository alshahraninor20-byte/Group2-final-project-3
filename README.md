# 🍔 **Burger Builder App — AKS Migration Project**

## 📚 **Table of Contents**
- [🌐 Overview](#-overview)
- [🧰 Tech Stack](#-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🏗️ Infrastructure Overview](#️-infrastructure-overview)
- [⚙️ Deployment Workflow](#️-deployment-workflow)
- [🧪 Validation & Testing](#-validation--testing)
- [👥 Team Members](#-team-members)
- [📬 Contact](#-contact)

---
## 🖼️ **Project Documentation**

For screenshots, system diagrams, and monitoring visuals, see the detailed documentation here:  
👉 [**View Project Docs →**](./docs/README.md)

---

## 🌐 **Overview**
Welcome to our **final DevOps bootcamp project** 🎓  

In this challenge, our team re-architected and migrated the **Burger Builder App** to a **cloud-native Azure Kubernetes Service (AKS)** environment 🧩.  
The mission: build a **secure, scalable, automated** system with:
> 🪄 *Infrastructure as Code (Terraform), CI/CD with GitHub Actions, and full observability using Prometheus + Grafana + Loki.*

---

## 🧰 **Tech Stack**

| Layer | Technologies |
|:------|:--------------|
| 💻 **Frontend** | React 18 + TypeScript (Vite • React Router • Axios • Context API • CSS Modules) • Docker containerization |
| ⚙️ **Backend** | Java 21 • Spring Boot • Maven • Docker containerization |
| ☁️ **Infrastructure** | Terraform (IaC) • Azure Kubernetes Service (AKS) • Azure SQL • Private Endpoints • VNet/Subnets • Private Endpoints • Azure Key Vault |
| 🔁 **CI/CD** | GitHub Actions (Infra / Backend / Frontend Pipelines) |
| 🧷 **Security** | Azure Key Vault • Private Endpoints • Least-privilege RBAC • Network Policies |
| 📈 **Monitoring** | Prometheus • Grafana • Loki |

---

## 📁 **Project Structure**

```bash
.github/
└── workflows/
    ├── infra.yml                  # CI/CD pipeline for infrastructure provisioning
    ├── backend.yml                # CI/CD pipeline for backend deployment
    └── frontend.yml               # CI/CD pipeline for frontend deployment

app/
├── backend/                       # Spring Boot backend (Java 21)
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src/
│   └── TESTING.md
│
├── frontend/                      # React + TypeScript frontend (Vite)
│   ├── Dockerfile
│   ├── index.html
│   ├── nginx.conf
│   ├── package.json
│   ├── public/
│   ├── src/
│   ├── vite.config.ts
│   └── vitest.config.ts
│
├── environment.env                # Main environment variables
├── environment.env.example        # Example template for env variables
└── README.md                      # App-specific documentation

k8s/                               # Kubernetes manifests for AKS deployment
├── backend/
│   ├── cm.yml                     # ConfigMap for backend
│   ├── deployment.yml             # Backend Deployment
│   ├── ingress.yml                # Ingress routing for backend
│   └── service.yml                # Backend Service definition
│
├── frontend/
│   ├── deployment.yml             # Frontend Deployment
│   ├── ingress.yml                # Ingress routing for frontend
│   └── service.yml                # Frontend Service definition
│
├── namespace.yml                  # Namespace definition for project
└── loki-fixed.json                # Loki configuration for log collection

terraform/                         # Infrastructure as Code (Terraform)
├── main.tf                        # Root Terraform configuration
├── locals.tf                      # Local variables
├── outputs.tf                     # Output definitions
├── providers.tf                   # Azure provider setup
├── variables.tf                   # Global variables
└── modules/
    ├── aks/                       # AKS cluster module
    ├── privatelink/               # Private Endpoint module
    ├── resource_group/            # Resource Group module
    ├── sql/                       # Azure SQL Database module
    ├── subnet/                    # Subnet module
    ├── vault/                     # Azure Key Vault module
    └── vnet/                      # Virtual Network module
```
---

## ⚙️ **Deployment Workflow**

---

### 1️⃣ **Provision Infrastructure Pipeline – `infra.yml`**  
- 🚀 **Workflow Name:** `Provision infra` — manually triggered via **workflow_dispatch**.  
- 🏗️ Provisions full Azure infrastructure using **Terraform** in the `terraform` directory.  
- Authenticates with **Azure Credentials** stored in GitHub secrets.  
- Runs `terraform init → validate → plan → apply` to deploy infrastructure components.  
- ✅ Uses **Azure Storage Remote Backend** for Terraform state management.  
- 🔐 Pushes DB credentials to Key Vault after deployment.  
- ⚙️ Connects to AKS, sets up **Helm**, and installs:  
  - **NGINX Ingress Controller** for routing  
  - **Prometheus**, **Grafana**, and **Loki** stacks for monitoring and logging  
- 📊 Automatically imports Grafana dashboards and outputs Grafana access details.

---

### 2️⃣ **AKS Cluster Setup – `set k8s`**  
- 🔧 Configures the **Azure Kubernetes Service (AKS)** cluster after infrastructure provisioning.  
- Authenticates to Azure and exports Terraform-based Azure credentials.  
- Runs `az aks get-credentials` to connect `kubectl` to the cluster.  
- Creates required Kubernetes namespaces.  
- Deploys **backend** and **frontend** apps with associated ConfigMaps, Services, Ingress, HPAs, NetworkPolicies, and ServiceAccounts.  
- 🚀 Verifies cluster connectivity using `kubectl get nodes` and validates deployments with `kubectl get all`.  
- Checks **HPA scaling status** and retrieves **Ingress IP** for external access.  

---

### 3️⃣ **Backend Deployment Pipeline – `backend.yml`**  
- 🧱 Builds and tests the **Spring Boot (Java 21)** backend.  
- Packages the app with **Maven**, containerizes it using **Docker**, and pushes the image to a container registry.  
- Authenticates to **Azure** and retrieves DB credentials from **Azure Key Vault** to create Kubernetes secrets.  
- Connects to **AKS**, verifies the cluster, and updates the backend deployment with the latest image tag.  
- Uses **ConfigMaps** and **Secrets** for configuration management.  
- Includes **readiness** and **liveness probes** for health monitoring.  
- 🔁 Performs rolling updates and ensures stable deployments through `kubectl rollout status`.  

---

### 4️⃣ **Frontend Deployment Pipeline – `frontend.yml`**  
- 💻 Builds and tests the **React + TypeScript (Vite)** frontend.  
- Packages the app into a **Docker image** and pushes it to a container registry.  
- Authenticates with **Azure** and connects to the **AKS** cluster.  
- Dynamically injects the backend API URL using the **Ingress LoadBalancer IP**.  
- Deploys the updated image to **AKS** using manifests under `k8s/frontend/`.  
- 🧩 Uses **ConfigMaps** for environment configuration.  
- 🌐 Exposes the frontend publicly via the **NGINX Ingress Controller** with HTTPS support.  
- 🔁 Performs rolling updates and verifies deployment health using `kubectl rollout status`.  

---

### 🧩 **Summary**

| Workflow File | Purpose | Tools Used |
|----------------|----------|-------------|
| `infra.yml` | Infrastructure provisioning (AKS, Azure SQL, Key Vault) and post-deploy setup (NGINX Ingress, Prometheus,    Grafana, Loki) | Terraform • Azure CLI • Helm |
| `set k8s.yml` | AKS cluster configuration and app setup | Azure CLI • Terraform • kubectl • Helm |
| `backend.yml` | Backend CI/CD pipeline | Maven • Docker • Azure Key Vault • AKS |
| `frontend.yml` | Frontend CI/CD pipeline | Node.js • Vite • Docker • AKS |
 
---

## 🧪 **Validation & Testing**
After successful deployment ✅  
- 🌐 Access the Ingress URL to verify the frontend loads properly.  
- 🔄 Test backend APIs through Ingress to ensure connectivity.  
- 📈 Check **Grafana dashboards** for cluster metrics and HPA activity.  
- 📜 Review **Loki logs** for errors and performance monitoring.

---

## 👥 **Team Members**
- 💫 **Norah Alshahrani**  
- ⚡ **Ferhad Baaqeel**  
- 🧩 **Khalid Alabdali**  
- 🎨 **Thekra Alzahrani**  
- 🔍 **Ahmed Basuwaiteen**

