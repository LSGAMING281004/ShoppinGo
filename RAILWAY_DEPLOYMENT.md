# ShoppinGo Backend - Railway Deployment Guide

Follow these steps to deploy your Spring Boot backend on Railway.

## 1. Prepare your GitHub Repository
Ensure your latest code (with the `FirebaseConfig` updates) is pushed to your GitHub repository: `https://github.com/LSGAMING281004/ShoppinGo.git`.

## 2. Connect GitHub to Railway
1. Go to [Railway.app](https://railway.app/) and log in.
2. Click **+ New Project**.
3. Select **Deploy from GitHub repo**.
4. Choose the `ShoppinGo` repository.
5. In the "Project Settings", set the **Root Directory** to `backend`.

## 3. Add and LINK a MySQL Database
1. In your Railway project dashboard, click **+ New**.
2. Select **Database** -> **Add MySQL**.
3. **CRITICAL STEP (Linking):**
   - Click and drag the **MySQL service** onto the **Backend service** in the Railway canvas.
   - Or, go to the **Backend service** -> **Settings** -> **Services** -> **Connect Service** and select MySQL.
   - This "links" them so Railway automatically gives the backend the database credentials.

## 4. Configure Environment Variables
Go to your **backend service** settings in Railway, click the **Variables** tab. 

If linked correctly, Railway auto-injects the database variables. You ONLY need to add:

| Variable Name | Value |
| :--- | :--- |
| `FIREBASE_CONFIG_JSON` | **Paste the ENTIRE content of your Firebase JSON file here.** |
| `PORT` | `8080` (Railway usually handles this automatically) |
| `SHOW_SQL` | `true` (Optional, for debugging) |

> [!TIP]
> To get the content for `FIREBASE_CONFIG_JSON`, open your `shoppingo-acb4f-firebase-adminsdk-fbsvc-6e012aca83.json` file, copy everything, and paste it exactly as a single value.

## 5. Deploy
1. Railway will automatically trigger a build when you save the variables.
2. Watch the **Deployments** tab for logs.
3. Once finished, Railway will provide a public URL (e.g., `https://backend-production.up.railway.app`).

## 6. Update Frontend (Coming Soon)
Once the backend is live, you'll need to update your Frontend code to point to this new Railway URL instead of `localhost:8080`.
