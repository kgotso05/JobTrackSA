require("dotenv").config();
const authRoutes = require("./routes/authRoutes");


const express = require("express");
const cors = require("cors");
const pool = require("./config/database");

const app = express();

const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json());
app.use("/api/auth", authRoutes);

// Test API route
app.get("/", (req, res) => {
    res.status(200).json({
        success: true,
        message: "JobTrack SA API is running"
    });
});

// Database health-check route
app.get("/api/health", async (req, res) => {
    try {
        const result = await pool.query("SELECT NOW()");

        res.status(200).json({
            success: true,
            message: "Database connection successful",
            databaseTime: result.rows[0].now
        });
    } catch (error) {
        console.error("Database health check failed:", error.message);

        res.status(500).json({
            success: false,
            message: "Database connection failed"
        });
    }
});

// Start server
app.listen(PORT, () => {
    console.log(`JobTrack SA API running on port ${PORT}`);
});