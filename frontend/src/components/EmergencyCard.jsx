function EmergencyCard({ user, resolve }) {

    return (
        <div className="card">

            <div className="alert">
                🚨 EMERGENCY
            </div>

            <h2>{user.name}</h2>

            <p>
                📞 {user.phone}
            </p>

            <p>
                🩸 Blood Group: {user.bloodGroup}
            </p>

            <p>
                🎂 Age: {user.age}
            </p>

            <p>
                📍 {user.address}
            </p>


            <div className="buttons">

                <a 
                href={`tel:${user.phone}`}
                className="call">
                    📞 Call
                </a>


                <button
                onClick={() => resolve(user.uid)}
                className="resolve">

                    ✔ Resolve

                </button>

            </div>

        </div>
    );
}

export default EmergencyCard;