import { useEffect, useState } from "react";
import api from "../services/api";
import logo from "../assets/logo.png";
import EmergencyCard from "../components/EmergencyCard";
import {
    FaHospital,
    FaUsers,
    FaPhoneAlt,
    FaClock,
} from "react-icons/fa";

function Dashboard() {
    const [requests, setRequests] = useState([]);
    const [currentTime, setCurrentTime] = useState(new Date());

    const getRequests = async () => {
        try {
            const res = await api.get("/requests");

            console.log("API Response:", res.data);

            setRequests(res.data);
        } catch (err) {
            console.log(err);
        }
    };

    const resolveRequest = async (uid) => {
        try {
            await api.post(`/resolve/${uid}`);
            getRequests();
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        getRequests();

        const requestInterval = setInterval(() => {
            getRequests();
        }, 5000);

        const clockInterval = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => {
            clearInterval(requestInterval);
            clearInterval(clockInterval);
        };
    }, []);

    return (
        <div className="dashboard">

            {/* Header */}

            <header className="dashboard-header">

                <div className="hospital">

                    <div className="hospital-logo">
                        <img src={logo} alt="Sahara Hospital Logo" />
                    </div>

                    <div>
                        <h1>SAHARA HOSPITAL</h1>
                        <p>Smart Care, Always There.</p>
                    </div>

                </div>

                <div className="date-card">

                    <FaClock className="clock-icon" />

                    <div>

                        <h3>
                            {currentTime.toLocaleDateString("en-GB", {
                                day: "numeric",
                                month: "long",
                                year: "numeric",
                            })}
                        </h3>

                        <p>
                            {currentTime.toLocaleTimeString([], {
                                hour: "2-digit",
                                minute: "2-digit",
                                second: "2-digit",
                            })}
                        </p>

                    </div>

                </div>

            </header>

            {/* Welcome */}

            <section className="welcome">

                <h2>Hello, Help Desk 👋</h2>

                <p>
                    Monitor incoming emergency requests in real time.
                </p>

            </section>

            {/* Stats */}

            <section className="stats">

                <div className="stat-card emergency">

                    <h4>🚨 Active Emergencies</h4>

                    <h2>{requests.length}</h2>

                </div>

                <div className="stat-card">

                    <FaUsers />

                    <div>

                        <h4>Patients Waiting</h4>

                        <p>{requests.length}</p>

                    </div>

                </div>

                <div className="stat-card">

                    <FaPhoneAlt />

                    <div>

                        <h4>Calls Required</h4>

                        <p>{requests.length}</p>

                    </div>

                </div>

                <div className="stat-card">

                    <FaClock />

                    <div>

                        <h4>Last Sync</h4>

                        <p>
                            {currentTime.toLocaleTimeString([], {
                                hour: "2-digit",
                                minute: "2-digit",
                            })}
                        </p>

                    </div>

                </div>

            </section>

            {/* Section Title */}

            <div className="section-title">

                <h2>Emergency Requests</h2>

            </div>

            {/* Cards */}

            <div className="cards">

                {requests.length === 0 ? (

                    <div className="empty-card">

                        <h2>No Emergency Requests</h2>

                        <p>
                            All patients are currently safe.
                        </p>

                    </div>

                ) : (

                    requests.map((user) => (
                        <EmergencyCard
                            key={user.uid}
                            user={user}
                            resolve={resolveRequest}
                        />
                    ))

                )}

            </div>

        </div>
    );
}

export default Dashboard;