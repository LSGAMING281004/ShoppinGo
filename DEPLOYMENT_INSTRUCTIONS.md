# Final Deployment Steps

I have successfully updated your `app-config.json` with the Clever Cloud database credentials.

Since the project is not initialized locally, you need to run the initialization command manually to link your folder to your Zoho Catalyst project.

## Steps to Run in Terminal

Please open your terminal in the `backend` directory and run:

### 1. Initialize Project
```powershell
catalyst init
```
**Interactive Selections:**
1.  Select your **Project** from the list.
2.  Select **AppSail** (use **Space** to select, **Enter** to confirm).
3.  When asked for **Stack**, select **Java 17**.
4.  For **Build Path**, keep default (`.`).
5.  For **Route**, keep default (`/`).

### 2. Deploy
Once initialization is complete, run:
```powershell
catalyst deploy
```

You will see your **Deployment URL** in the output once finished!
