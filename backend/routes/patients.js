const express = require("express");

const router = express.Router();

const { firestore } = require("../firebase");


router.get("/:uid", async(req,res)=>{

    try{

        const uid=req.params.uid;


        const patientRef =
        firestore.collection("users").doc(uid);


        const patient =
        await patientRef.get();



        if(!patient.exists){

            return res.status(404).json({
                message:"Patient not found"
            });

        }


        const data = patient.data();


        res.json({

            uid:uid,

            fullName:data.fullName,
            age:data.age,
            gender:data.gender,
            bloodGroup:data.bloodGroup,
            phoneNumber:data.phoneNumber,
            allergies:data.allergies,

            medicalHistory:
            data.medicalHistory || []

        });


    }
    catch(error){

        res.status(500).json({
            error:error.message
        });

    }

});


module.exports=router;