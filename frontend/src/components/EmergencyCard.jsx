function EmergencyCard({ user, resolve }) {

    return (

        <div className="card">

            <div className="alert">
                EMERGENCY
            </div>


            <h2>
                Name: {user.fullName || user.name}
            </h2>


            <p>
                Phone: {user.phoneNumber || user.phone}
            </p>


            <p>
                Blood Group: {user.bloodGroup}
            </p>


            <p>
                Age: {user.age}
            </p>


            {
                user.address &&

                <p>
                    {user.address}
                </p>
            }


            <div className="buttons">


                <a
                    href={`tel:${user.phoneNumber || user.phone}`}
                    className="call"
                >
                    Call
                </a>



                <button

                    onClick={() => resolve(user.uid)}

                    className="resolve"

                >

                    Resolve

                </button>


            </div>


        </div>

    );

}


export default EmergencyCard;