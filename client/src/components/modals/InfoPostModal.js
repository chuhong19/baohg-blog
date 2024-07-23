import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useContext } from 'react';
import { PostsContext } from '../../contexts/PostsContext';
import { format, parseISO } from 'date-fns';

const InfoPostModal = ({ postId, postAuthorName, postCreatedAt }) => {

    const { showInfoPostModal, setShowInfoPostModal } = useContext(PostsContext);

    const closeDialog = () => {
        setShowInfoPostModal(false);
    }

    const formatDateTime = (isoDateTime) => {
        const date = parseISO(isoDateTime);
        return format(date, 'hh:mm dd/MM/yyyy');
    };

    const formattedCreatedAt = postCreatedAt ? formatDateTime(postCreatedAt) : 'N/A';

    return (
        <Modal show={showInfoPostModal} animation={false} onHide={closeDialog}>
            <Modal.Header closeButton>
                <Modal.Title>Info post</Modal.Title>
            </Modal.Header>
            <Form>
                <Modal.Body>
                    <h1>Post id: {postId}</h1>
                    <h1>Created by: {postAuthorName}</h1>
                    <h1>Created at: {formattedCreatedAt}</h1>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='primary' onClick={closeDialog}> 
                        OK
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default InfoPostModal;
