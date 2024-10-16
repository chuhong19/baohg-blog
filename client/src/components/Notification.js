import React, { useEffect, useState } from 'react';
import CustomToast from './CustomToast';

const Notifications = ({ userId }) => {  
    const [socket, setSocket] = useState(null);
    const [toasts, setToasts] = useState([]); 

    useEffect(() => {

        const newSocket = new WebSocket(`ws://localhost:8080/notifications?userId=${userId}`);

        newSocket.onopen = () => {
            console.log("Connected to the WebSocket server from Notification.js");
        };

        newSocket.onmessage = (event) => {
            console.log("Received notification:", event.data);
            const message = event.data;
            showToast(message);
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

    const showToast = (message) => {
        const id = Math.random().toString(36).substr(2, 9);  // Tạo ID ngẫu nhiên
        setToasts([...toasts, { id, message }]);

        // Xóa thông báo sau 5 giây
        setTimeout(() => {
            setToasts((prevToasts) => prevToasts.filter((toast) => toast.id !== id));
        }, 5000);
    };

    return (
        <div>
            {/* Hiển thị tất cả các toast */}
            {toasts.map((toast) => (
                <CustomToast
                    key={toast.id}
                    message={toast.message}
                    duration={5000}
                    onClose={() => setToasts(toasts.filter(t => t.id !== toast.id))}
                />
            ))}
        </div>
    );
};

export default Notifications;
