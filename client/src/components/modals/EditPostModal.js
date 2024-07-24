import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useContext, useEffect, useState } from 'react';
import { PostsContext } from '../../contexts/PostsContext';
import { format, parseISO } from 'date-fns';
import AlertMessage from '../layout/AlertMessage';

const EditPostModal = () => {

    const {
        allMyPostsState: { activePost },
        showEditPostModal,
        setShowEditPostModal,
        updatePost
    } = useContext(PostsContext);

    let postId = '';
    let postAuthorName = '';
    let postCreatedAt = '';

    if (activePost !== undefined) {
        postId = activePost.id;
        postAuthorName = activePost.authorName;
        postCreatedAt = activePost.createdAt;
    }
    // State
    const [updatedPost, setUpdatedPost] = useState(activePost);

    useEffect(() => setUpdatedPost(activePost), [activePost]);

    const { title, content } = updatedPost || {};

    const onChangeUpdatedPostForm = (event) =>
        setUpdatedPost({ ...updatedPost, [event.target.name]: event.target.value });

    const closeDialog = () => {
        setUpdatedPost(activePost);
        setShowEditPostModal(false);
    }

    const onSubmit = async (event) => {
        event.preventDefault();
        await updatePost({
            postId: updatedPost.id, 
            title: updatedPost.title, 
            content: updatedPost.content
        });
        setShowEditPostModal(false);
    };

    const formatDateTime = (isoDateTime) => {
        const date = parseISO(isoDateTime);
        return format(date, 'hh:mm dd/MM/yyyy');
    };

    const formattedCreatedAt = postCreatedAt ? formatDateTime(postCreatedAt) : 'N/A';

    return (
        <Modal show={showEditPostModal} animation={false} onHide={closeDialog}>
            <AlertMessage info={alert} />
            <Modal.Header closeButton>
                <Modal.Title>Edit post</Modal.Title>
            </Modal.Header>
            <Form onSubmit={onSubmit}>
                <Modal.Body>
                    <Form.Group controlId="formTitle">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type='text'
                            placeholder='Enter title'
                            name='title'
                            value={title}
                            onChange={onChangeUpdatedPostForm}
                        />
                    </Form.Group>
                    <Form.Group controlId="formContent">
                        <Form.Label>Content</Form.Label>
                        <Form.Control
                            as='textarea'
                            rows={3}
                            placeholder='Enter content'
                            name='content'
                            value={content}
                            onChange={onChangeUpdatedPostForm}
                        />
                    </Form.Group>
                    <h1>Post id: {postId}</h1>
                    <h1>Created by: {postAuthorName}</h1>
                    <h1>Created at: {formattedCreatedAt}</h1>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='primary' type='submit'>
                        Save Changes
                    </Button>
                    <Button variant='secondary' onClick={closeDialog}>
                        Cancel
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default EditPostModal;
