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

## 📁 Project Structure

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
| 🌐 **Application Gateway (WAF v2)** | Ingress controller with TLS termination |
| 🧩 **VNet + Subnets** | Isolated network environment |
| 🐳 **Azure Container Registry (ACR)** | Stores Docker images |
| 📊 **Prometheus + Grafana + Loki** | Cluster metrics, dashboards & logs |

> Everything provisioned automatically via Terraform using remote state & OIDC authentication 🪄

---

## ⚙️ **Deployment Workflow**
Our CI/CD pipelines are powered by **GitHub Actions**, ensuring consistency and automation at every layer 🧩

### 🏗️ 1️⃣ Infrastructure Pipeline – `infra.yml`
- 🔑 Authenticates to Azure using OIDC  
- 🧱 Runs `terraform init → plan → apply`  
- ☁️ Creates RG, VNet, Subnets, AKS, SQL, Key Vault and Monitoring stack  

### 🧰 2️⃣ Backend Pipeline – `backend.yml`
- 🧪 Builds Spring Boot app → Docker image  
- 🐳 Pushes to Azure Container Registry  
- 🚀 Deploys to AKS via `k8s/backend` manifests  
- 🔐 Loads env vars from ConfigMaps/Secrets (never hardcoded)

### 💻 3️⃣ Frontend Pipeline – `frontend.yml`
- ⚡ Builds React (Vite) app → container → ACR  
- 🕸️ Deploys to AKS through Ingress controller  
- 🌍 Exposed securely via Application Gateway (WAF v2)

> ⚠️ **Workflow Order:**  
> 🏗️ Infra → 🧰 Backend → 💻 Frontend  

---

## 🧪 **Validation & Testing**
After successful deployment ✅  
- 🌐 Open the Application Gateway URL to verify frontend loads correctly  
- 🔄 Test API endpoints through the gateway (proxy to backend)  
- 📈 Check Grafana dashboards for pod metrics and HPA activity  
- 📜 Review Loki logs for errors or performance issues  

---

## 👥 **Team Members**
- 💫 **Norah Alshahrani**  
- ⚡ **Ferhad Baaqeel**  
- 🧩 **Khalid Alabdali**  
- 🎨 **Thekra Alzahrani**  
- 🔍 **Ahmed Basuwaiteen**


---

