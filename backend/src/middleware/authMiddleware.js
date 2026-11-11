const jwt = require("jsonwebtoken");

const authenticateToken = (req, res, next) => {
    try {
        const secret = process.env.JWT_SECRET;

        if (!secret || secret.length < 32) {
            console.error("JWT_SECRET is missing or too weak");

            return res.status(500).json({
                success: false,
                message: "Authentication service is not configured"
            });
        }

        const authHeader = req.headers.authorization;

        if (!authHeader || !authHeader.startsWith("Bearer ")) {
            return res.status(401).json({
                success: false,
                message: "Authentication token is required"
            });
        }

        const token = authHeader.slice(7).trim();

        if (!token) {
            return res.status(401).json({
                success: false,
                message: "Authentication token is required"
            });
        }

        const decoded = jwt.verify(token, secret, {
            algorithms: ["HS256"]
        });

        if (!decoded.userId) {
            return res.status(401).json({
                success: false,
                message: "Invalid authentication token"
            });
        }

        req.user = decoded;

        next();

    } catch (error) {
        return res.status(401).json({
            success: false,
            message: "Invalid or expired authentication token"
        });
    }
};

module.exports = authenticateToken;
