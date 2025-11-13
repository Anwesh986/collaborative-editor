# 🆓 FREE DEPLOYMENT GUIDE (Render + Vercel)

## Total Cost: $0/month ✅

---

## Step 1: Push to GitHub (2 min)

```bash
cd c:\project\Collaborative_editor
git init
git add .
git commit -m "Initial commit - Ready for deployment"
git branch -M main

# Create repo on GitHub first, then:
git remote add origin https://github.com/YOUR_USERNAME/collaborative-editor.git
git push -u origin main
```

---

## Step 2: Deploy Backend on Render (5 min)

1. **Sign up**: https://render.com (free account)

2. **New Web Service**:
   - Click "New +" → "Web Service"
   - "Build and deploy from a Git repository"
   - Connect GitHub account
   - Select your repository
   
3. **Configuration**:
   ```
   Name: collaborative-editor-backend
   Region: Select closest to you
   Branch: main
   Root Directory: Backend
   Runtime: Docker
   
   Build Command:
   mvn clean package -DskipTests
   
   Start Command:
   java -Dserver.port=$PORT -jar target/demo-0.0.1-SNAPSHOT.jar
   
   Plan: FREE ✅
   ```

4. **Environment Variables** (click "Advanced" → "Add Environment Variable"):
   ```
   SPRING_PROFILE=prod
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-gmail-app-password
   FRONTEND_URL=https://your-app.vercel.app
   ```
   
   *Note: Leave FRONTEND_URL blank for now, we'll update it after deploying frontend*

5. **Click "Create Web Service"**
   - Wait 5-10 minutes for first deploy
   - Copy your backend URL (e.g., `https://collaborative-editor-backend.onrender.com`)

---

## Step 3: Create PostgreSQL Database on Render (3 min)

1. **New PostgreSQL**:
   - Dashboard → "New +" → "PostgreSQL"
   - Name: `collaborative-editor-db`
   - Database: `collaborative_editor`
   - User: `postgres`
   - Region: Same as backend
   - Plan: **FREE** ✅

2. **Get Connection Details**:
   - Click on database name
   - Copy "Internal Database URL"
   - Example: `postgresql://user:pass@dpg-xxx.oregon-postgres.render.com/dbname`

3. **Add to Backend Environment**:
   - Go to backend service → Environment
   - Add variable:
     ```
     DATABASE_URL=<paste-internal-url-here>
     ```
   - Backend will auto-redeploy

4. **Initialize Database**:
   - In Render dashboard → Your database → "Connect" (top right)
   - Select "External Connection"
   - Use a PostgreSQL client (like DBeaver, pgAdmin, or online at https://sqliteonline.com)
   - Connect and run the SQL from `Backend/src/main/resources/schema-postgres.sql`
   
   *Or use Render's built-in shell:*
   - Database dashboard → "Shell" tab
   - Paste and run schema-postgres.sql contents

---

## Step 4: Deploy Frontend on Vercel (3 min)

1. **Sign up**: https://vercel.com (free account)

2. **New Project**:
   - Click "Add New..." → "Project"
   - "Import Git Repository"
   - Select your GitHub repo
   - Click "Import"

3. **Configuration**:
   ```
   Framework Preset: Angular
   Root Directory: Frontend
   
   Build Command:
   npm install && npm run build --configuration production
   
   Output Directory:
   dist/Frontend/browser
   
   Install Command:
   npm install
   ```

4. **Environment Variables** (optional):
   ```
   NODE_ENV=production
   ```

5. **Click "Deploy"**
   - Wait 2-3 minutes
   - Copy your frontend URL (e.g., `https://collaborative-editor.vercel.app`)

---

## Step 5: Connect Frontend & Backend (2 min)

**Update Frontend:**
1. Edit `Frontend/src/environments/environment.prod.ts`:
   ```typescript
   export const environment = {
     production: true,
     apiUrl: 'https://your-backend.onrender.com',
     wsUrl: 'wss://your-backend.onrender.com/ws',
     codeExecutionUrl: 'https://your-backend.onrender.com/api/execute'
   };
   ```

2. Commit and push:
   ```bash
   git add .
   git commit -m "Update production URLs"
   git push
   ```
   - Vercel auto-deploys on push ✅

**Update Backend:**
1. Render dashboard → Backend service → Environment
2. Update `FRONTEND_URL`:
   ```
   FRONTEND_URL=https://your-app.vercel.app
   ```
3. Save (auto-redeploys) ✅

---

## Step 6: Test Your Deployment (2 min)

1. Open your Vercel URL: `https://your-app.vercel.app`
2. Register a new user
3. Create a room
4. Copy room code
5. Open incognito window
6. Join with code
7. Test features:
   - ✅ Code sync
   - ✅ Cursor tracking
   - ✅ Chat
   - ✅ Terminal sharing
   - ✅ Voice chat

---

## ⚡ Performance Tips (Keep It Fast)

### Problem: Render Free Tier Sleeps After 15 Min
**Solution: Keep It Awake**

**Option 1: UptimeRobot (Recommended)**
1. Sign up: https://uptimerobot.com (free)
2. Add monitor:
   - Type: HTTP(s)
   - URL: `https://your-backend.onrender.com/api/auth/test`
   - Interval: 5 minutes
3. Done! Your backend stays awake ✅

**Option 2: Cron-job.org**
1. Sign up: https://cron-job.org (free)
2. Create job:
   - URL: `https://your-backend.onrender.com/api/auth/test`
   - Interval: */10 * * * * (every 10 minutes)

**Option 3: GitHub Actions** (Free Pings)
Add `.github/workflows/keep-alive.yml`:
```yaml
name: Keep Render Alive
on:
  schedule:
    - cron: '*/10 * * * *'  # Every 10 minutes
jobs:
  ping:
    runs-on: ubuntu-latest
    steps:
      - run: curl https://your-backend.onrender.com/api/auth/test
```

---

## 🔒 Security Checklist

Before going live:
- [ ] Change default admin password in database
- [ ] Use Gmail App-Specific Password (not your real password)
- [ ] Review CORS settings
- [ ] Enable HTTPS (automatic on Render & Vercel)
- [ ] Remove test users from production database
- [ ] Set strong database password

---

## 📊 Free Tier Limits

**Render Free:**
- ✅ 750 hours/month (31 days = 744 hours - perfect!)
- ✅ Sleeps after 15 min inactivity (30s cold start)
- ✅ 512 MB RAM
- ✅ PostgreSQL: 1GB storage, expires after 90 days

**Vercel Free:**
- ✅ Unlimited bandwidth
- ✅ 100GB per month
- ✅ Automatic HTTPS
- ✅ No sleep/cold starts

---

## 🆘 Troubleshooting

### Backend not starting?
- Check logs: Render Dashboard → Service → Logs
- Verify `SPRING_PROFILE=prod`
- Confirm `DATABASE_URL` is set

### Frontend can't reach backend?
- Check CORS in backend `application-prod.properties`
- Verify API URLs in `environment.prod.ts`
- Check browser console for errors

### Database connection failed?
- Use "Internal Database URL" from Render (not external)
- Verify schema-postgres.sql was run
- Check database is in same region as backend

### "This site can't be reached" on first load?
- Cold start (30s) - completely normal on free tier
- Refresh page after 30 seconds
- Consider using UptimeRobot to keep it warm

---

## 🎉 Success!

Your app is now LIVE and costs $0/month!

**Share your URLs:**
- Frontend: `https://your-app.vercel.app`
- Backend: `https://your-backend.onrender.com`

Share room codes and start collaborating! 🚀

---

## 📈 Future Scaling

When you outgrow free tier:
1. **Render**: Upgrade to $7/month (no cold starts)
2. **Database**: Upgrade to $7/month (persistent)
3. **Total**: $14/month for production-ready

Or switch to Railway ($5/month for both).

---

## 💡 Pro Tips

1. **Monitor uptime**: Set up UptimeRobot to track availability
2. **Check logs**: Render has excellent logging
3. **Backup database**: Export PostgreSQL regularly (Tools → Backup)
4. **Watch limits**: Render dashboard shows usage
5. **Update regularly**: `git push` triggers auto-deploy

---

You're all set! 🎊
