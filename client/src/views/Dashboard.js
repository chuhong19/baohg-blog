import { PostsContext } from "../contexts/PostsContext";
import { useContext, useEffect, useState } from "react"
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
import Spinner from 'react-bootstrap/esm/Spinner';
import { AuthContext } from "../contexts/AuthContext";
import SinglePost from "../components/posts/SinglePost";
import InfoPostModal from "../components/modals/InfoPostModal";
import ReportPostModal from "../components/modals/ReportModal";
import OverlayTrigger from 'react-bootstrap/esm/OverlayTrigger';
import Tooltip from 'react-bootstrap/esm/Tooltip';
import addIcon from '../assets/plus-circle-fill.svg';
import AddPostModal from "../components/modals/AddPostModal";

const Dashboard = () => {

    // Contexts
    const {
        authState: {
            username
        },
    } = useContext(AuthContext);

    const {
        allPostsState: { allPosts, allPostsLoading },
        getAllPosts,
        setShowInfoPostModal,
        setShowReportPostModal,
        setShowAddPostModal,
        addComment
    } = useContext(PostsContext)

    useEffect(() => {
        getAllPosts();
    }, [getAllPosts]);

    const [selectedPostId, setSelectedPostId] = useState(null);
    const [selectedPostAuthor, setSelectedPostAuthor] = useState(null);
    const [selectedPostCreatedAt, setSelectedPostCreatedAt] = useState(null);

    const handleShowInfoModal = (postId, postAuthorName, postCreatedAt) => {
        setShowInfoPostModal(true);
        setSelectedPostId(postId);
        setSelectedPostAuthor(postAuthorName);
        setSelectedPostCreatedAt(postCreatedAt);
    };

    const handleShowReportPostModal = (postId, postAuthorName) => {
        setShowReportPostModal(true);
        setSelectedPostId(postId);
        setSelectedPostAuthor(postAuthorName);
    }

    const handleAddComment = async (postId, content) => {
        await addComment(postId, content);
    };

    let body = null;

    if (allPostsLoading) {
        body = (
            <div className='spinner-container'>
                <Spinner animation='border' variant='info' />
            </div>
        );
    } else if (allPosts.length === 0) {
        body = (
            <>
                <Card className='text-center mx-5 my-5'>
                    <Card.Header as='h1'>Hi {username}</Card.Header>
                    <Card.Body>
                        <Card.Title>Welcome to Gia Bao blog</Card.Title>
                        <Card.Text>
                            Nothing here. Click the button below to create our first post
                        </Card.Text>
                        <Button
                            variant='primary'
                            onClick={() => setShowAddPostModal(true)}
                            // onClick={setShowAddPostModal.bind(this, true)}
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
                <h1>Dashboard</h1>
                <Row className='row-cols-1 row-cols-md-3 g-4 mx-auto mt-3'>
                    {allPosts.map((post) => {
                        return (
                            <Col key={post.id} className='my-2'>
                                <SinglePost
                                    post={post}
                                    onShowInfoModal={() => handleShowInfoModal(post.id, post.authorName, post.createdAt)}
                                    onShowReportPostModal={() => handleShowReportPostModal(post.id, post.authorName)}
                                    onAddComment={handleAddComment}
                                />
                            </Col>
                        )
                    })}
                </Row>
                {/* Open add post modal */}
                <OverlayTrigger
                    placement='left'
                    overlay={<Tooltip>Add a new post</Tooltip>}
                >
                    <Button
                        className='btn-floating'
                        onClick={() => setShowAddPostModal(true)}

                    >
                        <img src={addIcon} alt='add-post' width='60' height='60' />
                    </Button>
                </OverlayTrigger>
                {/* Open info post modal */}
                <InfoPostModal postId={selectedPostId} postAuthorName={selectedPostAuthor} postCreatedAt={selectedPostCreatedAt} />
                {/* Open report post modal */}
                <ReportPostModal postId={selectedPostId} postAuthorName={selectedPostAuthor} />
            </>
        )
    }
    return (
        <>
            {body}
            <AddPostModal />
        </>
    )
}

export default Dashboard;
