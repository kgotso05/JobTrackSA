const express = require("express");

const authenticateToken =
    require("../middleware/authMiddleware");

const {
    createApplication,
    getApplications,
    getApplicationById,
    updateApplication,
    deleteApplication
} = require("../controllers/applicationController");

const router = express.Router();

// All application routes require authentication
router.use(authenticateToken);

router.post("/", createApplication);

router.get("/", getApplications);

router.get("/:id", getApplicationById);

router.put("/:id", updateApplication);

router.delete("/:id", deleteApplication);

module.exports = router;