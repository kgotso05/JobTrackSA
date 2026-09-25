const crypto = require("crypto");
const pool = require("../config/database");

// CREATE APPLICATION
const createApplication = async (req, res) => {
    try {
        const userId = req.user.userId;

        const {
            companyName,
            jobTitle,
            location,
            sourceUrl,
            status,
            closingDate,
            dateApplied,
            notes
        } = req.body;

        if (!companyName || !jobTitle) {
            return res.status(400).json({
                success: false,
                message: "Company name and job title are required"
            });
        }

        const applicationId = crypto.randomUUID();

        const result = await pool.query(
            `INSERT INTO applications (
                application_id,
                user_id,
                company_name,
                job_title,
                location,
                source_url,
                status,
                closing_date,
                date_applied,
                notes
            )
            VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
            RETURNING *`,
            [
                applicationId,
                userId,
                companyName.trim(),
                jobTitle.trim(),
                location || null,
                sourceUrl || null,
                status || "Saved",
                closingDate || null,
                dateApplied || null,
                notes || null
            ]
        );

        return res.status(201).json({
            success: true,
            message: "Application created successfully",
            application: result.rows[0]
        });

    } catch (error) {
        console.error("Create application error:", error.message);

        return res.status(500).json({
            success: false,
            message: "Unable to create application"
        });
    }
};


// GET ALL APPLICATIONS FOR LOGGED-IN USER
const getApplications = async (req, res) => {
    try {
        const userId = req.user.userId;

        const result = await pool.query(
            `SELECT *
             FROM applications
             WHERE user_id = $1
             ORDER BY created_at DESC`,
            [userId]
        );

        return res.status(200).json({
            success: true,
            applications: result.rows
        });

    } catch (error) {
        console.error("Get applications error:", error.message);

        return res.status(500).json({
            success: false,
            message: "Unable to retrieve applications"
        });
    }
};


// GET ONE APPLICATION
const getApplicationById = async (req, res) => {
    try {
        const userId = req.user.userId;
        const applicationId = req.params.id;

        const result = await pool.query(
            `SELECT *
             FROM applications
             WHERE application_id = $1
             AND user_id = $2`,
            [applicationId, userId]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Application not found"
            });
        }

        return res.status(200).json({
            success: true,
            application: result.rows[0]
        });

    } catch (error) {
        console.error("Get application error:", error.message);

        return res.status(500).json({
            success: false,
            message: "Unable to retrieve application"
        });
    }
};


// UPDATE APPLICATION
const updateApplication = async (req, res) => {
    try {
        const userId = req.user.userId;
        const applicationId = req.params.id;

        const {
            companyName,
            jobTitle,
            location,
            sourceUrl,
            status,
            closingDate,
            dateApplied,
            notes
        } = req.body;

        if (!companyName || !jobTitle) {
            return res.status(400).json({
                success: false,
                message: "Company name and job title are required"
            });
        }

        const result = await pool.query(
            `UPDATE applications
             SET company_name = $1,
                 job_title = $2,
                 location = $3,
                 source_url = $4,
                 status = $5,
                 closing_date = $6,
                 date_applied = $7,
                 notes = $8,
                 updated_at = CURRENT_TIMESTAMP
             WHERE application_id = $9
             AND user_id = $10
             RETURNING *`,
            [
                companyName.trim(),
                jobTitle.trim(),
                location || null,
                sourceUrl || null,
                status || "Saved",
                closingDate || null,
                dateApplied || null,
                notes || null,
                applicationId,
                userId
            ]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Application not found"
            });
        }

        return res.status(200).json({
            success: true,
            message: "Application updated successfully",
            application: result.rows[0]
        });

    } catch (error) {
        console.error("Update application error:", error.message);

        return res.status(500).json({
            success: false,
            message: "Unable to update application"
        });
    }
};


// DELETE APPLICATION
const deleteApplication = async (req, res) => {
    try {
        const userId = req.user.userId;
        const applicationId = req.params.id;

        const result = await pool.query(
            `DELETE FROM applications
             WHERE application_id = $1
             AND user_id = $2
             RETURNING application_id`,
            [applicationId, userId]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({
                success: false,
                message: "Application not found"
            });
        }

        return res.status(200).json({
            success: true,
            message: "Application deleted successfully"
        });

    } catch (error) {
        console.error("Delete application error:", error.message);

        return res.status(500).json({
            success: false,
            message: "Unable to delete application"
        });
    }
};


module.exports = {
    createApplication,
    getApplications,
    getApplicationById,
    updateApplication,
    deleteApplication
};