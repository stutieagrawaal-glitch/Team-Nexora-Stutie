const router = require("express").Router();
const { database, firestore } = require("../firebase");

router.get("/requests", async (req, res) => {
    try {
        const snapshot = await database.ref("users").get();

        const requests = snapshot.val();

        if (!requests) {
            return res.json([]);
        }

        const emergencyUsers = [];

        for (const uid in requests) {

            if (requests[uid].NeedHelp === true) {

                const doc = await firestore
                    .collection("users")
                    .doc(uid)
                    .get();

                if (doc.exists) {
                    emergencyUsers.push({
                        uid,
                        ...doc.data(),
                        timestamp: requests[uid].timestamp
                    });
                }
            }
        }

        res.json(emergencyUsers);

    } catch (error) {
        console.error(error);
        res.status(500).json({
            error: error.message
        });
    }
});

router.post("/resolve/:uid", async (req, res) => {
    try {

        await database.ref("users/" + req.params.uid).update({
            NeedHelp: false
        });

        res.json({
            success: true
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({
            error: error.message
        });
    }
});

module.exports = router;