import { useEffect, useState } from "react";
import api from "../services/api";
import EmergencyCard from "../components/EmergencyCard";

function Dashboard() {

    const [requests, setRequests] = useState([]);


    const getRequests = async () => {

        try {

            const response = await api.get("/requests");

            setRequests(response.data);

        } 
        
        catch (error) {

            console.log(error);

        }

    };


    const resolveRequest = async (uid) => {

        try {

            await api.post(`/resolve/${uid}`);

            getRequests();

        } 
        
        catch (error) {

            console.log(error);

        }

    };


    useEffect(() => {

        getRequests();


        const interval = setInterval(() => {

            getRequests();

        }, 5000);


        return () => clearInterval(interval);


    }, []);



    return (

        <div className="dashboard">


            <header>

                <h1>
                    Sahara Hospital
                </h1>


                <h3>
                    Active Emergencies : {requests.length}
                </h3>


            </header>



            <div className="container">


                {
                    requests.length === 0 ?

                    <h2>
                        No Emergency Requests
                    </h2>


                    :

                    requests.map((user) => (

                        <EmergencyCard

                            key={user.uid}

                            user={user}

                            resolve={resolveRequest}

                        />

                    ))

                }


            </div>


        </div>

    );

}


export default Dashboard;