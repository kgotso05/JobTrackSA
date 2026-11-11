const express = require("express");

const app = express();

app.get("/", (req, res) => {
    res.send("Test server works");
});

app.listen(3000, () => {
    console.log("TEST SERVER RUNNING ON PORT 3000");
});