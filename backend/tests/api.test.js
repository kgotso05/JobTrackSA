const request = require("supertest");
const app = require("../src/server");

describe("JobTrack SA API", () => {

    test("GET / should confirm API is running", async () => {

        const response = await request(app)
            .get("/");

        expect(response.statusCode).toBe(200);

        expect(response.body.success).toBe(true);

        expect(response.body.message)
            .toBe("JobTrack SA API is running");
    });

    test("GET /api/health should confirm database connection", async () => {

        const response = await request(app)
            .get("/api/health");

        expect(response.statusCode).toBe(200);

        expect(response.body.success).toBe(true);

        expect(response.body.message)
            .toBe("Database connection successful");
    });

    test("GET /api/applications without JWT should be rejected", async () => {

        const response = await request(app)
            .get("/api/applications");

        expect(response.statusCode).toBe(401);

        expect(response.body.success).toBe(false);

        expect(response.body.message)
            .toBe("Authentication token is required");
    });


    test("POST /api/auth/register should reject missing required fields", async () => {

        const response = await request(app)
            .post("/api/auth/register")
            .send({
                email: "invalid@test.com"
            });

        expect(response.statusCode).toBe(400);

        expect(response.body.success).toBe(false);
    });

    test("POST /api/auth/login should reject missing credentials", async () => {

        const response = await request(app)
            .post("/api/auth/login")
            .send({});

        expect(response.statusCode).toBe(400);

        expect(response.body.success).toBe(false);
    });

});