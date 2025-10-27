Group2-final-project-3/
│
├── .github/
│   └── workflows/
│       ├── backend-pipeline.yml
│       ├── frontend-pipeline.yml
│       ├── k8s-pipeline.yml
│       └── infra-pipeline.yml
│
├── app/
│   ├── frontend/
│   │   ├── src/
│   │   ├── public/
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── Dockerfile
│   │
│   └── backend/
│       ├── src/
│       ├── pom.xml
│       └── Dockerfile
│
├── k8s/
│   ├── backend-deployment.yaml     # Backend placeholder
│   └── frontend-deployment.yaml    # Frontend placeholder
│
├── terraform/
│   ├── main.tf                     # Terraform main configuration file
│   └── variables.tf                # Terraform variables file
│
├── docs/
│   ├── architecture-diagram.png
│   └── README.md
│
├── env/
│   ├── environment.env
│   └── environment.env.example
│
└── README.md


backend/
│
├── src/                         # Spring Boot application source code
├── pom.xml
├── Dockerfile                   # Backend Docker build image
└── README.md
