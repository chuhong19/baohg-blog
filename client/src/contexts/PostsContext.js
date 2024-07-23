import { createContext, useReducer, useState } from "react";
import { allPostsReducer } from "../reducers/allPostsReducer";
import { apiUrl, ALL_POST_LOADED_SUCCESS, ALL_POST_LOADED_FAIL, MY_POST_LOADED_SUCCESS, MY_POST_LOADED_FAIL, STALK_POST_LOADED_SUCCESS, STALK_POST_LOADED_FAIL, UPDATE_POST, FIND_POST, DELETE_POST, ADD_POST } from "./constants";
import axios from "axios";
import { allMyPostsReducer } from "../reducers/allMyPostsReducer";
import { allStalkPostsReducer } from "../reducers/allStalkPostsReducer";

export const PostsContext = createContext();

const PostsContextProvider = ({ children }) => {

    const [allPostsState, dispatch] = useReducer(allPostsReducer, {
        allPosts: [],
        allPostsLoading: true,
    })

    const [allMyPostsState, dispatch2] = useReducer(allMyPostsReducer, {
        post: null,
        allMyPosts: [],
        allMyPostsLoading: true,
    })

    const [allStalkPostsState, dispatch3] = useReducer(allStalkPostsReducer, {
        allStalkPosts: [],
        allStalkPostsLoading: true,
    })

    const [showInfoPostModal, setShowInfoPostModal] = useState(false);
    const [showReportPostModal, setShowReportPostModal] = useState(false);
    const [showEditPostModal, setShowEditPostModal] = useState(false);
    const [showEditProfileModal, setShowEditProfileModal] = useState(false);
    const [showDeletePostModal, setShowDeletePostModal] = useState(false);
    const [showAddPostModal, setShowAddPostModal] = useState(false);

    // Get all posts
    const getAllPosts = async () => {
        try {
            const response = await axios.get(`${apiUrl}/post/getAllPosts`);
            if (response.data) {
                dispatch({ type: ALL_POST_LOADED_SUCCESS, payload: response.data })
            }
        } catch (error) {
            dispatch({ type: ALL_POST_LOADED_FAIL })
        }
    }

    // Get all my posts
    const getAllMyPosts = async () => {
        try {
            const response = await axios.get(`${apiUrl}/post/getAllMyPosts`);
            if (response.data) {
                dispatch2({ type: MY_POST_LOADED_SUCCESS, payload: response.data })
            }
        } catch (error) {
            dispatch2({ type: MY_POST_LOADED_FAIL })
        }
    }

    // Get all my stalk posts
    const getAllStalkPosts = async (StalkUserId) => {
        try {
            const response = await axios.post(`${apiUrl}/post/getAllAuthorPosts`, { userId: StalkUserId });
            if (response.data) {
                dispatch3({ type: STALK_POST_LOADED_SUCCESS, payload: response.data })
            }
        } catch (error) {
            dispatch3({ type: STALK_POST_LOADED_FAIL })
        }
    }

    // Add post
    const addPost = async (newPost) => {
        try {
            const response = await axios.post(`${apiUrl}/post/create`, newPost);
            if (response.data) {
                dispatch({ type: ADD_POST, payload: response.data.post });
                return response.data;
            }
        } catch (error) {
            return error.response.data
                ? error.response.data
                : { success: false, message: 'Server error' };
        }
    };

    // Find post user clicked on when updating post
    const findPost = (postId) => {
        const post = allMyPostsState.allMyPosts.find((post) => post.id === postId);
        dispatch2({ type: FIND_POST, payload: post });
    };

    // Update post
    const updatePost = async (updatedPost) => {
        try {
            const response = await axios.post(`${apiUrl}/post/update`, {
                postId: updatedPost.postId,
                title: updatedPost.title,
                content: updatedPost.content
            });
            if (response.data.code === '200') {
                const { authorID, ...payload } = response.data.data;
                dispatch2({ type: UPDATE_POST, payload: payload });
            } else {

            }
        } catch (error) {
            return error;
        }
    };

    // Like post
    const likePost = async (postId) => {
        try {
            await axios.post(`${apiUrl}/post/likePost`, { postId: postId });
        } catch (error) {
            return error;
        }
    }

    // Dislike post
    const dislikePost = async (postId) => {
        try {
            await axios.post(`${apiUrl}/post/dislikePost`, { postId: postId });
        } catch (error) {
            return error;
        }
    }

    // Unlike post
    const unlikePost = async (postId) => {
        try {
            await axios.post(`${apiUrl}/post/unlikePost`, { postId: postId });
        } catch (error) {
            return error;
        }
    }

    // UndislikePost post
    const undislikePost = async (postId) => {
        try {
            await axios.post(`${apiUrl}/post/undislikePost`, { postId: postId });
        } catch (error) {
            return error;
        }
    }

    // Delete post
    const deletePost = async (postId) => {
        try {
            await axios.post(`${apiUrl}/post/delete`, { postId: postId });
            dispatch2({ type: DELETE_POST, payload: postId });
        } catch (error) {
            return error;
        }
    }

    // Add this to your context data
    const addComment = async (postId, content) => {
        try {
            const response = await axios.post(`${apiUrl}/post/commentPost`, { postId, content });
            if (response.data.code === '200') {
                // noti something   
            }
        } catch (error) {
            console.error("Error adding comment:", error);
        }
    };

    // Post context data
    const PostsContextData = {
        allPostsState,
        getAllPosts,
        allMyPostsState,
        getAllMyPosts,
        allStalkPostsState,
        getAllStalkPosts,
        showAddPostModal,
        setShowAddPostModal,
        showInfoPostModal,
        setShowInfoPostModal,
        showReportPostModal,
        setShowReportPostModal,
        showEditPostModal,
        setShowEditPostModal,
        showEditProfileModal,
        setShowEditProfileModal,
        showDeletePostModal,
        setShowDeletePostModal,
        addPost,
        findPost,
        updatePost,
        likePost,
        dislikePost,
        unlikePost,
        undislikePost,
        deletePost,
        addComment
    };

    return (
        <PostsContext.Provider value={PostsContextData}>
            {children}
        </PostsContext.Provider>
    )
}

export default PostsContextProvider