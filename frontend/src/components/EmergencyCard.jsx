import { useState, useEffect } from "react";
import {
    FaPhoneAlt,
    FaTint,
    FaBirthdayCake,
    FaHistory,
    FaCheckCircle,
    FaClock,
    FaMapMarkerAlt,
} from "react-icons/fa";

function EmergencyCard({ user, resolve }) {
    const [showHistory, setShowHistory] = useState(false);

    return (
        <>
            <div className="emergency-card">

                <div className="card-top">

                    <span className="emergency-badge">
                        EMERGENCY
                    </span>

                    <span className="time">
                        <FaClock /> Just Now
                    </span>

                </div>

                <div className="patient-avatar">
                    {user.fullName
                        ? user.fullName.charAt(0).toUpperCase()
                        : "P"}
                </div>

                <h3>{user.fullName}</h3>

                <div className="info">

                    <p>
                        <FaBirthdayCake />
                        <span>{user.age} Years</span>
                    </p>

                    <p>
                        <FaTint />
                        <span>{user.bloodGroup}</span>
                    </p>

                    <p>
                        <FaPhoneAlt />
                        <span>{user.phoneNumber}</span>
                    </p>

                    <p>
                        <FaMapMarkerAlt />
                        <span>
                            {user.latitude && user.longitude
                                ? "Location Available"
                                : "Location Unavailable"}
                        </span>
                    </p>

                    {/* <p>
                        ETA

                        <span
                            style={{
                                color: "#16a34a",
                                fontWeight: "bold",
                                marginLeft: "8px"
                            }}
                        >
                            {user.eta} min
                        </span>
                    </p> */}

                    {user.latitude && user.longitude && (
                        <a
                            href={`https://www.google.com/maps?q=${user.latitude},${user.longitude}`}
                            target="_blank"
                            rel="noreferrer"
                            className="location-btn"
                        >
                            <FaMapMarkerAlt />
                            View Live Location
                        </a>
                    )}

                </div>

                <div className="actions">

                    <button
                        className="history-btn"
                        onClick={() => setShowHistory(true)}
                    >
                        <FaHistory />
                        History
                    </button>

                    <button
                        className="resolve-btn"
                        onClick={() => resolve(user.uid)}
                    >
                        <FaCheckCircle />
                        Resolve
                    </button>

                </div>

            </div>

            {showHistory && (
                <div
                    className="history-overlay"
                    onClick={() => setShowHistory(false)}
                >
                    <div
                        className="history-modal"
                        onClick={(e) => e.stopPropagation()}
                    >

                        <h2>Patient Medical History</h2>

                        <hr />

                        <div className="history-details">

                            <p>
                                <strong>Name:</strong> {user.fullName}
                            </p>

                            <p>
                                <strong>Age:</strong> {user.age}
                            </p>

                            <p>
                                <strong>Blood Group:</strong> {user.bloodGroup}
                            </p>

                            <p>
                                <strong>Phone:</strong> {user.phoneNumber}
                            </p>

                            <p>
                                <strong>Medical History:</strong>
                            </p>

                            <div className="history-box">

                                {Array.isArray(user.medicalHistory) ? (
                                    user.medicalHistory.length > 0 ? (
                                        user.medicalHistory.map((record, index) => (
                                            <div
                                                key={index}
                                                className="history-item"
                                            >
                                                <h4>{record.disease}</h4>

                                                <p>
                                                    <strong>Doctor:</strong> {record.doctor}
                                                </p>

                                                <p>
                                                    <strong>Treatment:</strong> {record.treatment}
                                                </p>

                                                <p>
                                                    <strong>Date:</strong> {record.date}
                                                </p>

                                            </div>
                                        ))
                                    ) : (
                                        <p>No medical records.</p>
                                    )
                                ) : (
                                    <p>{user.medicalHistory}</p>
                                )}

                            </div>

                        </div>

                        <button
                            className="close-btn"
                            onClick={() => setShowHistory(false)}
                        >
                            Close
                        </button>

                    </div>
                </div>
            )}
        </>
    );
}

export default EmergencyCard;