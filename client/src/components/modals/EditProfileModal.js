import React, { useState, useContext, useEffect } from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { PostsContext } from '../../contexts/PostsContext';
import axios from 'axios';
import { apiUrl } from '../../contexts/constants';
import { AuthContext } from '../../contexts/AuthContext';
import AlertMessage from '../layout/AlertMessage';

const EditProfileModal = ({ profileFirstname, profileLastname, profileGender }) => {

    const {
        showEditProfileModal,
        setShowEditProfileModal
    } = useContext(PostsContext);

    const {
        authState,
        updateUser
    } = useContext(AuthContext);

    const { userId } = authState;

    const [alert, setAlert] = useState(null);

    const [firstname, setFirstname] = useState(profileFirstname || '');
    const [lastname, setLastname] = useState(profileLastname || '');
    const [gender, setGender] = useState(profileGender || '');

    useEffect(() => {
        setFirstname(profileFirstname || '');
        setLastname(profileLastname || '');
        setGender(profileGender || '');
    }, [profileFirstname, profileLastname, profileGender]);

    const closeDialog = () => {
        setShowEditProfileModal(false);
    }
    
    const handleEditProfile = async () => {
        try {
            const response = await axios.post(`${apiUrl}/user/update`, { userId, firstname, lastname, gender });
            if (response.data) {
                setAlert({ type: 'danger', message: "Profile updated" });
                setTimeout(() => setAlert(null), 1000);
                setTimeout(() => closeDialog(), 2000);

                const updatedUser = { userId, firstname, lastname, gender };
                await updateUser(updatedUser);

            } else {
                setAlert({ type: 'danger', message: "Update failed" });
                setTimeout(() => setAlert(null), 2000);
                setTimeout(() => closeDialog(), 3000);
            }
        } catch (error) {
            setAlert({ type: 'danger', message: "Server error" });
            setTimeout(() => setAlert(null), 2000);
            setTimeout(() => closeDialog(), 3000);
        }
        setShowEditProfileModal(false);
    }

    return (
        <Modal show={showEditProfileModal} animation={false} onHide={closeDialog}>
            <AlertMessage info={alert} />
            <Modal.Header closeButton>
                <Modal.Title>Edit Profile</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formFirstname">
                        <Form.Label>Firstname</Form.Label>
                        <Form.Control
                            type="text"
                            name="firstname"
                            value={firstname}
                            onChange={(e) => setFirstname(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group controlId="formLastname">
                        <Form.Label>Lastname</Form.Label>
                        <Form.Control
                            type="text"
                            name="lastname"
                            value={lastname}
                            onChange={(e) => setLastname(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group controlId="formGender">
                        <Form.Label>Gender</Form.Label>
                        <Form.Control
                            type="text"
                            name="gender"
                            value={gender}
                            onChange={(e) => setGender(e.target.value)}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={closeDialog}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleEditProfile}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default EditProfileModal;
