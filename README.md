## SimpleLMS

[![Java CI with Maven](https://github.com/Prrost/SimpleLMS/actions/workflows/maven.yml/badge.svg?branch=master&event=push)](https://github.com/Prrost/SimpleLMS/actions/workflows/maven.yml)

### Quick Start

1. Run backend:

```bash
./mvnw spring-boot:run
```

2. Run frontend:

```bash
cd frontend
npm install
npm run dev
```

3. If needed, set backend URL in `frontend/.env`:

```env
VITE_API_BASE=http://localhost:8080
```