const { initializeApp, cert } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
const { getDatabase } = require("firebase-admin/database");

const serviceAccount = require("./serviceAccountKey.json");

const app = initializeApp({
  credential: cert(serviceAccount),
  databaseURL: "https://sahara-b7c52-default-rtdb.asia-southeast1.firebasedatabase.app"
});

module.exports = {
  app,
  firestore: getFirestore(app),
  database: getDatabase(app)
};