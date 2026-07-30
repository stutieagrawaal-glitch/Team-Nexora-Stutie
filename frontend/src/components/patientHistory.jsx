import React from "react";

function PatientHistory({ patient }) {

  if (!patient) {
    return (
      <div className="patient-history">
        <h3>Patient History</h3>
        <p>No patient data available</p>
      </div>
    );
  }


  return (
    <div className="patient-history">

      <h2>Patient Medical History</h2>

      <div className="patient-info">

        <p>
          <strong>Name:</strong> {patient.fullName || "N/A"}
        </p>

        <p>
          <strong>Age:</strong> {patient.age || "N/A"}
        </p>

        <p>
          <strong>Gender:</strong> {patient.gender || "N/A"}
        </p>

        <p>
          <strong>Blood Group:</strong> {patient.bloodGroup || "N/A"}
        </p>

        <p>
          <strong>Phone:</strong> {patient.phoneNumber || "N/A"}
        </p>

        <p>
          <strong>Allergies:</strong> {patient.allergies || "None"}
        </p>

      </div>


      <h3>Previous Medical Records</h3>


      {
        patient.medicalHistory &&
        patient.medicalHistory.length > 0 ? (

          <div className="history-list">

            {
              patient.medicalHistory.map((record,index)=>(

                <div 
                  className="history-card"
                  key={index}
                >

                  <p>
                    <strong>Disease:</strong> 
                    {record.disease}
                  </p>

                  <p>
                    <strong>Doctor:</strong>
                    {record.doctor}
                  </p>

                  <p>
                    <strong>Date:</strong>
                    {record.date}
                  </p>

                  <p>
                    <strong>Treatment:</strong>
                    {record.treatment}
                  </p>


                </div>

              ))
            }

          </div>

        ) : (

          <p>
            No previous medical records found.
          </p>

        )
      }


    </div>
  );
}

export default PatientHistory;