import React, { useState, useEffect, useRef, useContext } from 'react';
import { AuthContext } from '../contexts/AuthContext';

const Community = () => {
    const {
        authState: {
          username
        }
      } = useContext(AuthContext);
   
    const [sentMessages, setSentMessages] = useState([]);
    const [input, setInput] = useState('');
    const [isTyping, setIsTyping] = useState(false);
    const [typingUsers, setTypingUsers] = useState([]);
    const typingTimeoutRef = useRef(null);

    const ws = useRef(null);

    useEffect(() => {
        ws.current = new WebSocket('ws://localhost:8080/community');

        ws.current.onopen = () => {
        };

        ws.current.onclose = () => {
        };

        ws.current.onmessage = (evt) => {
            const message = JSON.parse(evt.data);
            if (message.type === 'typing') {
                setTypingUsers(prevUsers => [...new Set([...prevUsers, message.username])]);
            } else if (message.type === 'stopTyping') {
                setTypingUsers(prevUsers => prevUsers.filter(user => user !== message.username));
            } else {
                setSentMessages(prevMessages => [...prevMessages, message]);
            }
            
        };

        return () => {
            ws.current.close();
        };
    }, []);

    const handleSendMessage = () => {
        if (ws.current && input) {
            const timestamp = new Date().toLocaleTimeString();
            const message = {
                username: username,
                text: input,
                time: timestamp
            };
            ws.current.send(JSON.stringify(message));
            setInput('');
        }
    };

    const handleTyping = () => {
        if (!isTyping) {
            setIsTyping(true);
            ws.current.send(JSON.stringify({ type: 'typing', username: username }));
        }

        clearTimeout(typingTimeoutRef.current);
        typingTimeoutRef.current = setTimeout(() => {
            setIsTyping(false);
            ws.current.send(JSON.stringify({ type: 'stopTyping', username: username }));
        }, 2000);
    };
    
    return (
        <>
            <h1>Community</h1>
            <div className="chat-container">
                <div className="sent-messages">
                    <h2>Sent Messages</h2>
                    {sentMessages.map((msg, index) => (
                        <div key={index} className="message-item">
                            {msg.username}: {msg.text} ({msg.time})
                        </div>
                    ))}
                </div>
                <div className="typing-indicator">
                    {typingUsers.length > 0 && (
                        <div>{typingUsers.join(', ')} is typing...</div>
                    )}
                </div>
                <div className="message-input">
                    <input 
                        type="text" 
                        value={input} 
                        onChange={(e) => { 
                            setInput(e.target.value);
                            handleTyping();
                        }} 
                    />
                    <button onClick={handleSendMessage}>Send</button>
                </div>
            </div>
        </>
    );
};

export default Community;
