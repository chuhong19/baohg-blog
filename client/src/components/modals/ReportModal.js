import { PostsContext } from '../../contexts/PostsContext';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useContext } from 'react';
import axios from 'axios';
import { apiUrl } from '../../contexts/constants';

const ReportPostModal = ({ postId, postAuthorName }) => {

    const { showReportPostModal, setShowReportPostModal } = useContext(PostsContext);

    const closeDialog = () => {
        setShowReportPostModal(false);
    }

    const handleReportPost = async () => {
        try {
            const response = await axios.post(`${apiUrl}/post/reportPost`, {postId: postId});
            if (response.data.data) {
            
            } 
            else {

            }
        } catch (error) {
            return error.message;
        }
    }

    return (
        <Modal show={showReportPostModal} animation={false} onHide={closeDialog}>
            <Modal.Header closeButton>
                <Modal.Title>Report post</Modal.Title>
            </Modal.Header>
            <Form>
                <Modal.Body>
                    <h1>Post id: {postId}</h1>
                    <h1>Author name: {postAuthorName}</h1>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='primary' onClick={handleReportPost}> 
                        Report
                    </Button>
                    <Button variant='secondary' onClick={closeDialog}> 
                        Cancel
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default ReportPostModal;
