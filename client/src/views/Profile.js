import React, { useContext, useState } from 'react';
import { AuthContext } from '../contexts/AuthContext';
import Button from 'react-bootstrap/Button'
import editIcon from '../assets/pencil.svg'
import EditProfileModal from '../components/modals/EditProfileModal';
import { PostsContext } from '../contexts/PostsContext';

const Profile = () => {
    const { authState } = useContext(AuthContext);
    const { setShowEditProfileModal } = useContext(PostsContext);
    const { username, email, firstname, lastname, gender } = authState;

    const [profileFirstname, setProfileFirstname] = useState(firstname);
    const [profileLastname, setProfileLastname] = useState(lastname);
    const [profileGender, setProfileGender] = useState(gender);

    const handleShowEditPostModal = () => {
        setShowEditProfileModal(true);
        setProfileFirstname(firstname);
        setProfileLastname(lastname);
        setProfileGender(gender);
    }

    return (
        <div>
            <h1>Profile Page</h1>
            <div>
                <p>Welcome, {username}!</p>
                <h4>Email: {email}</h4>
                <h4>Firstname: {firstname}</h4>
                <h4>Lastname: {lastname}</h4>
                <h4>Gender: {gender}</h4>
                <Button className='post-button' onClick={handleShowEditPostModal}>
                    <img src={editIcon} alt='edit' width='24' height='24' />
                    <span className='edit-profile-text'>Edit profile</span>
                </Button>
            </div>
            {/* Open edit profile modal */}
            <EditProfileModal 
                profileFirstname={profileFirstname} 
                profileLastname={profileLastname} 
                profileGender={profileGender} 
            />
        </div>
    );
};

export default Profile;
