import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useContext, useState } from 'react';
import { PostsContext } from '../../contexts/PostsContext';

const AddPostModal = () => {

    const {
        allMyPostsState: { activePost },
        showAddPostModal,
        setShowAddPostModal,
        addPost
    } = useContext(PostsContext);

    // State
    const [newPost, setNewPost] = useState({
        title: '',
        content: '',
    });

    const { title, content } = newPost;

    const onChangeNewPostForm = (event) =>
        setNewPost({ ...newPost, [event.target.name]: event.target.value });

    const closeDialog = () => {
        setShowAddPostModal(false);
    }

    const onSubmit = async (event) => {
        event.preventDefault();
        await addPost(newPost);
        resetAddPostData();
    };

    const resetAddPostData = () => {
        setNewPost({ title: '', content: '' });
        setShowAddPostModal(false);
    };

    return (
        <Modal show={showAddPostModal} animation={false} onHide={closeDialog}>
            <Modal.Header closeButton>
                <Modal.Title>What do you want to learn?</Modal.Title>
            </Modal.Header>
            <Form onSubmit={onSubmit}>
                <Modal.Body>
                    <Form.Group>
                        <Form.Control
                            type='text'
                            placeholder='Title'
                            name='title'
                            required
                            aria-describedby='title-help'
                            value={title}
                            onChange={onChangeNewPostForm}
                        />
                        <Form.Text id='title-help' muted>
                            Required
                        </Form.Text>
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            as='textarea'
                            rows={3}
                            placeholder='Content'
                            name='content'
                            value={content}
                            onChange={onChangeNewPostForm}
                        />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='secondary' onClick={closeDialog}>
                        Cancel
                    </Button>
                    <Button variant='primary' type='submit'>
                        Confirm
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
};

export default AddPostModal;
