import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useContext } from 'react';
import { PostsContext } from '../../contexts/PostsContext';
import AlertMessage from '../layout/AlertMessage';

const DeletePostModal = () => {

    const {
        allMyPostsState: { activePost },
        showDeletePostModal,
        setShowDeletePostModal,
        deletePost
    } = useContext(PostsContext);

    let postId = 0;

    if (activePost !== undefined) {
        postId = activePost.id;
    }

    const closeDialog = () => {
        setShowDeletePostModal(false);
    }

    const onSubmit = async (event) => {
        event.preventDefault();
        await deletePost(postId);
        setShowDeletePostModal(false);
    };

    return (
        <Modal show={showDeletePostModal} animation={false} onHide={closeDialog}>
            <AlertMessage info={alert} />
            <Modal.Header closeButton>
                <Modal.Title>Delete post</Modal.Title>
            </Modal.Header>
            <Form onSubmit={onSubmit}>
                <Modal.Body>
                    <h1>Post id: {postId}</h1>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='primary' type='submit'>
                        Confirm delete
                    </Button>
                    <Button variant='secondary' onClick={closeDialog}>
                        Cancel
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default DeletePostModal;
