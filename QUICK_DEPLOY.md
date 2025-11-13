# 🚀 Quick Deployment Guide

## Prerequisites
- GitHub account
- Railway account (https://railway.app)
- Vercel account (https://vercel.com)

---

## 📋 Step-by-Step Deployment (15 minutes)

### **Step 1: Push to GitHub** (2 min)
```bash
cd c:\project\Collaborative_editor
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/collaborative-editor.git
git push -u origin main
```

---

### **Step 2: Deploy Backend + Database on Railway** (5 min)

1. **Go to** https://railway.app and sign up
2. **New Project** → **Deploy PostgreSQL**
3. **Add Service** → **GitHub Repo** → Select your repository
4. **Settings**:
   - Root Directory: `Backend`
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/demo-0.0.1-SNAPSHOT.jar`

5. **Environment Variables** (Railway → Backend Service → Variables):
   ```
   SPRING_PROFILE=prod
   FRONTEND_URL=https://your-app.vercel.app  (will update later)
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-specific-password
   ```

6. **Connect Database**:
   - Railway auto-creates `DATABASE_URL`
   - Click "Connect" on PostgreSQL service
   - Variables are automatically injected

7. **Deploy** - Railway builds and deploys automatically

8. **Get Backend URL**:
   - Go to Settings → Generate Domain
   - Copy URL (e.g., `https://collaborative-editor-production.up.railway.app`)

9. **Initialize Database**:
   - Railway → PostgreSQL → Data tab
   - Run `schema-postgres.sql` queries

---

### **Step 3: Deploy Frontend on Vercel** (3 min)

1. **Go to** https://vercel.com and sign up
2. **New Project** → **Import Git Repository**
3. **Configure**:
   - Framework: Angular
   - Root Directory: `Frontend`
   - Build Command: `npm install && npm run build --configuration production`
   - Output Directory: `dist/Frontend/browser`

4. **Environment Variables**:
   ```
   NODE_ENV=production
   ```

5. **Deploy** - Vercel builds and deploys

6. **Get Frontend URL** (e.g., `https://collaborative-editor.vercel.app`)

---

### **Step 4: Update Configuration** (2 min)

**Frontend:**
1. Update `Frontend/src/environments/environment.prod.ts`:
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://YOUR-BACKEND.railway.app',
  wsUrl: 'wss://YOUR-BACKEND.railway.app/ws',
  codeExecutionUrl: 'https://YOUR-BACKEND.railway.app/api/execute'
};
```

2. Commit and push:
```bash
git add .
git commit -m "Update production URLs"
git push
```

**Backend:**
1. Railway → Backend Service → Variables
2. Update `FRONTEND_URL` with your Vercel URL
3. Redeploy backend

---

### **Step 5: Test** (3 min)

1. Open your Vercel URL
2. Register a new user
3. Create a room
4. Share room code with another browser/device
5. Test collaboration features:
   - ✅ Code synchronization
   - ✅ Cursor tracking
   - ✅ Chat
   - ✅ Terminal sharing
   - ✅ Voice communication

---

## 🔧 Troubleshooting

### Backend not starting?
- Check Railway logs
- Verify `SPRING_PROFILE=prod`
- Confirm PostgreSQL is connected

### Frontend can't connect?
- Check CORS settings in backend
- Verify API URLs in `environment.prod.ts`
- Check browser console for errors

### Database connection failed?
- Railway auto-configures `DATABASE_URL`
- Check PostgreSQL service is running
- Verify schema is created

### WebSocket not connecting?
- Use `wss://` (not `ws://`) for production
- Check CORS allows WebSocket connections
- Verify backend is using HTTPS

---

## 💡 Alternative: Local PostgreSQL Testing

Before deploying, test with local PostgreSQL:

1. **Install PostgreSQL** locally
2. **Create database**:
   ```sql
   CREATE DATABASE collaborative_editor;
   ```
3. **Run schema**: Execute `schema-postgres.sql`
4. **Update** `application-prod.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/collaborative_editor
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```
5. **Run**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

---

## 📊 Monitoring

**Railway:**
- Logs: Railway → Service → Logs
- Metrics: Railway → Service → Metrics
- Database: Railway → PostgreSQL → Data

**Vercel:**
- Analytics: Vercel → Project → Analytics
- Logs: Vercel → Project → Deployments → View Function Logs

---

## 🔒 Security Checklist

- [ ] Change default admin password
- [ ] Remove test users from data.sql
- [ ] Use strong database password
- [ ] Enable HTTPS on both services
- [ ] Set secure CORS origins
- [ ] Use environment variables for secrets
- [ ] Enable rate limiting
- [ ] Add input validation

---

## 💰 Cost

**Railway**: $5/month (includes PostgreSQL)
**Vercel**: Free tier sufficient

**Total**: $5/month 🎉

---

## 🆘 Need Help?

- Railway Docs: https://docs.railway.app
- Vercel Docs: https://vercel.com/docs
- Spring Boot: https://docs.spring.io/spring-boot
- Angular: https://angular.dev

---

## ✅ Deployment Complete!

Your collaborative code editor is now live! 🚀

Share your room codes and start collaborating in real-time!
