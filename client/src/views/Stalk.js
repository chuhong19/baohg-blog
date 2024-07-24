import { PostsContext } from "../contexts/PostsContext";
import { useContext, useEffect, useState } from "react"
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Row from 'react-bootstrap/esm/Row';
import Col from 'react-bootstrap/esm/Col';
import Spinner from 'react-bootstrap/esm/Spinner';
import { AuthContext } from "../contexts/AuthContext";
import SinglePost from "../components/posts/SinglePost";
import { useParams } from "react-router-dom";
import axios from "axios";
import { apiUrl } from "../contexts/constants";

const Stalk = () => {
    const { stalkUserId } = useParams();

    const [stalkUserName, setStalkUserName] = useState(null);

    const findUsername = async stalkUserId => {
        try {
            const response = await axios.post(`${apiUrl}/user/getUsernameById`, { userId: stalkUserId });
            if (response.data !== null) {
                setStalkUserName(response.data);
            }
        } catch (error) {
            if (error.response.data) return error.response.data;
            else return {
                success: false,
                message: error.message
            }
        }
    }

    useEffect(() => {
        findUsername(stalkUserId)
    }, []);

    // Contexts
    const {
        authState: {
            username
        },
    } = useContext(AuthContext);

    const {
        allStalkPostsState: { allStalkPosts, allStalkPostsLoading },
        getAllStalkPosts
    } = useContext(PostsContext)

    useEffect(() => {
        getAllStalkPosts(stalkUserId)
    }, [stalkUserId]);

    let body = null;

    if (allStalkPostsLoading) {
        body = (
            <div className='spinner-container'>
                <Spinner animation='border' variant='info' />
            </div>
        );
    } else if (allStalkPosts.length === 0) {
        body = (
            <Card className='text-center mx-5 my-5'>
                <Card.Header as='h1'>Hi {username}</Card.Header>
                <Card.Body>
                    <Card.Title>Welcome to Gia Bao blog</Card.Title>
                    <Card.Text>
                        Nothing here. Nothing for you to stalk :)
                    </Card.Text>
                    <Button
                        variant='primary'
                    >
                        Say hello to him/her now
                    </Button>
                </Card.Body>
            </Card>
        );
    } else {
        body = (
            <>
                <h1>You are stalking {stalkUserName}</h1>
                <Row className='row-cols-1 row-cols-md-3 g-4 mx-auto mt-3'>
                    {allStalkPosts.map((post) => {
                        return (
                            <Col key={post.id} className='my-2'>
                                <SinglePost post={post} />
                            </Col>
                        )
                    })}
                </Row>
            </>
        )
    }
    return (
        <>{body}</>
    )
}

export default Stalk;