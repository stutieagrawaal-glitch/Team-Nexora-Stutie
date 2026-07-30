import {
  FaUser,
  FaPhoneAlt,
  FaTint,
  FaBirthdayCake,
  FaFileMedical,
  FaUserMd,
  FaCalendarAlt,
  FaNotesMedical,
} from "react-icons/fa";

function PatientHistory({ patient, onClose }) {
  if (!patient) return null;

  return (
    <div className="history-overlay">

      <div className="history-modal">

        <div className="history-header">

          <h2>
            <FaFileMedical /> Patient Medical History
          </h2>

          <button
            className="close-btn"
            onClick={onClose}
          >
            ✕
          </button>

        </div>

        {/* Patient Information */}

        <div className="patient-profile">

          <div className="profile-avatar">
            {patient.fullName
              ? patient.fullName.charAt(0).toUpperCase()
              : "P"}
          </div>

          <div>

            <h3>{patient.fullName}</h3>

            <p>Emergency Patient</p>

          </div>

        </div>

        <div className="patient-grid">

          <div className="info-box">

            <FaBirthdayCake />

            <div>

              <span>Age</span>

              <strong>{patient.age || "N/A"}</strong>

            </div>

          </div>

          <div className="info-box">

            <FaTint />

            <div>

              <span>Blood Group</span>

              <strong>{patient.bloodGroup || "N/A"}</strong>

            </div>

          </div>

          <div className="info-box">

            <FaPhoneAlt />

            <div>

              <span>Phone</span>

              <strong>{patient.phoneNumber || "N/A"}</strong>

            </div>

          </div>

          <div className="info-box">

            <FaUser />

            <div>

              <span>Gender</span>

              <strong>{patient.gender || "N/A"}</strong>

            </div>

          </div>

        </div>

        {/* Medical History */}

        <div className="records-section">

          <h3>
            <FaNotesMedical /> Hospital Records
          </h3>

          {/* If medicalHistory is an array */}

          {Array.isArray(patient.medicalHistory) &&
          patient.medicalHistory.length > 0 ? (

            patient.medicalHistory.map((record, index) => (

              <div
                className="record-card"
                key={index}
              >

                <h4>{record.disease}</h4>

                <p>

                  <FaUserMd />

                  Doctor :
                  {record.doctor}

                </p>

                <p>

                  <FaCalendarAlt />

                  {record.date}

                </p>

                <p>

                  Treatment :
                  {record.treatment}

                </p>

              </div>

            ))

          ) : typeof patient.medicalHistory === "string" ? (

            <div className="record-card">

              <p>{patient.medicalHistory}</p>

            </div>

          ) : (

            <div className="record-card">

              <p>No previous hospital records found.</p>

            </div>

          )}

        </div>

      </div>

    </div>
  );
}

export default PatientHistory;