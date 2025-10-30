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

## 🌐 **Overview**
Welcome to our **final DevOps bootcamp project** 🎓  

In this challenge, our team re-architected and migrated the **Burger Builder App** to a **cloud-native Azure Kubernetes Service (AKS)** environment 🧩.  
The mission: build a **secure, scalable, automated** system with:
> 🪄 *Infrastructure as Code (Terraform), CI/CD with GitHub Actions, and full observability using Prometheus + Grafana + Loki.*

Key achievements:
- 🚀 Fully automated multi-tier deployment on Azure  
- 🔒 Security-by-default (Key Vault, Private Endpoints, RBAC)  
- 📊 Real-time metrics & dashboards for monitoring  

---

## 🧰 **Tech Stack**

| Layer | Technologies |
|:------|:--------------|
| 💻 **Frontend** | React 18 + TypeScript (Vite • React Router • Axios • Context API • CSS Modules) |
| ⚙️ **Backend** | Java 21 • Spring Boot • Maven |
| ☁️ **Infrastructure** | Azure Kubernetes Service (AKS) • Azure SQL • Terraform (IaC) |
| 🔁 **CI/CD** | GitHub Actions (Infra / Backend / Frontend Pipelines) |
| 🧷 **Security** | Azure Key Vault • Private Endpoints • RBAC • App Gateway (WAF v2) |
| 📈 **Monitoring** | Prometheus • Grafana • Loki • Azure Monitor & Log Analytics |

---

## 📁 **Project Structure**

The following structure shows the main directories and files for the **AKS Migration Project**.  
Each folder represents a major part of the DevOps workflow — app source code, Kubernetes manifests, Terraform IaC, and CI/CD automation.

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
    ├── aks/                       # AKS cluster configuration
    ├── privatelink/               # Private Endpoint setup
    ├── sql/                       # Azure SQL Database module
    ├── subnet/                    # Subnet configuration
    └── vnet/                      # Virtual Network module
```

---

## 🏗️ **Infrastructure Overview**
Our AKS migration adopts a **3-tier architecture** 🧱:

| Component | Description |
|------------|--------------|
| 🖥️ **AKS Cluster** | Hosts all containerized workloads (frontend + backend) |
| 🗄️ **Azure SQL Database** | Secure data layer with private endpoint |
| 🔒 **Azure Key Vault** | Secrets and credentials management |
| 🧩 **VNet + Subnets** | Isolated network environment |
| 📊 **Prometheus + Grafana + Loki** | Cluster metrics, dashboards & logs |

> Everything provisioned automatically via Terraform using remote state & OIDC authentication 🪄

---

## ⚙️ **Deployment Workflow**

The deployment of the **Burger Builder App – AKS Migration Project** is fully automated using **GitHub Actions**.  
The following pipelines ensure that each stage — infrastructure, backend, and frontend — is deployed consistently and securely to Azure.

---

### 1️⃣ Infrastructure Pipeline – `infra.yml`
- 🏗️ Provisions the entire cloud infrastructure using **Terraform**.  
- Authenticates to Azure via OIDC (Service Principal).  
- Runs `terraform init → plan → apply` to create:
  - Resource Group, Virtual Network (VNet), and Subnets  
  - Azure Kubernetes Service (AKS) Cluster  
  - Azure SQL Database (Private Endpoint)  
  - Azure Key Vault for secrets  
  - Log Analytics, Prometheus, Grafana, and Loki  
  - Application Gateway (WAF v2) for ingress routing  
- ✅ Uses **Remote Backend (Azure Storage)** for Terraform state.

---

### 2️⃣ Backend Deployment Pipeline – `backend.yml`
- 🧱 Builds and tests the **Spring Boot (Java 21)** backend.  
- Containerizes the app with **Docker** and pushes to **Azure Container Registry (ACR)**.  
- Deploys automatically to **AKS** using manifests under `k8s/backend/`.  
- Uses **ConfigMaps** and **Secrets** for environment variables.  
- Includes **readiness** and **liveness probes** for monitoring.  
- 🔁 Supports rolling updates and auto-restart for failed pods.

---

### 3️⃣ Frontend Deployment Pipeline – `frontend.yml`
- 💻 Builds and tests the **React + TypeScript (Vite)** frontend.  
- Creates a Docker image and pushes it to **ACR**.  
- Deploys to **AKS** using manifests under `k8s/frontend/`.  
- Routed publicly via **Application Gateway (WAF v2)** with HTTPS.  
- 🧩 Uses ConfigMaps for environment settings.

---

### ⚡ **Pipeline Order**
Workflows are triggered manually from the **GitHub Actions** tab in this sequence:
> **Infrastructure → Backend → Frontend**

This ensures the infrastructure is provisioned before deploying applications.

---

### 🧩 **Summary**

| Workflow File | Purpose | Tools Used |
|----------------|----------|-------------|
| `infra.yml` | Infrastructure provisioning | Terraform • Azure CLI |
| `backend.yml` | Backend CI/CD | Maven • Docker • AKS |
| `frontend.yml` | Frontend CI/CD | Node.js • Vite • AKS |

> ⚙️ These pipelines ensure consistent, repeatable, and secure deployments across all environments 🚀

---

## 🧪 **Validation & Testing**
After successful deployment ✅  
- 🌐 Access the Application Gateway URL to verify the frontend loads properly.  
- 🔄 Test backend APIs through the gateway to ensure connectivity.  
- 📈 Check **Grafana dashboards** for cluster metrics and HPA activity.  
- 📜 Review **Loki logs** for errors and performance monitoring.

---

## 👥 **Team Members**
- 💫 **Norah Alshahrani**  
- ⚡ **Ferhad Baaqeel**  
- 🧩 **Khalid Alabdali**  
- 🎨 **Thekra Alzahrani**  
- 🔍 **Ahmed Basuwaiteen**

