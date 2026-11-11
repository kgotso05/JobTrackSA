
const getFirebaseAuth = () => {
    const {
        getApps,
        initializeApp,
        cert
    } = require("firebase-admin/app");

    const { getAuth } = require("firebase-admin/auth");
    let app;

    const existingApps = getApps();

    if (existingApps.length > 0) {
        app = existingApps[0];
    } else {
        const {
            FIREBASE_PROJECT_ID,
            FIREBASE_CLIENT_EMAIL,
            FIREBASE_PRIVATE_KEY
        } = process.env;

        if (
            !FIREBASE_PROJECT_ID ||
            !FIREBASE_CLIENT_EMAIL ||
            !FIREBASE_PRIVATE_KEY
        ) {
            throw new Error(
                "Firebase Admin environment variables are missing"
            );
        }

        app = initializeApp({
            credential: cert({
                projectId: FIREBASE_PROJECT_ID,
                clientEmail: FIREBASE_CLIENT_EMAIL,
                privateKey: FIREBASE_PRIVATE_KEY.replace(/\\n/g, "\n")
            })
        });
    }

    return getAuth(app);
};

module.exports = { getFirebaseAuth };
