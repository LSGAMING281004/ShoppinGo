# Database Configuration Guide for Zoho Catalyst Deployment

## ⚠️ Important: Database Setup Required

Before deploying your ShoppinGo backend to Zoho Catalyst, you need to configure a **production MySQL database**. The current `app-config.json` has placeholder values that need to be updated.

## 🌟 Recommended: Clever Cloud (Since you have an account)

1.  **Log in** to your [Clever Cloud Console](https://console.clever-cloud.com/).
2.  Click **Create...** -> **an add-on**.
3.  Select **MySQL**.
4.  Choose your plan (e.g., **Dev** for free/testing).
5.  Proceed to create the add-on.
6.  Once created, clicking on the add-on will show the **Dashboard**.
7.  Look for the **Connection details** section to find:
    *   **Host** (e.g., `bw8...-mysql.services.clever-cloud.com`)
    *   **Database** (e.g., `bw8...`)
    *   **User**
    *   **Password**

---

## Other Options

### 🔹 Free/Low-Cost Options

1. **Railway.app**
   - $5/month shared MySQL
   - Easy setup with CLI
   - Sign up at: https://railway.app/

2. **db4free.net**
   - Free tier: 200MB storage
   - For development/testing only
   - Sign up at: https://www.db4free.net/

### 🔹 Production-Ready Options

1. **AWS RDS for MySQL**
   - Free tier: 750 hours/month for 12 months
   - Sign up at: https://aws.amazon.com/rds/

2. **Azure Database for MySQL**
   - Free tier available with Azure account
   - Sign up at: https://azure.microsoft.com/en-us/products/mysql/

3. **Google Cloud SQL for MySQL**
   - $300 free credit for 90 days
   - Sign up at: https://cloud.google.com/sql

4. **DigitalOcean Managed MySQL**
   - $15/month starter tier
   - Sign up at: https://www.digitalocean.com/products/managed-databases-mysql

## How to Configure Your Database

### Update app-config.json

Edit your `app-config.json` file and replace the placeholders:

```json
{
    "runtime": "java17",
    "build_command": "./mvnw.cmd clean package -DskipTests",
    "run_command": "java -Dserver.port=$PORT -jar target/backend-0.0.1-SNAPSHOT.jar",
    "stack": "ubuntu-22.04",
    "env_variables": {
        "PORT": "8080",
        "DB_URL": "jdbc:mysql://YOUR_CLEVER_CLOUD_HOST:3306/YOUR_DATABASE_NAME",
        "DB_USERNAME": "YOUR_USERNAME",
        "DB_PASSWORD": "YOUR_PASSWORD",
        "FIREBASE_CONFIG_PATH": "shoppingo-acb4f-firebase-adminsdk-fbsvc-6e012aca83.json",
        "SHOW_SQL": "false"
    }
}
```

**Replace:**
- `YOUR_CLEVER_CLOUD_HOST` → Copy from Clever Cloud (e.g. `xx.mysql.services.clever-cloud.com`)
- `YOUR_DATABASE_NAME` → Copy from Clever Cloud
- `YOUR_USERNAME` → Copy from Clever Cloud
- `YOUR_PASSWORD` → Copy from Clever Cloud

## Security Best Practices

✅ **DO:**
- Use strong passwords
- Restrict database access to specific IPs if possible (Clever Cloud handles this securely usually)
- Regularly backup your database

❌ **DON'T:**
- Commit `app-config.json` with real credentials to Git
- Share credentials publicly
