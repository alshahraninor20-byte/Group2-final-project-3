## 🧱 Project Structure

```
Group2-final-project-3/
│
├── .github/
│   └── workflows/
│       ├── backend-pipeline.yml          # CI/CD for backend
│       ├── frontend-pipeline.yml         # CI/CD for frontend
│       ├── k8s-pipeline.yml              # CI/CD for Kubernetes
│       └── infra-pipeline.yml            # CI/CD for infrastructure
│
├── app/
│   ├── frontend/                         # React + TypeScript (Vite)
│   │   ├── src/
│   │   ├── public/
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── Dockerfile
│   │
│   └── backend/                          # Spring Boot (Java)
│       ├── src/
│       ├── pom.xml
│       └── Dockerfile
│
├── k8s/                                  # Kubernetes manifests
│   ├── backend-deployment.yaml           # Backend placeholder
│   └── frontend-deployment.ya
