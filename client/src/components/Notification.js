import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify'; 

const Notifications = ({ userId }) => {  
    const [socket, setSocket] = useState(null);

    useEffect(() => {

        const newSocket = new WebSocket(`ws://localhost:8080/notifications?userId=${userId}`);

        newSocket.onopen = () => {
            console.log("Connected to the WebSocket server");
        };

        newSocket.onmessage = (event) => {
            console.log("Received notification:", event.data);
            const message = event.data;
            toast.info(message);  
        };

        newSocket.onclose = () => {
            console.log("Disconnected from the WebSocket server");
        };

        newSocket.onerror = (error) => {
            console.log("WebSocket error: ", error);
        };

        setSocket(newSocket);

        return () => {
            if (newSocket) {
                newSocket.close();
            }
        };
    }, [userId]);

    return null;
};

export default Notifications;
