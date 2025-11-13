# Deployment Guide

## 🚀 Deployment Options

### Option 1: Railway (Recommended - Easiest)
**Backend + Database in one platform**

1. **Sign up**: https://railway.app
2. **Create New Project** → Select "Deploy PostgreSQL"
3. **Add Service** → "GitHub Repo" → Connect your repository
4. **Environment Variables**:
   ```
   SPRING_PROFILE=prod
   DATABASE_URL=postgresql://...  (auto-populated by Railway)
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=...  (auto-populated)
   FRONTEND_URL=https://your-frontend-url.vercel.app
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   ```
5. **Deploy** - Railway will build and deploy automatically
6. **Copy the backend URL** (e.g., https://your-app.railway.app)

---

### Option 2: Render
**Free tier available**

**Backend:**
1. Go to https://render.com
2. **New** → **Web Service**
3. Connect GitHub repository
4. Settings:
   - Build Command: `cd Backend && mvn clean package -DskipTests`
   - Start Command: `cd Backend && java -jar target/demo-0.0.1-SNAPSHOT.jar`
   - Environment: `SPRING_PROFILE=prod`
5. Add environment variables (same as Railway)

**Database:**
1. **New** → **PostgreSQL**
2. Copy connection details to backend environment variables

---

### Option 3: AWS (Most Scalable)

**Backend (Elastic Beanstalk):**
1. Package: `mvn clean package`
2. Upload JAR to Elastic Beanstalk
3. Configure environment variables

**Database (RDS):**
1. Create PostgreSQL RDS instance
2. Update connection string in environment

**Frontend (S3 + CloudFront):**
1. Build: `ng build --configuration production`
2. Upload `dist/` to S3 bucket
3. Configure CloudFront for CDN

---

### Option 4: Heroku
**Note: Heroku no longer has free tier**

1. Install Heroku CLI
2. `heroku create your-app-name`
3. `heroku addons:create heroku-postgresql:mini`
4. `git push heroku main`

---

## 🌐 Frontend Deployment

### Vercel (Recommended)
1. Go to https://vercel.com
2. **Import Project** → Connect GitHub
3. Framework: Angular
4. Build Command: `cd Frontend && npm install && npm run build`
5. Output Directory: `Frontend/dist/Frontend/browser`
6. Environment Variables:
   ```
   NODE_ENV=production
   ```
7. **Deploy**
8. Update `environment.prod.ts` with backend URL

### Netlify
1. Go to https://netlify.com
2. **Add new site** → Import from Git
3. Build settings:
   - Base directory: `Frontend`
   - Build command: `npm install && npm run build`
   - Publish directory: `dist/Frontend/browser`

---

## 📝 Pre-Deployment Checklist

### Backend:
- [ ] Update CORS allowed origins to frontend URL
- [ ] Set up production email credentials
- [ ] Configure PostgreSQL connection
- [ ] Set `SPRING_PROFILE=prod`
- [ ] Secure WebSocket endpoints
- [ ] Enable HTTPS

### Frontend:
- [ ] Create `environment.prod.ts` with production API URL
- [ ] Update API base URLs
- [ ] Enable production mode
- [ ] Configure CDN for assets

### Database:
- [ ] Run schema.sql on PostgreSQL
- [ ] Create admin user
- [ ] Set up backups
- [ ] Configure connection pooling

---

## 🔒 Security Considerations

1. **Remove test credentials** from data.sql
2. **Use environment variables** for all secrets
3. **Enable HTTPS** on both frontend and backend
4. **Set secure cookies** for sessions
5. **Implement rate limiting**
6. **Add authentication tokens** (JWT)
7. **Validate all inputs**

---

## 🛠️ Environment Variables Needed

### Backend (.env or platform settings):
```bash
SPRING_PROFILE=prod
DATABASE_URL=postgresql://host:5432/dbname
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-password
FRONTEND_URL=https://your-frontend.vercel.app
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### Frontend (environment.prod.ts):
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://your-backend.railway.app',
  wsUrl: 'wss://your-backend.railway.app/ws'
};
```

---

## 💰 Cost Estimates

| Platform | Backend | Database | Frontend | Total/Month |
|----------|---------|----------|----------|-------------|
| Railway | $5 | Included | - | $5 |
| Render | Free | $7 | - | $7 |
| Vercel/Netlify | - | - | Free | $0 |
| AWS | $10 | $15 | $1 | $26 |

**Recommended Stack**: Railway (Backend + DB) + Vercel (Frontend) = **$5/month**

---

## 🚀 Quick Deploy (Railway + Vercel)

1. **Push to GitHub**
2. **Railway**: Deploy backend + PostgreSQL (5 min)
3. **Vercel**: Deploy frontend (2 min)
4. **Update** environment variables
5. **Done!** ✅
