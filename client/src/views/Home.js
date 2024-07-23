import MySinglePost from "../components/posts/MySinglePost";
import { useContext, useEffect, useState } from "react"
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
import Spinner from 'react-bootstrap/esm/Spinner';
import { AuthContext } from "../contexts/AuthContext";
import { PostsContext } from "../contexts/PostsContext";
import InfoPostModal from "../components/modals/InfoPostModal";
import EditPostModal from "../components/modals/EditPostModal";
import DeletePostModal from "../components/modals/DeletePostModal";

const Home = () => {

    // Contexts
    const {
        authState: {
            username
        },
    } = useContext(AuthContext);

    const {
        allMyPostsState: { allMyPosts, allMyPostsLoading },
        getAllMyPosts,
        setShowInfoPostModal,
        setShowEditPostModal,
        setShowDeletePostModal
    } = useContext(PostsContext)

    useEffect(() => {
        getAllMyPosts();
    }, []);

    const [selectedPostId, setSelectedPostId] = useState(null);
    const [selectedPostAuthor, setSelectedPostAuthor] = useState(null);
    const [selectedPostCreatedAt, setSelectedPostCreatedAt] = useState(null);
   
    const handleShowInfoModal = (postId, postAuthorName, postCreatedAt) => {
        setShowInfoPostModal(true);
        setSelectedPostId(postId);
        setSelectedPostAuthor(postAuthorName);
        setSelectedPostCreatedAt(postCreatedAt);
    };

    const handleShowEditPostModal = (postId, postAuthorName, postCreatedAt) => {
        setShowEditPostModal(true);
        setSelectedPostId(postId);
        setSelectedPostAuthor(postAuthorName);
        setSelectedPostCreatedAt(postCreatedAt);
    }

    const handleShowDeletePostModal = (postId, postAuthorName, postCreatedAt) => {
        setShowDeletePostModal(true);
        setSelectedPostId(postId);
        setSelectedPostAuthor(postAuthorName);
        setSelectedPostCreatedAt(postCreatedAt);
    }

    let body = null;

    if (allMyPostsLoading) {
        body = (
            <div className='spinner-container'>
                <Spinner animation='border' variant='info' />
            </div>
        );
    } else if (allMyPosts.length === 0) {
        body = (
            <>
                <Card className='text-center mx-5 my-5'>
                    <Card.Header as='h1'>Hi {username}</Card.Header>
                    <Card.Body>
                        <Card.Title>Welcome to Gia Bao blog</Card.Title>
                        <Card.Text>
                            Click the button below to create your first post
                        </Card.Text>
                        <Button
                            variant='primary'
                        >
                            Create post now
                        </Button>
                    </Card.Body>
                </Card>
            </>
        );
    } else {
        body = (
            <>
                <h1>Home</h1>
                <Row className='row-cols-1 row-cols-md-3 g-4 mx-auto mt-3'>
                    {allMyPosts.map((post) => {  
                        return (
                            <Col key={post.id} className='my-2'>
                                <MySinglePost
                                    post={post}
                                    onShowInfoModal={() => handleShowInfoModal(post.id, post.authorName, post.createdAt)}
                                    onShowEditPostModal={() => handleShowEditPostModal(post.id, post.authorName, post.createdAt, post.title, post.content)}
                                    onShowDeletePostModal={() => handleShowDeletePostModal(post.id, post.authorName, post.createdAt)}
                                />
                            </Col>
                        )
                    })}
                </Row>
                {/* Open info post modal */}
                <InfoPostModal postId={selectedPostId} postAuthorName={selectedPostAuthor} postCreatedAt={selectedPostCreatedAt}/>
                {/* Open edit post modal */}
                <EditPostModal postId={selectedPostId} postAuthorName={selectedPostAuthor} postCreatedAt={selectedPostCreatedAt} />
                {/* Open delete post modal */}
                <DeletePostModal postId={selectedPostId} postAuthorName={selectedPostAuthor} postCreatedAt={selectedPostCreatedAt} />
            </>
        )
    }
    return (
        <>{body}</>
    )
}

export default Home;